package nl.uva.kite.Doko;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Groups extends Activity {
    public static String[] groups;
    public static int[] group_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        //final LinearLayout lm = (LinearLayout)findViewById(R.id.mygroups);

        ListView friendsListView = (ListView)findViewById(R.id.mygroups_list);

        ArrayList<String> groupList = new ArrayList<String>();
        groupList.addAll(Arrays.asList(groups));
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, groupList);

        friendsListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
