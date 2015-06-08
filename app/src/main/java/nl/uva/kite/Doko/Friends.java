package nl.uva.kite.Doko;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.R;

public class Friends extends AppCompatActivity {
    private static final String PHPADD = "http://intotheblu.nl/friends_add.php";
    private static final String PHPLIST = "http://intotheblu.nl/friends_list.php";

    /* adds a friend to the user's friend list;
     * returns: 1 on success
     *          0 on failure */
    public int add(String friendName) {
        if (!Login.isLoggedIn() || friendName.equals(Login.getLoginName()))
            return 0;

        JSONParser jsonParser = new JSONParser();

        int success;
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", Login.getLoginName()));
            params.add(new BasicNameValuePair("password", Login.getPassword()));
            params.add(new BasicNameValuePair("friend", friendName));

            JSONObject json = jsonParser.makeHttpRequest(PHPADD, "POST", params);

            success = json.getInt("success");
            if (success == 1) {
                return 1;
            }
            else {
                return 0;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /* retrieves the user's friends list from DB */
    public String[] get_friendlist() {
        if (!Login.isLoggedIn())
            return null;

        Log.e("friends", "getting friend_list");

        JSONParser jsonParser = new JSONParser();
        String[] friend_list = null;

        int success;
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", Login.getLoginName()));
            params.add(new BasicNameValuePair("password", Login.getPassword()));

            JSONObject json = jsonParser.makeHttpRequest(PHPLIST, "POST", params);

            JSONArray jfriends = json.getJSONArray("friends");
            friend_list = new String[jfriends.length()];
            for (int i = 0; i < friend_list.length; i++) {
                friend_list[i] = jfriends.getString(i);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("friends", "getting friends2");

        for (String f : friend_list) {
            Log.e("friend", f);
        }

        return friend_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        String[] friends = get_friendlist();
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
