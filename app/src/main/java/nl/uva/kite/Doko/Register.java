// source: http://www.mybringback.com/android-sdk/12924/android-tutorial-using-remote-databases-php-and-mysql-part-1/

package nl.uva.kite.Doko;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Register extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        /* setup Button listener */
        Button mRegister = (Button)findViewById(R.id.register_submit);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_submit:
                String user = ((EditText)findViewById(R.id.register_user)).getText().toString();
                String pass = ((EditText)findViewById(R.id.register_pass)).getText().toString();

                Login.securePreferences.put("username", user);
                Login.securePreferences.put("password", pass);

                attemptRegister(v.getContext(), user, pass);
                break;

            case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    /* attempts registration */
    public static void attemptRegister(Context ctext, String user, String pass) {
        if (user.length() < 1 && pass.length() < 1)
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", user));
        params.add(new BasicNameValuePair("password", pass));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.REGISTER);
        jr.execute("http://intotheblu.nl/register.php");
    }

}