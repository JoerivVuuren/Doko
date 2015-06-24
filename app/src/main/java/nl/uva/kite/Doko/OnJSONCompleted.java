package nl.uva.kite.Doko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import nl.uva.kite.Doko.Adapters.MemberListArrayAdapter;
import nl.uva.kite.Doko.Fragments.Tabs.Tab2;
import nl.uva.kite.Doko.Fragments.Tabs.Tab4;

public class OnJSONCompleted {
    public static final int NONE = -1;
    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    public static final int FRIENDADD = 2;
    public static final int FRIENDLISTUPDATE = 3;
    public static final int GROUPCREATE = 10;
    public static final int GROUPADDUSER = 11;
    public static final int GROUPLISTUPDATE = 12;
    public static final int GROUPLISTOPEN = 13;
    public static final int GROUPMEMBERSLIST = 14;
    public static final int GROUPREQUESTUPDATE = 20;
    public static final int FRIENDREQUESTUPDATE = 15;
    public static final String imagesDirectory = "http://intotheblu.nl:2222/CMD_FILE_MANAGER/images/";
    public static final int DEBTADD = 16;
    public static final int ALLREQUESTUPDATE = 17;
    public static final int STARTGAME = 21;
    public static final int UPDATEGAME = 24;
    public static final int GAMELISTUPDATE = 25;
    public static final int LOADGAME = 25;

    public static void dotask(int type, JSONObject json, final Context ctext) {
        try {
            Activity a = (Activity)ctext;

            if (type == LOGIN) {
                if (json.getInt("success") == 1) {
                    Login.setLoggedIn(true);

                    // Set Parse username data.
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    //ParseUser currentUser = ParseUser.getCurrentUser();
                    //currentUser.setUsername(username);
                    installation.put("username", Login.getLoginName());
                    //Log.e("", "set current user to: " + ParseUser.getCurrentUser().getUsername());
                    installation.saveInBackground();

                    /* close Login activity and open MainActivity */
                    Intent intent = new Intent(ctext, MainActivity.class);
                    a.finish();
                    ctext.startActivity(intent);
                }
                else {
                    /* disable autologin */
                    Login.securePreferences.put("autologin", "0");
                    Login.setLoggedIn(false);
                }
            }
            else if (type == REGISTER) {
                if (json.getInt("success") == 1) {
                    /* close Register activity and auto login user */
                    a.finish();
                    Login.securePreferences.put("autologin", "1");
                    Login.attemptLogin(Login.mContext, Login.securePreferences.getString("username"),
                            Login.securePreferences.getString("password"));
                }
            }
            else if (type == FRIENDLISTUPDATE) {

                /* fill Friends.friends with json response */
                JSONArray jfriends = json.getJSONArray("friends");
                String[] friend_list = new String[jfriends.length()];
                for (int i = 0; i < friend_list.length; i++) {
                    friend_list[i] = jfriends.getString(i);
                }

                Friends.friends = friend_list;

                /* create a ListView for friends */
                final ListView friendListView = (ListView)a.findViewById(R.id.friends_list);
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.addAll(Arrays.asList(friend_list));
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.member_list_row,R.id.member_group_list_name, arrList);

                friendListView.setAdapter(listAdapter);

                FloatingActionButton fab = (FloatingActionButton)a.findViewById(R.id.friends_fab);
                fab.attachToListView(friendListView);

                friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        String friend_name = parent.getItemAtPosition(position).toString();

                        Log.e("friend selected", "friend name=" + friend_name);
                    }
                });
            }
            else if(type == FRIENDREQUESTUPDATE){
                /* fill Friends.requests with json response */
                JSONArray jrequests = json.getJSONArray("senders");
                String[] request_list = new String[jrequests.length()];
                for (int i = 0; i < request_list.length; i++) {
                    request_list[i] = jrequests.getString(i);
                }

                Friends.requests = request_list;

                /* create a ListView for requests */
                final ListView requestListView = (ListView)a.findViewById(R.id.requests_list);
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.addAll(Arrays.asList(request_list));
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, arrList);

                requestListView.setAdapter(listAdapter);

                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctext);

                        alertDialogBuilder.setTitle("Add " + ((TextView) view).getText() + " to friends?");

                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Friends.add(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Friends.deny_request(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });
            }
            else if (type == GROUPLISTOPEN || type == GROUPLISTUPDATE) {

                /* fill Groups.group_ids and .group_names with json response */
                JSONArray jgroups = json.getJSONArray("groups");
                String[] group_names = new String[jgroups.length()];
                int[] group_ids = new int[jgroups.length()];

                for (int i = 0; i < group_names.length; i++) {
                    JSONObject grp = jgroups.getJSONObject(i);
                    group_ids[i] = grp.getInt("id");
                    group_names[i] = grp.getString("name");
                }

                Groups.groups = group_names;
                Groups.group_ids = group_ids;

                if (type == GROUPLISTOPEN) {
                    /* create a ListView for groups */
                    final ListView groupsListView = (ListView) a.findViewById(R.id.mygroups_list);

                    ArrayList<String> arrList = new ArrayList<String>();
                    arrList.addAll(Arrays.asList(group_names));

                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, arrList);

                    groupsListView.setAdapter(listAdapter);

                    FloatingActionButton fab = (FloatingActionButton) a.findViewById(R.id.groups_fab);
                    fab.attachToListView(groupsListView);

                    groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            if (position > Groups.group_ids.length - 1)
                                return;

                            /* user clicked this group: */
                            int group_id = Groups.group_ids[position];

                            Groups.activateGroup(group_id, Groups.groupIDtoName(group_id));

                            /* restart MainActivity */
                            Activity a = (Activity) ctext;
                            Intent intent = new Intent(ctext, MainActivity.class);
                            a.finish();
                            ctext.startActivity(intent);
                        }
                    });
                }
            }
            else if (type == GROUPMEMBERSLIST) {

                /* fill Groups.current_group_* with json response */
                JSONArray jmembers = json.getJSONArray("users");
                JSONArray jpics = json.getJSONArray("profile_picture");
                JSONArray jdebt = json.getJSONArray("debts");
                Groups.current_group_admin_name = json.getString("admin_name");
                Groups.current_group_members = new String[jmembers.length()];
                Groups.current_group_pictures = new String[jmembers.length()];
                Groups.current_group_debts = new double[jmembers.length()];
                Groups.current_group_debts_euro = new String[jmembers.length()];

                for (int i = 0; i < jmembers.length(); i++) {
                    Groups.current_group_members[i] = jmembers.getString(i);
                    Groups.current_group_pictures[i] = jpics.getString(i);
                    Groups.current_group_debts[i] = jdebt.getDouble(i);
                    Groups.current_group_debts_euro[i] = MainActivity.doubleToEuro(Groups.current_group_debts[i]);
                }

                /* hide + button if user is not admin */
                FloatingActionButton fab = (FloatingActionButton)a.findViewById(R.id.fabulous_fab);
                if (!Groups.current_group_admin_name.equals(Login.getLoginName()))
                    fab.hide();
                else
                    fab.show();

                /* create ListView using MemberListArrayAdapter */
                ListView memberListView = (ListView)a.findViewById(R.id.groups_list);
                MemberListArrayAdapter listAdapter = new MemberListArrayAdapter(ctext, Groups.current_group_members);
                memberListView.setAdapter(listAdapter);
            }
            else if(type == GROUPREQUESTUPDATE){
                /* fill Friends.requests with json response */
                JSONArray jrequests_group_names = json.getJSONArray("group_name");
                JSONArray jrequests_group_id = json.getJSONArray("group_id");
                String[] request_list = new String[jrequests_group_names.length()];
                final String[] id_list = new String[jrequests_group_names.length()];
                for (int i = 0; i < request_list.length; i++) {
                    request_list[i] = jrequests_group_names.getString(i);
                    id_list[i] = jrequests_group_id.getString(i);
                }

                Groups.requests = request_list;

                /* create a ListView for requests */
                final ListView requestListView = (ListView)a.findViewById(R.id.requests_list);
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.addAll(Arrays.asList(request_list));
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, arrList);

                requestListView.setAdapter(listAdapter);

                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctext);

                        alertDialogBuilder.setTitle("Do you want to join the group " + ((TextView) view).getText() + "?");

                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Groups.adduser(Login.getLoginName(), id_list[id+1], view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Groups.deny_request(id_list[id+1], view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });
            }
            else if (type == GROUPCREATE) {
                /* activate new group id */
                Groups.activateGroup(Integer.parseInt(json.getString("group_id")), json.getString("group_name"));

                /* restart MainActivity */
                Intent intent = new Intent(ctext, MainActivity.class);
                a.finish();
                ctext.startActivity(intent);
            }
            else if (type == DEBTADD) {
                //doeiets
            }
            else if (type == ALLREQUESTUPDATE) {
                JSONArray jrequests_game = json.getJSONArray("game_sender");
                JSONArray jrequests_game_id = json.getJSONArray("game_id");
                JSONArray jrequests_game_amount = json.getJSONArray("game_amount");
                JSONArray jrequests_debit = json.getJSONArray("debit_sender");
                JSONArray jrequests_debit_id = json.getJSONArray("debit_id");
                JSONArray jrequests_debit_debt = json.getJSONArray("debit_debt");
                JSONArray jrequests_debit_reason = json.getJSONArray("debit_reason");
                JSONArray jrequests_credit = json.getJSONArray("credit_sender");
                JSONArray jrequests_credit_id = json.getJSONArray("credit_id");
                JSONArray jrequests_credit_debt = json.getJSONArray("credit_debt");
                JSONArray jrequests_credit_reason = json.getJSONArray("credit_reason");
                Log.e("", "came in request update");

                Tab2.requests_game = new String[jrequests_game.length()];
                Tab2.requests_game_id = new int[jrequests_game.length()];
                Tab2.requests_game_amount = new double[jrequests_game.length()];
                for (int i = 0; i < jrequests_game.length(); i++) {
                    Tab2.requests_game[i] = jrequests_game.getString(i);
                    Tab2.requests_game_id[i] = jrequests_game_id.getInt(i);
                    Tab2.requests_game_amount[i] = jrequests_game_amount.getDouble(i);
                }

                Tab2.requests_debit = new String[jrequests_debit.length()];
                Tab2.requests_debit_id = new int[jrequests_debit.length()];
                Tab2.requests_debit_amount = new double[jrequests_debit.length()];
                Tab2.requests_debit_reason = new String[jrequests_debit.length()];
                for (int i = 0; i < jrequests_debit.length(); i++) {
                    Tab2.requests_debit[i] = jrequests_debit.getString(i);
                    Tab2.requests_debit_id[i] = jrequests_debit_id.getInt(i);
                    Tab2.requests_debit_amount[i] = jrequests_debit_debt.getDouble(i);
                    Tab2.requests_debit_reason[i] = jrequests_debit_reason.getString(i);
                }

                Tab2.requests_credit = new String[jrequests_credit.length()];
                Tab2.requests_credit_id = new int[jrequests_credit.length()];
                Tab2.requests_credit_amount = new double[jrequests_credit.length()];
                Tab2.requests_credit_reason = new String[jrequests_credit.length()];
                for (int i = 0; i < jrequests_credit.length(); i++) {
                    Tab2.requests_credit[i] = jrequests_credit.getString(i);
                    Tab2.requests_credit_id[i] = jrequests_credit_id.getInt(i);
                    Tab2.requests_credit_amount[i] = jrequests_credit_debt.getDouble(i);
                    Tab2.requests_credit_reason[i] = jrequests_credit_reason.getString(i);
                }

                /* create a ListView for game requests */
                ListView gameReqView = (ListView)a.findViewById(R.id.game_request_list);
                RequestlistAdapter gameReqAdapter = new RequestlistAdapter(ctext, Tab2.requests_game);
                gameReqAdapter.setType(gameReqAdapter.GAME);
                gameReqView.setAdapter(gameReqAdapter);
                RequestlistAdapter.setListViewHeightBasedOnChildren(gameReqView);

                /* create a ListView for credit requests */
                ListView creditReqView = (ListView)a.findViewById(R.id.credit_request_list);
                RequestlistAdapter creditReqAdapter = new RequestlistAdapter(ctext, Tab2.requests_credit);
                creditReqAdapter.setType(creditReqAdapter.CREDIT);
                creditReqView.setAdapter(creditReqAdapter);
                RequestlistAdapter.setListViewHeightBasedOnChildren(creditReqView);

                /* create a ListView for debit requests */
                ListView debitReqView = (ListView)a.findViewById(R.id.debit_request_list);
                RequestlistAdapter debitReqAdapter = new RequestlistAdapter(ctext, Tab2.requests_debit);
                debitReqAdapter.setType(debitReqAdapter.DEBIT);
                debitReqView.setAdapter(debitReqAdapter);
                RequestlistAdapter.setListViewHeightBasedOnChildren(debitReqView);

                /* create a ListView for debit requests
                //Activity ab = (Activity)ctext;
                final ListView requestListView_debit = (ListView)a.findViewById(R.id.debit_request_list);
                ArrayList<String> arrList_debit = new ArrayList<String>();
                arrList_debit.addAll(Arrays.asList(request_list_debit));
                ArrayAdapter<String> listAdapter_debit = new ArrayAdapter<String>(ctext, R.layout.simplerow, R.id.rowTextView, arrList_debit);

                requestListView_debit.setAdapter(listAdapter_debit);

                requestListView_debit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctext);

                        alertDialogBuilder.setTitle("Accept the debit from " + ((TextView) view).getText() + "?");

                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Tab2.add(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Tab2.deny_request(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });

                /* create a ListView for credit requests
                //Activity ab = (Activity)ctext;
                final ListView requestListView_credit = (ListView)a.findViewById(R.id.credit_request_list);
                ArrayList<String> arrList_credit = new ArrayList<String>();
                arrList_credit.addAll(Arrays.asList(request_list_credit));
                ArrayAdapter<String> listAdapter_debt = new ArrayAdapter<String>(ctext, R.layout.simplerow, R.id.rowTextView, arrList_credit);

                requestListView_credit.setAdapter(listAdapter_debt);

                requestListView_credit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctext);

                        alertDialogBuilder.setTitle("Accept the credit from " + ((TextView) view).getText() + "?");

                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Tab2.add(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Tab2.deny_request(((TextView) view).getText().toString(), view.getContext());
                                        view.setVisibility(View.GONE);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });*/
            }
            else if (type == FRIENDADD){
                Friends.get_friendlist(FRIENDLISTUPDATE, ctext);
            }
            else if (type == GROUPADDUSER){
                Groups.get_grouplist(GROUPLISTOPEN, ctext);
            }
            else if (type == STARTGAME) {
                Intent tryIntent;
                tryIntent = new Intent(ctext, TicTacToe.class);
                tryIntent.putExtra("class", "STARTGAME");
                tryIntent.putExtra("game_id", "" + json.getInt("game_id"));
                a.startActivity(tryIntent);
                //tryIntent.putExtra("Requests", "friend");
                //NotificationCompat.Builder builder = new NotificationCompat.Builder(ctext);
                //TicTacToe.getUpdate(json.getInt("group_id"), ctext);
            }
            else if (type == UPDATEGAME || type == LOADGAME) {
                Intent tryIntent;
                tryIntent = new Intent(ctext, TicTacToe.class);
                tryIntent.putExtra("class", "UPDATEGAME");
                tryIntent.putExtra("game_id", "" + json.getInt("game_id"));
                tryIntent.putExtra("veld0", "" + json.getInt("veld0"));
                tryIntent.putExtra("veld1", "" + json.getInt("veld1"));
                tryIntent.putExtra("veld2", "" + json.getInt("veld2"));
                tryIntent.putExtra("veld3", "" + json.getInt("veld3"));
                tryIntent.putExtra("veld4", "" + json.getInt("veld4"));
                tryIntent.putExtra("veld5", "" + json.getInt("veld5"));
                tryIntent.putExtra("veld6", "" + json.getInt("veld6"));
                tryIntent.putExtra("veld7", "" + json.getInt("veld7"));
                tryIntent.putExtra("veld8", "" + json.getInt("veld8"));
                tryIntent.putExtra("turn", "" + json.getInt("turn"));

                a.startActivity(tryIntent);
            }
            else if (type == GAMELISTUPDATE) {
                JSONArray jrequests_game_opponent = json.getJSONArray("opponent");
                JSONArray jrequests_game_amount = json.getJSONArray("wager");
                JSONArray jrequests_game_id = json.getJSONArray("game_id");
                Log.e("", "some kind of respnse");

                Tab4.game_opponents = new String[jrequests_game_opponent.length()];
                Tab4.game_wagers = new double[jrequests_game_opponent.length()];
                Tab4.game_id = new int[jrequests_game_opponent.length()];
                for (int i = 0; i < jrequests_game_opponent.length(); i++) {
                    Log.e("", "opponent is: " + jrequests_game_opponent.getString(i));
                    Tab4.game_opponents[i] = jrequests_game_opponent.getString(i);
                    Tab4.game_wagers[i] = jrequests_game_amount.getDouble(i);
                    Tab4.game_id[i] = jrequests_game_id.getInt(i);
                }

                 /* create a ListView for games */
                ListView gamesView = (ListView)a.findViewById(R.id.game_list);
                RequestlistAdapter gamesAdapter = new RequestlistAdapter(ctext, Tab4.game_opponents);
                gamesAdapter.setType(gamesAdapter.GAMELIST);
                gamesView.setAdapter(gamesAdapter);
                RequestlistAdapter.setListViewHeightBasedOnChildren(gamesView);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}