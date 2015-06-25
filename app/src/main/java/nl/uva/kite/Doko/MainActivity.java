package nl.uva.kite.Doko;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.List;

import nl.uva.kite.Doko.Fragments.TabWrapper;
import nl.uva.kite.Doko.Fragments.Tabs.Tab4;


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
        if (MainActivity.mContext != null)
            /* close current main activity */
            MainActivity.mContext.finish();

        MainActivity.mContext = this;
        setContentView(R.layout.activity_homescreen);
        /* change drawer username */
        TextView drawerUsername = (TextView) findViewById(R.id.drawer_username);
        drawerUsername.setText(Login.getLoginName());

        /* update groups list */
        Groups.get_grouplist(OnJSONCompleted.GROUPLISTUPDATE, this);

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
        if (getIntent().getStringExtra("groupID") != null && getIntent().getStringExtra("groupName") != null) {
            int newGroupID = Integer.parseInt(getIntent().getStringExtra("groupID"));
            String newGroupName = getIntent().getStringExtra("groupName");
            if (newGroupID > -1)
                Groups.activateGroup(newGroupID, newGroupName);
        } if (getIntent().getStringExtra("gameturn") != null && getIntent().getStringExtra("gameturn").equals("begon")) {
            Tab4.get_game_data(OnJSONCompleted.LOADGAME, TicTacToe.current_game_id, TicTacToe.myContext);
        }

        /* activate last selected group_id */
        if (Login.securePreferences.getString("group_id") != null &&
            Login.securePreferences.getString("group_name") != null) {
            int group_id = Integer.parseInt(Login.securePreferences.getString("group_id"));
            String group_name = Login.securePreferences.getString("group_name");
            if (group_id > -1)
                Groups.activateGroup(group_id, group_name);
        }

        //        change title to current group name if there is an active group
        Typeface titleTF = Typeface.createFromAsset(getAssets(), "fonts/Crescent-Regular.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("Doko");
        if (Groups.current_group_name != null){
            SS = new SpannableStringBuilder(Groups.current_group_name);
        }
        SS.setSpan (new CustomTypefaceSpan("", titleTF), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);

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

    /* places EURO sign in front of double */
    public static String doubleToEuro(double d) {
        final String EURO = "\u20AC";
        String prefix = "";
        if (d < 0) {
            /* move minus sign in front of euro sign */
            prefix = "- ";
            d = -d;
        }
        return String.format(prefix + EURO + " %.2f", d);
    }

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
                            jsonObject.put("message", "You just received a new Friend request from " + installation.get("username") + "!");
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
        LinearLayout alertLayout= new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);

        final Spinner spin = new Spinner(this);
        final EditText wageurl = new EditText(this);

        // Load current group members, excluding yourself
        String[] allMembers = Groups.getGroupMemberList();
        ArrayList<String> members = new ArrayList<>();
        Collections.addAll(members, allMembers);
        members.remove(Login.getLoginName());

        // Create drop down menu
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, members);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter2);
        ViewGroup.LayoutParams spinParams = view.getLayoutParams();
        spinParams.width = 450;
        spin.setLayoutParams(spinParams);

        // Set the default text to a link of the Queen
        wageurl.setHint("The wager of your game");
        wageurl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        wageurl.setKeyListener(DigitsKeyListener.getInstance("01234567890."));
        alertLayout.addView(spin);
        alertLayout.addView(wageurl);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Play for wager!");
        alert.setMessage("Please select the name of your opponent");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    //ParseUser currentUser = ParseUser.getCurrentUser();

                    String friendName = spin.getSelectedItem().toString();
                    String wager = wageurl.getText().toString();
                    if (friendName.length() < 1 || friendName.equals(Login.getLoginName()))
                        return;
                    try {
                        if (Double.parseDouble(wager) < 0) {
                            Toast.makeText(view.getContext(), "Enter a positive number!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    catch (NumberFormatException e) {
                        Toast.makeText(view.getContext(), "Invalid number!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        String login = Login.getLoginName();
                        String password = Login.getPassword();
                        // Add game request to database
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("username", login));
                        params.add(new BasicNameValuePair("password", password));
                        params.add(new BasicNameValuePair("friend", friendName));
                        params.add(new BasicNameValuePair("wager", wager));
                        params.add(new BasicNameValuePair("group_id", Integer.toString(Groups.current_group_id)));
                        JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.NONE);
                        jr.execute("http://intotheblu.nl/game_request_add.php");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("message", "You just received a new Game request from " + installation.get("username") + "!");
                        jsonObject.put("friendName", installation.get("username"));
                        jsonObject.put("class", "gamerequest");
                        jsonObject.put("groupID", Integer.toString(Groups.current_group_id));
                        jsonObject.put("groupName", Groups.current_group_name);
                        ParsePush push = new ParsePush();
                        pushQuery.whereEqualTo("username", friendName);
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

    public void AddCredit(final View view) {
        LinearLayout alertLayout = new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);

        final Spinner spin = new Spinner(this);
        final EditText debturl = new EditText(this);
        final EditText reasonurl = new EditText(this);

        // Load current group members, excluding yourself
        String[] allMembers = Groups.getGroupMemberList();
        ArrayList<String> members = new ArrayList<>();
        Collections.addAll(members, allMembers);
        members.remove(Login.getLoginName());

        // Create drop down menu
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, members);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter2);
        ViewGroup.LayoutParams spinParams = view.getLayoutParams();
        spinParams.width = 450;
        spin.setLayoutParams(spinParams);

        debturl.setHint("The amount of debt");
        debturl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        debturl.setKeyListener(DigitsKeyListener.getInstance("01234567890."));
        reasonurl.setHint("The reason for this credit");

        alertLayout.addView(spin);
        alertLayout.addView(debturl);
        alertLayout.addView(reasonurl);

        if (Groups.current_group_id == -1) {
            Toast.makeText(view.getContext(), "Please choose a group first!", Toast.LENGTH_LONG).show();
            return;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Add Credit");
        alert.setMessage("Please select the name of your debitor:");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String debitor = spin.getSelectedItem().toString().trim();
                String debt = debturl.getText().toString().trim();
                String reason = reasonurl.getText().toString().trim();
                if (debitor.length() < 1 || debt.length() < 1)
                    return;
                if (debitor.equals(Login.getLoginName())) {
                    Toast.makeText(view.getContext(), "You cannot create money out of thin air!", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    if (Double.parseDouble(debt) < 0) {
                        Toast.makeText(view.getContext(), "Enter a positive number!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(view.getContext(), "Invalid number!", Toast.LENGTH_LONG).show();
                    return;
                }

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
                    params.add(new BasicNameValuePair("type", "debit"));
                    JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.DEBTADD);
                    jr.execute("http://intotheblu.nl/debt_request_add.php");

                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", "You have just received a debit request from " + Login.getLoginName() +
                            " for " + reason + " in group " + Groups.current_group_name + "!");
                    jsonObject.put("friendName", Login.getLoginName());
                    jsonObject.put("class", "addDebit");
                    jsonObject.put("groupID", Integer.toString(Groups.current_group_id));
                    jsonObject.put("groupName", Groups.current_group_name);
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
        LinearLayout alertLayout = new LinearLayout(this);
        alertLayout.setOrientation(LinearLayout.VERTICAL);

        final Spinner spin = new Spinner(this);
        final EditText debturl = new EditText(this);
        final EditText reasonurl = new EditText(this);

        // Load current group members, excluding yourself
        String[] allMembers = Groups.getGroupMemberList();
        ArrayList<String> members = new ArrayList<>();
        Collections.addAll(members, allMembers);
        members.remove(Login.getLoginName());

        // Create drop down menu
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, members);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter2);
        ViewGroup.LayoutParams spinParams = view.getLayoutParams();
        spinParams.width = 450;
        spin.setLayoutParams(spinParams);

        debturl.setHint("The amount of debt");
        debturl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        debturl.setKeyListener(DigitsKeyListener.getInstance("01234567890."));
        reasonurl.setHint("The reason for this debit");

        alertLayout.addView(spin);
        alertLayout.addView(debturl);
        alertLayout.addView(reasonurl);

        if (Groups.current_group_id == -1) {
            Toast.makeText(view.getContext(), "please choose a group first!", Toast.LENGTH_LONG).show();
            return;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        alert.setTitle("Add Debit");
        alert.setMessage("Please enter data of your debit action");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String creditor = spin.getSelectedItem().toString().trim();
                String debt = debturl.getText().toString().trim();
                String reason = reasonurl.getText().toString().trim();

                if (creditor.length() < 1 || debt.length() < 1)
                    return;
                if (creditor.equals(Login.getLoginName())) {
                    Toast.makeText(view.getContext(), "You cannot create money out of thin air!", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    if (Double.parseDouble(debt) < 0) {
                        Toast.makeText(view.getContext(), "Enter a positive number!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(view.getContext(), "Invalid number!", Toast.LENGTH_LONG).show();
                    return;
                }

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
                    params.add(new BasicNameValuePair("type", "credit"));
                    JSONRetrieve jr = new JSONRetrieve(view.getContext(), params, OnJSONCompleted.DEBTADD);
                    jr.execute("http://intotheblu.nl/debt_request_add.php");

                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", "You have just received a credit request from " + Login.getLoginName() +
                            " for " + reason + " in group " + Groups.current_group_name + "!");
                    jsonObject.put("friendName", Login.getLoginName());
                    jsonObject.put("class", "addCredit");
                    jsonObject.put("groupID", Integer.toString(Groups.current_group_id));
                    jsonObject.put("groupName", Groups.current_group_name);
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

        LinearLayout alertLayout = new LinearLayout(this);
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
