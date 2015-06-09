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

import nl.uva.kite.Doko.R;

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
            String friendName = data.getString("friendName");
            int requestID = (int) System.currentTimeMillis();
            Intent tryIntent = new Intent(context, ReceiveActivity.class);
            tryIntent.putExtra("friendName", friendName);
            PendingIntent pIntent = PendingIntent.getActivity(context, requestID, tryIntent, 0);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("Friend Request");
            builder.setContentText("You just received a friend request from " + friendName);
            builder.setSmallIcon(R.drawable.aka);
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