package nl.uva.kite.Doko;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

import nl.uva.kite.Doko.SlidingTab.SlidingTabLayout;
import nl.uva.kite.Doko.SlidingTab.ViewPagerAdapter;
import nl.uva.kite.Doko.Fragments.Contacts;


public class Homescreen extends ActionBarActivity {

    // sidebar
    String sideTitles[] = {"Messages","Contacts","Settings","Help","Log Out"};
    int sideIcons[] = {R.drawable.ic_mail,R.drawable.ic_shop, R.drawable.ic_settings,R.drawable.ic_help,R.drawable.ic_travel};
    String NAME = "Meisje met Kont";
    String EMAIL = "me@doko.net";
    int PROFILE = R.drawable.aka;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Start", "Me", "Group", "Games"};
    int Numboftabs = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Commentaar...
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

    public void SendFriendRequest(View view) {
        final EditText txtUrl = new EditText(this);

        // Set the default text to a link of the Queen
        txtUrl.setHint("Your friends name");

        new AlertDialog.Builder(this)
                .setTitle("Friend Request")
                .setMessage("Please type the username of your friend!")
                .setView(txtUrl)
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                        //ParseUser currentUser = ParseUser.getCurrentUser();
                        String friendName = txtUrl.getText().toString();
                        try {
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
