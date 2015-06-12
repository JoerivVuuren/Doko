package nl.uva.kite.Doko.pushHandling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.JSONRetrieve;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;

public class ReceiveActivity extends Activity{

    public void handleClicks() {
        final ViewGroup group = (ViewGroup)findViewById(R.id.myLayout);
        // Loop through layout elements
        for(int i = 0; i < group.getChildCount(); i++) {
            final View v = group.getChildAt(i);
            Log.e("", "Entered Forloop");
            // Set Textview as clickable
            if((v instanceof TextView)) v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View arg0) {
                    Log.e("", "Registered click.");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ReceiveActivity.this);

                    alertDialogBuilder.setTitle("Add " + ((TextView) v).getText() + " to friends?");

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    List<NameValuePair> params = new ArrayList<>();
                                    params.add(new BasicNameValuePair("username", "Dav"));
                                    params.add(new BasicNameValuePair("password", "q"));
                                    params.add(new BasicNameValuePair("friend", ((TextView) v).getText().toString()));
                                    JSONRetrieve jr = new JSONRetrieve(arg0.getContext(), params, OnJSONCompleted.NONE);
                                    jr.execute("http://intotheblu.nl/friend_request_accept.php");
                                    v.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    v.setVisibility(View.GONE);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });
            else{
                Log.e("", "This is not a Textview!");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String friendName = extras.getString("friendName");
            setTextView(friendName);
            handleClicks();
        }
        else {
            Log.e("", "extras is helaas null");
        }
        handleClicks();
    }

    public void setTextView(String friendName){
        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.myLayout);
        /*View templateView = getLayoutInflater().inflate(R.layout.template, insertPoint, false);

        TextView name = (TextView) templateView.findViewById(R.id.textView1);
        name.setText(friendName);*/
        final TextView newTextView = new TextView(this);
        newTextView.setText(friendName);
        insertPoint.addView(newTextView);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
