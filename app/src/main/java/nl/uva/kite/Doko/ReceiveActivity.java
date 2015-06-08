package nl.uva.kite.Doko;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Shahrukh on 8-6-2015.
 */
public class ReceiveActivity extends Activity{
    private static ReceiveActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState){
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
    }

    public static ReceiveActivity instance() {
        return instance;
    }

    public void setTextView(String friendName){
        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.myLayout);
        View templateView = getLayoutInflater().inflate(R.layout.template, insertPoint, false);

        TextView name = (TextView) templateView.findViewById(R.id.textView1);
        name.setText(friendName);
        insertPoint.addView(name);
    }

    @Override
    public void onStop() {
        super.onStop();
        instance = null;
    }
}
