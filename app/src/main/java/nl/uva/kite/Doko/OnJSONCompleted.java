package nl.uva.kite.Doko;

import android.content.Context;
import android.util.Log;

import com.parse.ParseInstallation;

import org.json.JSONException;
import org.json.JSONObject;

public class OnJSONCompleted {
    public static final int LOGIN = 0;
    public static final int REGISTER = 1;

    public static void dotask(int type, JSONObject json, Context context) {
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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}