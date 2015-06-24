package nl.uva.kite.Doko.pushHandling;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.TicTacToe;

public class MyPushBroadcastReceiver extends ParsePushBroadcastReceiver{

    public static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {


        JSONObject data = getDataFromIntent(intent);
        // Do something with the data. To create a notification do:
        try {
            // Data used for the notification itself
            String friendName = data.getString("friendName");
            String message = data.getString("message");
            String classType = data.getString("class");
            Log.e("", "onpushreceive classtype is: " + classType);

            Intent tryIntent;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            if(classType.equals("friendrequest")) {
                builder.setContentTitle("Friend Request");
                tryIntent = new Intent(context, MainActivity.class);
                tryIntent.putExtra("Requests", "friend");
            }
            else if(classType.equals("gamerequest")) {
                builder.setContentTitle("Game Request");
                tryIntent = new Intent(context, MainActivity.class);
                tryIntent.putExtra("Requests", "game");
            }
            else if(classType.equals("addDebit")){
                builder.setContentTitle("Debt request");
                tryIntent = new Intent(context, MainActivity.class);
                tryIntent.putExtra("Requests", "debt");
            }
            else if(classType.equals("addCredit")){
                builder.setContentTitle("Credit request");
                tryIntent = new Intent(context, MainActivity.class);
                tryIntent.putExtra("Requests", "credit");
            }
            else if(classType.equals("addMember")) {
                builder.setContentTitle("Group request");
                tryIntent = new Intent(context, MainActivity.class);
                tryIntent.putExtra("Requests", "addMember");
            }
            else if (classType.equals("gameturn")) {
                tryIntent = new Intent(context, TicTacToe.class);
                tryIntent.putExtra("gameturn", "begon");
            }
            else {
                Log.e("", "onpushreceive in de else gekomen helaas...");
                tryIntent = new Intent(context, ReceiveActivity.class);
            }
            // Put data that needs to be read once notification is clicked.
            tryIntent.putExtra("friendName", friendName);

            int requestID = (int) System.currentTimeMillis();
            PendingIntent pIntent = PendingIntent.getActivity(context, requestID, tryIntent, 0);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            builder.setContentText(message);
            builder.setSmallIcon(R.drawable.ic_stat_doko_d);
            builder.setAutoCancel(true);
            builder.setContentIntent(pIntent);

            notificationManager.notify("MyTag", 0, builder.build());
        } catch(JSONException e) {
            Log.e("", "JSONERROR");
        }
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }
}