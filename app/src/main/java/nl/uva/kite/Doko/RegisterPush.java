package nl.uva.kite.Doko;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

public class RegisterPush extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "2fddNMsnNgxufI2tqFNzAo4EXPDXkQCMK6ObmSEn", "QSJ22yZ3akyvnkYUg4MOqadkgQvTOZPOVV8t156c");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //String devicetoken = ParseInstallation.getCurrentInstallation().get("deviceToken").toString();
        Log.e("", "i came in registration"/* + devicetoken*/);
    }
}
