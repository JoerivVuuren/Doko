package nl.uva.kite.Doko;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

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
    public static final int FRIENDLISTOPEN = 4;
    public static final int SELECTFRIEND = 8;
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
            else if (type == FRIENDLISTOPEN || type == FRIENDLISTUPDATE || type == SELECTFRIEND) {
                JSONArray jfriends = json.getJSONArray("friends");
                String[] friend_list = new String[jfriends.length()];
                for (int i = 0; i < friend_list.length; i++) {
                    friend_list[i] = jfriends.getString(i);
                }

                Friends.friends = friend_list;
                if (type == FRIENDLISTOPEN) {
                    Intent intent = new Intent(ctext, Friends.class);
                    ctext.startActivity(intent);
                }
                else if (type == SELECTFRIEND) {
                    Activity a = (Activity) ctext;
                    a.setContentView(R.layout.activity_select_friend);

                    String[] friends = Friends.friends;

                    ListView friendsListView = (ListView) a.findViewById(R.id.select_friend_list);

                    ArrayList<String> friendList = new ArrayList<String>();
                    friendList.addAll( Arrays.asList(friends) );
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, friendList);

                    friendsListView.setAdapter( listAdapter );
                }
            }
            else if (type == GROUPLISTUPDATE || type == GROUPLISTOPEN) {
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
                    Intent intent = new Intent(ctext, Groups.class);
                    ctext.startActivity(intent);
                }
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
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ctext, R.layout.simplerow, memberList);
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