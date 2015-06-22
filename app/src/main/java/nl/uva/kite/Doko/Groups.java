package nl.uva.kite.Doko;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Groups extends Fragment {
    private RelativeLayout layout;
    public static ListView listView;

    public static int current_group_id = -1;
    public static String current_group_name;
    public static String current_group_admin_name;
    public static String[] current_group_members;
    public static String[] current_group_pictures;
    public static double[] current_group_debts;
    public static String[] current_group_debts_euro;

    public static String[] groups;
    public static String[] requests;
    public static int[] group_ids;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context thisContext = this.getActivity();
        super.onCreate(savedInstanceState);
        layout = (RelativeLayout)inflater.inflate(R.layout.groups, container, false);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) layout.findViewById(R.id.swipeContainerGroups);
//        set refresh color
        swipeView.setColorSchemeColors(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );

        listView = (ListView) layout.findViewById(R.id.mygroups_list);
        //        sweet refresh on pulldown spinner
        swipeView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeView.setRefreshing(true);
                        if (Login.isLoggedIn()) {
//                            update the list!
                            if (Login.isLoggedIn())
                            Groups.get_grouplist(OnJSONCompleted.GROUPLISTOPEN, thisContext);
                        }
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeView.setRefreshing(false);

                            }
                        }, 1000);
                    }
                });


        if (Login.isLoggedIn()) {
            /* get friend list from DB and update list of friends in Fragment */
            Groups.get_group_request_list(OnJSONCompleted.GROUPREQUESTUPDATE, this.getActivity());
            Groups.get_grouplist(OnJSONCompleted.GROUPLISTOPEN, this.getActivity());
        }

        return layout;
    }

    /* creates a group with admin = creator */
    public static void create(String groupName, Context ctext) {
        if (!Login.isLoggedIn() || groupName == null || groupName.isEmpty())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("groupname", groupName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.GROUPCREATE);
        jr.execute("http://intotheblu.nl/group_create.php");
    }

    /* adds a user to group */
    public static void adduser(String username, int group_id, Context ctext) {
        if (!Login.isLoggedIn() || username == null || username.isEmpty())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + group_id));
        params.add(new BasicNameValuePair("adduser", username));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.GROUPADDUSER);
        jr.execute("http://intotheblu.nl/group_adduser.php");
    }

    public static void add_request(String friendName, int groupid, String group_name, Context ctext) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + groupid));
        params.add(new BasicNameValuePair("group_name", group_name));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/group_request_add.php");
    }

    public static void deny_request(String friendName, Context ctext){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/group_request_delete.php");
    }

    /* retrieves the group list from DB */
    public static void get_grouplist(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/group_list.php");
    }

    /* retrieves list of members of a group */
    public static void get_groupmembers(Context context) {
        if(!Login.isLoggedIn() || Groups.current_group_id == -1) {
            return;
        }
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + Groups.current_group_id));
        JSONRetrieve jr = new JSONRetrieve(context, params, OnJSONCompleted.GROUPMEMBERSLIST);
        jr.execute("http://intotheblu.nl/group_members.php");
    }

    /* retrieves the user's friend request list from DB */
    public static void get_group_request_list(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/group_request_list.php");
    }

    /* activates group */
    public static void activateGroup(int id) {
        Groups.current_group_id = id;
        Groups.current_group_name = groupIDtoName(id);

        /* save group_id for next App start */
        Login.securePreferences.put("group_id", "" + id);
    }

    /* returns group name for group id;
     * returns "" if group id not found */
    public static String groupIDtoName(int id) {
        if (Groups.group_ids == null)
            return "";

        int index = -1;
        for (int i = 0; i < Groups.group_ids.length; i++) {
            if (Groups.group_ids[i] == id) {
                index = i;
                break;
            }
        }
        if (index != -1)
            return Groups.groups[index];

        return "";
    }
}
