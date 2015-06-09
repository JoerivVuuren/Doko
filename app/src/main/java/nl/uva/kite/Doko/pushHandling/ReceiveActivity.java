package nl.uva.kite.Doko.pushHandling;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.uva.kite.Doko.R;

public class ReceiveActivity extends Activity{
    private static ReceiveActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState){
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String friendName = extras.getString("friendName");
            setTextView(friendName);
        }
        else {
            Log.e("", "extras is helaas null");
        }
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
}
