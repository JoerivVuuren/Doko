package nl.uva.kite.moneymoneymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseCrashReporting;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Homescreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
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
    /* opens the login screen activity */
    public void OpenLoginScreen(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    /* opens the profile screen activity */
    public void OpenProfileScreen(View view) {
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    public void SendPush(View view) {
        // WRONG WAY TO SEND PUSH - INSECURE!
        // Used for pushing. Moet nog de library importen, maar ik had geen tijd meer...

        final EditText txtUrl = new EditText(this);

        // Set the default text to a link of the Queen
        txtUrl.setHint("Your friends name");

        new AlertDialog.Builder(this)
                .setTitle("Name")
                .setMessage("Paste in the username of your friend!")
                .setView(txtUrl)
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                        //ParseUser currentUser = ParseUser.getCurrentUser();
                        String message = installation.get("username") + " says Hi!";
                        String friendName = txtUrl.getText().toString();
                        Log.e("", "tried to push to |" + friendName + "|");
                        Log.e("", "pushed from:  |" + installation.get("username") + "|");
                        ParsePush push = new ParsePush();
                        pushQuery.whereEqualTo("username", friendName);
                        push.setQuery(pushQuery); // Set our Installation query
                        push.setMessage(message);
                        push.sendInBackground();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public void RegisterPush(View View) {
        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "2fddNMsnNgxufI2tqFNzAo4EXPDXkQCMK6ObmSEn", "QSJ22yZ3akyvnkYUg4MOqadkgQvTOZPOVV8t156c");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        //Log.e("", "tried to set Parse data");
        Log.e("", "tried to set Parse data");

        //ParseUser.enableAutomaticUser();
        //ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
    }

}
