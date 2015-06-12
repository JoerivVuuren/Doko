package nl.uva.kite.Doko;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.Fragments.TabWrapper;
import nl.uva.kite.Doko.Fragments.Contacts;


public class MainActivity extends ActionBarActivity {
    // Declerations for stuff we will need later on
    NavigationView mNavigationView;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    FrameLayout mContentFrame;
    DrawerLayout mDrawerLayout;

    // Declaring Your View and Variables
    Toolbar toolbar;

    ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        // Load up a starting fragment in our fragment container
        android.support.v4.app.FragmentManager fragmentmanager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
        TabWrapper tabWrapper = new TabWrapper();
        fragmentTransaction.replace(R.id.fragment_container, tabWrapper);
        fragmentTransaction.commit();

//        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        // See which drawer item has been selected
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Fragment manager to switch fragments in our main activity
                android.support.v4.app.FragmentManager fragmentmanager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();

                // item in menu list has been selected, which fragment do we switch to?
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    // Current active group homepage
                    case R.id.navigation_item_1:
                        Snackbar.make(mContentFrame, "My Group", Snackbar.LENGTH_SHORT).show();
                        TabWrapper tabWrapper = new TabWrapper();
                        fragmentTransaction.replace(R.id.fragment_container, tabWrapper);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 0;
                        return true;
                    // List of our beloved contacts
                    case R.id.navigation_item_2:
                        Snackbar.make(mContentFrame, "Contacts", Snackbar.LENGTH_SHORT).show();
                        SelectFriend selFriend = new SelectFriend();
                        //Tab4 tab4 = new Tab4();
                        fragmentTransaction.replace(R.id.fragment_container, selFriend);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 1;
                        return true;
                    default:
                        return true;
                }
            }
        });
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer) {
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_float_new);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
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

    /* opens the mygroups screen activity */
    public void OpenMyGroups(View view) {
        Groups.get_grouplist(OnJSONCompleted.GROUPLISTOPEN, view.getContext());
    }

    /* opens the friends screen activity */
    public void OpenContacts(View view) {
        Contacts contacts = new Contacts();
        this.getFragmentManager().beginTransaction()
        .replace(R.id.contentFragment, contacts, null).addToBackStack(null).commit();
    }

    /* opens the tic tac toe game */
    public void OpenTicTacToe(View view) {
        Intent intent = new Intent(this, TicTacToe.class);
        startActivity(intent);
    }

    public void OpenPlayAgainstFriend(View view) {
        Intent intent = new Intent(this, SelectFriend.class);
        startActivity(intent);
    }


    /* json login testje */
    public void JSONTest(View view) {
        Login.loginName = "Dav";
        Login.loginPass = "q";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "Dav"));
        params.add(new BasicNameValuePair("password", "q"));
        JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.LOGIN);
        jr.execute("http://intotheblu.nl/login.php");
    }

    public void SendFriendRequest(final View view) {
        final EditText txtUrl = new EditText(this);


        // Set the default text to a link of the Queen
        txtUrl.setHint("Your friend's name");

        new AlertDialog.Builder(this)
                .setTitle("Friend Request")
                .setMessage("Please enter the username of your friend!")
                .setView(txtUrl)
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                        //ParseUser currentUser = ParseUser.getCurrentUser();
                        String friendName = txtUrl.getText().toString();
                        try {
                            String login = Login.getLoginName();
                            String password = Login.getPassword();
                            // Add friend request to database
                            List<NameValuePair> params = new ArrayList<>();
                            params.add(new BasicNameValuePair("username",login ));
                            params.add(new BasicNameValuePair("password", password));
                            params.add(new BasicNameValuePair("friend", friendName));
                            JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.NONE);
                            jr.execute("http://intotheblu.nl/friend_request_add.php");

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("message", "You just reveived a new Friend request from " + installation.get("username") + "!");
                            jsonObject.put("friendName", installation.get("username"));
                            jsonObject.put("class", "friendrequest");
                            ParsePush push = new ParsePush();
                            pushQuery.whereEqualTo("username", friendName);
                            push.setQuery(pushQuery); // Set our Installation query
                            push.setData(jsonObject);
                            push.sendInBackground();


                        } catch (JSONException e) {
                            Log.e("", "failed JSON");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public void SendGameRequest(View view) {
        final EditText txtUrl = new EditText(this);

        // Set the default text to a link of the Queen
        txtUrl.setHint("Your friends name");

        new AlertDialog.Builder(this)
                .setTitle("Game request")
                .setMessage("Please type the username of your friend!")
                .setView(txtUrl)
                .setPositiveButton("Invite to Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                        //ParseUser currentUser = ParseUser.getCurrentUser();

                        String friendName = txtUrl.getText().toString();
                        if (friendName.length() < 1)
                            return;

                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("message", "You just reveived a new Game request from " + installation.get("username") + "!");
                            jsonObject.put("friendName", installation.get("username"));
                            jsonObject.put("class", "gamerequest");
                            ParsePush push = new ParsePush();
                            pushQuery.whereEqualTo("username", friendName);
                            push.setQuery(pushQuery); // Set our Installation query
                            push.setData(jsonObject);
                            push.sendInBackground();
                        } catch (JSONException e) {
                            Log.e("", "failed JSON");
                        }
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
