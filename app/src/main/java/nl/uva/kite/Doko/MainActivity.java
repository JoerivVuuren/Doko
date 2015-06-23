package nl.uva.kite.Doko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.ContentBody;
import com.parse.entity.mime.content.FileBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.Fragments.TabWrapper;
import nl.uva.kite.Doko.Fragments.Contacts;
import nl.uva.kite.Doko.Fragments.Tabs.Tab2;
import nl.uva.kite.Doko.Fragments.Tabs.Tab3;


public class MainActivity extends AppCompatActivity {

    public static MainActivity mContext;
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
    private Uri fileUri;
    private int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpAndDisplayMainScreen();
    }

    /* creates and sets up the Main screen with tabs, nav drawer */
    public void setUpAndDisplayMainScreen() {
        MainActivity.mContext = this;
        setContentView(R.layout.activity_homescreen);

        /* update groups list */
        Groups.get_grouplist(OnJSONCompleted.GROUPLISTUPDATE, this);

        /* activate last selected group_id */
        if (Login.securePreferences.getString("group_id") != null &&
            Login.securePreferences.getString("group_name") != null) {
            int group_id = Integer.parseInt(Login.securePreferences.getString("group_id"));
            String group_name = Login.securePreferences.getString("group_name");
            if (group_id > -1)
                Groups.activateGroup(group_id, group_name);
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
//        change title to current group name if there is an active group
        if (Groups.current_group_name != null)
            getSupportActionBar().setTitle(Groups.current_group_name);



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
                        Snackbar.make(mContentFrame, "Home", Snackbar.LENGTH_SHORT).show();
                        TabWrapper tabWrapper = new TabWrapper();
                        fragmentTransaction.replace(R.id.fragment_container, tabWrapper);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 0;
                        return true;
                    // List of our dear contacts
                    case R.id.navigation_item_2:
                        Snackbar.make(mContentFrame, "Groups", Snackbar.LENGTH_SHORT).show();
                        Groups groupsFragment = new Groups();
                        //Tab4 tab4 = new Tab4();
                        fragmentTransaction.replace(R.id.fragment_container, groupsFragment);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.navigation_item_3:
                        Snackbar.make(mContentFrame, "Contacts", Snackbar.LENGTH_SHORT).show();
                        Friends friendsFragment = new Friends();
                        //Tab4 tab4 = new Tab4();
                        fragmentTransaction.replace(R.id.fragment_container, friendsFragment);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.navigation_item_4:
                        Snackbar.make(mContentFrame, "Settings", Snackbar.LENGTH_LONG).show();
                        Settings settingsFragment = new Settings();
                        fragmentTransaction.replace(R.id.fragment_container, settingsFragment);
                        fragmentTransaction.commit();
                        mCurrentSelectedPosition = 3;
                        return true;
                    default:
                        return true;
                }
            }
        });
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationIcon(R.drawable.ic_action);

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
        //        getSupportActionBar().setIcon(R.drawable.ic_action);
        //        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        String menuFragment = getIntent().getStringExtra("Requests");

        //android.support.v4.app.FragmentManager fragmentmanager = getSupportFragmentManager();
        //android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
        if (menuFragment != null) {
            if (menuFragment.equals("game") || menuFragment.equals("debt") || menuFragment.equals("credit")) {
                TabWrapper tabWrapper1 = new TabWrapper();
                fragmentTransaction.replace(R.id.fragment_container, tabWrapper1);
            } else if (menuFragment.equals("friend")) {
                Friends friends = new Friends();
                fragmentTransaction.replace(R.id.fragment_container, friends);
            } else if (menuFragment.equals("addMember")) {
                Groups groups = new Groups();
                fragmentTransaction.replace(R.id.fragment_container, groups);
            }
        }
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

    /* opens the contacts fragment
    public void OpenContacts(View view) {
        Contacts contacts = new Contacts();
        this.getFragmentManager().beginTransaction()
        .replace(R.id.contentFragment, contacts, null).addToBackStack(null).commit();
    } */


    /* opens the tic tac toe game */
    public void OpenTicTacToe(View view) {
        Intent intent = new Intent(this, TicTacToe.class);
        startActivity(intent);
    }

    /* logs out the current user */
    public void LogOutUser(View view) {
        Login.logOut(view.getContext());
    }

    public void AddGroupPrompt(final View view) {
        final EditText txt = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Create a group")
                .setMessage("Please enter the desired group name (limited to 50 characters).")
                .setView(txt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String groupName = txt.getText().toString();
                        if (groupName.length() < 1)
                            return;

                        Groups.create(groupName, view.getContext());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
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

                        if (friendName.length() < 1)
                            return;

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

    public void SendGameRequest(final View view) {
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
                            String login = Login.getLoginName();
                            String password = Login.getPassword();
                            // Add game request to database
                            List<NameValuePair> params = new ArrayList<>();
                            params.add(new BasicNameValuePair("username",login ));
                            params.add(new BasicNameValuePair("password", password));
                            params.add(new BasicNameValuePair("friend", friendName));
                            params.add(new BasicNameValuePair("group_id", Integer.toString(Groups.current_group_id)));
                            JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.NONE);
                            jr.execute("http://intotheblu.nl/game_request_add.php");

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

    public void AddCredit(final View view) {
        LinearLayout alertLayout= new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText debitorurl = new EditText(this);
        final EditText debturl = new EditText(this);
        final EditText reasonurl = new EditText(this);
        debitorurl.setHint("The name of your debitor");
        debturl.setHint("The amount of debt");
        reasonurl.setHint("The reason of this credit");

        alertLayout.addView(debitorurl);
        alertLayout.addView(debturl);
        alertLayout.addView(reasonurl);

        if(Groups.current_group_id == -1) {
            Toast.makeText(view.getContext(), "please choose a group first!", Toast.LENGTH_LONG).show();
            return;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Add Credit");
        alert.setMessage("Please enter data of your credit action");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String debitor = debitorurl.getText().toString().trim();
                String debt = debturl.getText().toString().trim();
                String reason = reasonurl.getText().toString().trim();
                int groupID = 3;
                if (debitor.length() < 1 || debt.length() < 1 || reason.length() < 1)
                    return;

                try {
                    String login = Login.getLoginName();
                    String password = Login.getPassword();
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("username", login));
                    params.add(new BasicNameValuePair("password", password));
                    params.add(new BasicNameValuePair("friend", debitor));
                    params.add(new BasicNameValuePair("debt", debt));
                    params.add(new BasicNameValuePair("group_id", Integer.toString(Groups.current_group_id)));
                    params.add(new BasicNameValuePair("origin", reason));
                    JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.NONE);
                    jr.execute("http://intotheblu.nl/credit_request_add.php");
                    //AddDebt(debt, Login.getLoginName(), debitor, reason, groupID, view.getContext());
                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", "You have just received debt from " + Login.getLoginName() + "!");
                    jsonObject.put("friendName", Login.getLoginName());
                    jsonObject.put("class", "addCredit");
                    ParsePush push = new ParsePush();
                    pushQuery.whereEqualTo("username", debitor);
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setData(jsonObject);
                    push.sendInBackground();


                } catch (JSONException e) {
                    Log.e("", "failed JSON");
                }
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    public void AddDebit(final View view) {
        LinearLayout alertLayout= new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText creditorurl = new EditText(this);
        final EditText debturl = new EditText(this);
        final EditText reasonurl = new EditText(this);
        creditorurl.setHint("The name of the creditor");
        debturl.setHint("The amount of debt");
        reasonurl.setHint("The reason of this debit");

        alertLayout.addView(creditorurl);
        alertLayout.addView(debturl);
        alertLayout.addView(reasonurl);

        if(Groups.current_group_id == -1) {
            Toast.makeText(view.getContext(), "please choose a group first!", Toast.LENGTH_LONG).show();
            return;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Add Debit");
        alert.setMessage("Please enter data of your debit action");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String creditor = creditorurl.getText().toString().trim();
                String debt = debturl.getText().toString().trim();
                String reason = reasonurl.getText().toString().trim();
                if (creditor.length() < 1 || debt.length() < 1 || reason.length() < 1)
                    return;

                try {
                    String login = Login.getLoginName();
                    String password = Login.getPassword();
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("username", login));
                    params.add(new BasicNameValuePair("password", password));
                    params.add(new BasicNameValuePair("friend", creditor));
                    params.add(new BasicNameValuePair("debt", debt));
                    params.add(new BasicNameValuePair("group_id", Integer.toString(Groups.current_group_id)));
                    params.add(new BasicNameValuePair("origin", reason));
                    JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.NONE);
                    jr.execute("http://intotheblu.nl/debit_request_add.php");
                    //AddDebt(debt, creditor, Login.getLoginName(), reason, groupID, view.getContext());
                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", "You have just received credit from " + Login.getLoginName() + "!");
                    jsonObject.put("friendName", Login.getLoginName());
                    jsonObject.put("class", "addDebit");
                    ParsePush push = new ParsePush();
                    pushQuery.whereEqualTo("username", creditor);
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setData(jsonObject);
                    push.sendInBackground();


                } catch (JSONException e) {
                    Log.e("", "failed JSON");
                }
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    public static void AddDebt(String debt, String creditor, String debitor, String reason, int groupID, Context ctext, String debtType) {
        if (!Login.isLoggedIn() || creditor.equals(debitor))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("creditor", creditor));
        params.add(new BasicNameValuePair("debitor", debitor));
        params.add(new BasicNameValuePair("group_id", "" + groupID));
        params.add(new BasicNameValuePair("origin", reason));
        params.add(new BasicNameValuePair("debt", debt));
        params.add(new BasicNameValuePair("type", debtType));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.DEBTADD);
        jr.execute("http://intotheblu.nl/debt_add.php");
    }

    public void RegisterPush(View View) {
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
    }

    public void adduser(final View view) {
        if (Groups.current_group_id == -1)
            return;

        LinearLayout alertLayout= new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText memberurl = new EditText(this);

        memberurl.setHint("The name of the member to add");
        alertLayout.addView(memberurl);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Add Members");
        alert.setMessage("Please enter the username of the members you want to add");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String member = memberurl.getText().toString().trim();

                if (member.length() < 1)
                    return;

                try {
                    Groups.add_request(member, Groups.current_group_id, Groups.current_group_name, view.getContext());
                    //Groups.adduser(member, Groups.current_group_id, view.getContext());
                    //AddDebt(debt, creditor, Login.getLoginName(), reason, groupID, view.getContext());
                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", "You have just received a group invite from " + Login.getLoginName() + "!");
                    jsonObject.put("friendName", Login.getLoginName());
                    jsonObject.put("class", "addMember");
                    ParsePush push = new ParsePush();
                    pushQuery.whereEqualTo("username", member);
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setData(jsonObject);
                    push.sendInBackground();


                } catch (JSONException e) {
                    Log.e("", "failed JSON");
                }
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }

    public void onClickChooseImage(View view) {
        Intent intent = new Intent(this, UploadImage.class);
        startActivity(intent);
    }

}
