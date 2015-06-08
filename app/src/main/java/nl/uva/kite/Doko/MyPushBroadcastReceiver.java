package nl.uva.kite.Doko;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPushBroadcastReceiver extends ParsePushBroadcastReceiver{

    public static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        JSONObject data = getDataFromIntent(intent);

        try {
            String message = data.getString("message");
            String friendName = data.getString("friendName");
            Log.e("", "just received a friend request with message: " + message + " and friendName: " + friendName);
            //ReceiveActivity instance = ReceiveActivity.instance();
            Intent i = new Intent(context, ReceiveActivity.class);
            i.putExtra("friendName", friendName);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            /*if(instance != null){
                instance.setTextView(friendName);
            }
            else{
                Log.e("", "Something went wrong qoute: Joeri");
            }*/
        } catch(JSONException e) {
            Log.e("", "JSONERROR");
        }

    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {


        JSONObject data = getDataFromIntent(intent);
        // Do something with the data. To create a notification do:
        Intent resultIntent = new Intent(context, ReceiveActivity.class);
        try {
            String friendName = data.getString("friendName");
            resultIntent.putExtra("friendName", friendName);
        } catch(JSONException e) {
            Log.e("", "JSONERROR");
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
        stackBuilder.addParentStack(ReceiveActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Title");
        builder.setContentText("Text");
        builder.setSmallIcon(R.drawable.banaan);
        builder.setAutoCancel(true);
        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify("MyTag", 0, builder.build());

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