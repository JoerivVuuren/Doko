package nl.uva.kite.Doko;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class Login extends Activity implements OnClickListener {
    SecurePreferences saved_preferences;

    private static boolean loggedIn = false;
    private static String loginName = "";
    private static String loginPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saved_preferences = new SecurePreferences(this, "Doko-preferences", "DokoFO2opPOA@#F=/00000000", true);

        setContentView(R.layout.login);

        /* do auto-login */
        if (saved_preferences.getString("autologin") != null &&
                saved_preferences.getString("autologin").equals("1") &&
                saved_preferences.getString("username") != null &&
                saved_preferences.getString("password") != null) {

            attemptLogin(saved_preferences.getString("username"),
                         saved_preferences.getString("password"));
        }
        else if (saved_preferences.getString("autologin") != null &&
                     saved_preferences.getString("autologin").equals("0")) {
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

                saved_preferences.put("autologin", (autoLogin ? "1" : "0"));
                saved_preferences.put("username", user);
                saved_preferences.put("password", pass);

                attemptLogin(user, pass);
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
    public void attemptLogin(String user, String pass) {
        if (user.length() < 1 && pass.length() < 1)
            return;

        setLoginName(user);
        setLoginPass(pass);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", user));
        params.add(new BasicNameValuePair("password", pass));
        JSONRetrieve jr = new JSONRetrieve(this, params, OnJSONCompleted.LOGIN);
        jr.execute("http://intotheblu.nl/login.php");
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