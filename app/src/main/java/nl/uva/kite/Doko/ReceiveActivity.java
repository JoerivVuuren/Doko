package nl.uva.kite.Doko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shahrukh on 8-6-2015.
 */
public class ReceiveActivity extends Activity{
    private static ReceiveActivity instance;
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    private TextView myText = null;

    @Override
    public void onCreate(Bundle savedInstanceState){

        /*String friendName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                friendName= null;
            } else {
                friendName= extras.getString("friendName");
            }
        } else {
            friendName= (String) savedInstanceState.getSerializable("friendName");
        }

        Log.e("", "just received a friend request with no message and friendName: " + friendName);
        setTextView(friendName);*/


        /*Intent i = getIntent();
        JSONObject data = getDataFromIntent(i);
        try {
            String message = data.getString("message");
            String friendName = data.getString("friendName");
        }
        catch(JSONException e) {
            Log.e("", "JSONERROR");
        }*/

        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        LinearLayout lView = new LinearLayout(this);

        myText = new TextView(this);
        myText.setText("You just received a friend request from Matthijs!");

        lView.addView(myText);

        setContentView(lView);
    }

    public static ReceiveActivity instance() {
        return instance;
    }

    public void setTextView(String friendName){
        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.myLayout);
        View templateView = getLayoutInflater().inflate(R.layout.template, insertPoint, false);

        TextView name = (TextView) templateView.findViewById(R.id.textView1);
        name.setText(friendName);
        insertPoint.addView(templateView);
    }

    @Override
    public void onStop() {
        super.onStop();
        instance = null;
    }


    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        if(intent.getExtras() == null) {
            Log.e("", "looooooooooooooooooooooooooooooooool");
        }
        try {
            data = new JSONObject(intent.getExtras().getString("com.parse.Data"));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }
}
