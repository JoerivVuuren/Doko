package nl.uva.kite.moneymoneymoney;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    private static final String PHPURL = "http://intotheblu.nl/friends_add.php";

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

            JSONObject json = jsonParser.makeHttpRequest(PHPURL, "POST", params);

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
}
