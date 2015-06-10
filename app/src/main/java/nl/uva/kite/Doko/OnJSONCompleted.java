package nl.uva.kite.Doko;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnJSONCompleted {
    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    public static final int FRIENDADD = 2;
    public static final int FRIENDLISTUPDATE = 3;
    public static final int FRIENDLISTOPEN = 5;
    public static final int GROUPCREATE = 6;
    public static final int GROUPADDUSER = 7;
    public static final int SELECTFRIEND = 8;

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
                    final LinearLayout lm = (LinearLayout) a.findViewById(R.id.select_friend);
                    for (String friend : friends) {
                        Button myButton = new Button(ctext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        myButton.setText(friend);
                        myButton.setLayoutParams(params);
                        LinearLayout ll = new LinearLayout(ctext);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.addView(myButton);
                        lm.addView(ll);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}