package nl.uva.kite.Doko;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class Groups {
    public static String[] groups;
    public static int[] group_ids;

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

    /* retrieves the group list from DB */
    public static void get_grouplist(Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.GROUPLIST);
        jr.execute("http://intotheblu.nl/group_list.php");
    }
}
