package nl.uva.kite.Doko;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.R;

public class Friends extends Activity {
    public static String[] friends;

    /* adds a friend to the user's friend list */
    public static void add(String friendName, Context ctext) {
        if (!Login.isLoggedIn() || friendName.equals(Login.getLoginName()))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FRIENDADD);
        jr.execute("http://intotheblu.nl/friends_add.php");
    }

    /* retrieves the user's friends list from DB */
    public static void get_friendlist(Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FRIENDLIST);
        jr.execute("http://intotheblu.nl/friends_list.php");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        if (!Login.isLoggedIn()) {
            finish();
        }
        else {
            /* list friends on screen */

            String ftext = "Your friends:\n\n";
            for (String f : friends) {
                ftext += f + "\n";
            }
            TextView tv = (TextView)findViewById(R.id.friends_list);
            tv.setText(ftext);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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
}
