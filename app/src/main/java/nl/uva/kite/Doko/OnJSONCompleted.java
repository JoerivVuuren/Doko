package nl.uva.kite.Doko;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static void dotask(int type, JSONObject json, Context ctext) {
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
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, arrList);

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

                        int group_id = Groups.group_ids[position];
                        String group_name = parent.getItemAtPosition(position).toString();

                        Log.e("group selected", "id=" + group_id + ", name=" + group_name);
                    }
                });
            }
            else if (type == GROUPMEMBERSLIST) {
                JSONArray jmembers = json.getJSONArray("members");
                String[] member_list = new String[jmembers.length()];
                for (int i = 0; i < member_list.length; i++) {
                    member_list[i] = jmembers.getString(i);
                }
                Groups.group_members = member_list;
                Activity a = (Activity) ctext;
                ListView memberListView = (ListView) a.findViewById(R.id.groups_list);
                ArrayList<String> memberList = new ArrayList<String>();
                memberList.addAll( Arrays.asList(member_list) );
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.member_list_row,R.id.member_group_list_name,  memberList);
                memberListView.setAdapter( listAdapter );
            }
            else if (type == GROUPCREATE) {
                // activate group
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}