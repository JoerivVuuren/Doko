package nl.uva.kite.Doko;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class Login extends Activity implements OnClickListener {
    public static Context mContext;
    public static String intentExtra;
    public static String intentExtraGroupID;
    public static String intentExtraGroupName;
    public static SecurePreferences securePreferences;
    private static boolean loggedIn = false;
    private static String loginName = "";
    private static String loginPass = "";
    public static String picName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        intentExtra = getIntent().getStringExtra("Requests");
        intentExtraGroupID = getIntent().getStringExtra("groupID");
        intentExtraGroupName = getIntent().getStringExtra("groupName");
        securePreferences = new SecurePreferences(this, "Doko-preferences", "DokoFO2opPOA@#F=/00000000", true);

        setContentView(R.layout.login);

        /* fill in username field */
        if (securePreferences.getString("username") != null) {
            ((EditText)findViewById(R.id.username)).setText(securePreferences.getString("username"));
            findViewById(R.id.password).requestFocus();
        }

        /* do auto-login */
        if (securePreferences.getString("autologin") != null &&
                securePreferences.getString("autologin").equals("1") &&
                securePreferences.getString("username") != null &&
                securePreferences.getString("password") != null) {

            attemptLogin(this, securePreferences.getString("username"),
                    securePreferences.getString("password"));
        }
        else if (securePreferences.getString("autologin") != null &&
                securePreferences.getString("autologin").equals("0")) {
            ((CheckBox)findViewById(R.id.autoLoginCheckBox)).setChecked(false);
        }

        /* setup Button listeners */
        Button mSubmit = (Button)findViewById(R.id.login);
        Button mRegister = (Button)findViewById(R.id.register);
        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String user = ((EditText)findViewById(R.id.username)).getText().toString();
                String pass = ((EditText)findViewById(R.id.password)).getText().toString();
                boolean autoLogin = ((CheckBox)findViewById(R.id.autoLoginCheckBox)).isChecked();

                securePreferences.put("autologin", (autoLogin ? "1" : "0"));
                securePreferences.put("username", user);
                securePreferences.put("password", pass);

                attemptLogin(this, user, pass);
                break;

            case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    /* attempts login */
    public static void attemptLogin(Context ctext, String user, String pass) {
        if (user.length() < 1 && pass.length() < 1)
            return;

        setLoginName(user);
        setLoginPass(pass);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", user));
        params.add(new BasicNameValuePair("password", pass));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.LOGIN);
        jr.execute("http://intotheblu.nl/login.php");
    }

    /* logs the current user out */
    public static void logOut(Context ctext) {
        setLoggedIn(false);
        setLoginName("");
        setLoginPass("");
        securePreferences.put("autologin", "0");
        securePreferences.put("password", "");
        securePreferences.put("group_name", "");
        securePreferences.put("group_id", "-1");

        /* close current activity */
        Activity a = (Activity)ctext;
        a.finish();
    }

    public static boolean isLoggedIn() {
        return Login.loggedIn;
    }

    public static String getLoginName() {
        return Login.loginName;
    }

    public static String getPassword() {
        return Login.loginPass;
    }

    public static void setLoginName(String name) {
        loginName = name;
    }

    public static void setLoginPass(String pass) {
        loginPass = pass;
    }

    public static void setLoggedIn(boolean b) {
        loggedIn = b;
    }
}