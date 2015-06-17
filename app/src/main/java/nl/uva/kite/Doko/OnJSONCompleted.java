package nl.uva.kite.Doko;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import java.util.Formatter;
import java.util.Locale;

import nl.uva.kite.Doko.Fragments.Tabs.Tab2;

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
    public static final int FRIENDREQUESTUPDATE = 15;
    public static final String imagesDirectory = "http://intotheblu.nl:2222/CMD_FILE_MANAGER/images/";
    public static final int DEBTADD = 16;
    public static final int ALLREQUESTUPDATE = 17;

    public static void dotask(int type, JSONObject json, final Context ctext) {
        try {
            if (type == LOGIN) {
                if (json.getInt("success") == 1) {
                    Log.d("Login Successful!", json.toString());

                    Login.loggedIn = true;

                    // Set Parse username data.
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    //ParseUser currentUser = ParseUser.getCurrentUser();
                    //currentUser.setUsername(username);
                    installation.put("username", Login.getLoginName());
                    //Log.e("", "set current user to: " + ParseUser.getCurrentUser().getUsername());
                    installation.saveInBackground();
                }
                else {
                    Login.loggedIn = false;
                    Log.d("Login Failure!", json.getString("message"));
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
                Activity a = (Activity)ctext;
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
                Activity a = (Activity)ctext;
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
            else if (type == GROUPLISTUPDATE) {

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

                /* create a ListView for groups */
                Activity a = (Activity)ctext;
                final ListView groupsListView = (ListView)a.findViewById(R.id.mygroups_list);

                ArrayList<String> arrList = new ArrayList<String>();
                arrList.addAll(Arrays.asList(group_names));

                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, arrList);

                groupsListView.setAdapter(listAdapter);

                FloatingActionButton fab = (FloatingActionButton)a.findViewById(R.id.groups_fab);
                fab.attachToListView(groupsListView);

                groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (position > Groups.group_ids.length - 1)
                            return;

                        /* user clicked this group: */
                        int group_id = Groups.group_ids[position];
                        String group_name = parent.getItemAtPosition(position).toString();

                        Groups.current_group_id = group_id;
                        Groups.current_group_name = group_name;

                        /* restart MainActivity */
                        Activity a = (Activity)ctext;
                        Intent intent = new Intent(ctext, MainActivity.class);
                        a.finish();
                        ctext.startActivity(intent);
                    }
                });
            }
            else if (type == GROUPMEMBERSLIST) {

                /* fill Groups.current_group_* with json response */
                final String EURO = "\u20AC";
                JSONArray jmembers = json.getJSONArray("users");
                JSONArray jpics = json.getJSONArray("profile_picture");
                JSONArray jdebt = json.getJSONArray("debts");
                Groups.current_group_members = new String[jmembers.length()];
                Groups.current_group_pictures = new String[jmembers.length()];
                Groups.current_group_debts = new double[jmembers.length()];
                Groups.current_group_debts_euro = new String[jmembers.length()];

                for (int i = 0; i < jmembers.length(); i++) {
                    Groups.current_group_members[i] = jmembers.getString(i);
                    Groups.current_group_pictures[i] = jpics.getString(i);
                    Groups.current_group_debts[i] = jdebt.getDouble(i);

                    String debtPrefix = "";
                    double debt = Groups.current_group_debts[i];
                    if (debt < 0) {
                        /* move minus sign in front of euro sign */
                        debtPrefix = "- ";
                        debt = -debt;
                    }
                    Groups.current_group_debts_euro[i] = String.format(debtPrefix + EURO + " %.2f", debt);
                }

                /* create ListView using MemberlistArrayAdapter */
                Activity a = (Activity)ctext;
                ListView memberListView = (ListView)a.findViewById(R.id.groups_list);
                MemberlistArrayAdapter listAdapter = new MemberlistArrayAdapter(ctext, Groups.current_group_members);
                memberListView.setAdapter(listAdapter);
            }
            else if (type == GROUPCREATE) {
                // activate group
            }
            else if (type == DEBTADD) {
                //doeiets
            }
            else if (type == ALLREQUESTUPDATE) {
                JSONArray jrequests = json.getJSONArray("senders");
                String[] request_list = new String[jrequests.length()];
                for (int i = 0; i < request_list.length; i++) {
                    request_list[i] = jrequests.getString(i);
                }

                Tab2.requests = request_list;

                /* create a ListView for requests */
                Activity a = (Activity)ctext;
                final ListView requestListView = (ListView)a.findViewById(R.id.game_request_list);
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.addAll(Arrays.asList(request_list));
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.friend_request_list_row,R.id.friend_request_name, arrList);

                requestListView.setAdapter(listAdapter);

                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctext);

                        alertDialogBuilder.setTitle("Accept the game from " + "?");

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
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}