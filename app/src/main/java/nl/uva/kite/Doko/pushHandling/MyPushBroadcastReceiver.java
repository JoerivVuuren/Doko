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

import nl.uva.kite.Doko.Fragments.Tabs.Tab4;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.OnJSONCompleted;
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
            //String groupID, groupName;
            // Data used for the notification itself
            String friendName = data.getString("friendName");
            String message = data.getString("message");
            String classType = data.getString("class");

            Log.e("", "onpushreceive classtype is: " + classType);

            Intent tryIntent;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            if (!Login.isLoggedIn()) {
                tryIntent = new Intent(context, Login.class);
            }
            else {
                tryIntent = new Intent(context, MainActivity.class);
            }

            if(!data.isNull("groupID") && !data.isNull("groupName")){
                tryIntent.putExtra("groupID", data.getString("groupID"));
                tryIntent.putExtra("groupName", data.getString("groupName"));
            }

            if(classType.equals("friendrequest")) {
                builder.setContentTitle("Friend Request");
                tryIntent.putExtra("Requests", "friend");
            }
            else if(classType.equals("gamerequest")) {
                builder.setContentTitle("Game Request");
                tryIntent.putExtra("Requests", "game");
            }
            else if(classType.equals("addDebit")){
                builder.setContentTitle("Debt request");
                tryIntent.putExtra("Requests", "debt");
            }
            else if(classType.equals("addCredit")){
                builder.setContentTitle("Credit request");
                tryIntent.putExtra("Requests", "credit");
            }
            else if(classType.equals("addMember")) {
                builder.setContentTitle("Group request");
                tryIntent.putExtra("Requests", "addMember");
            }
            else if (classType.equals("gameturn")) {
                if(TicTacToe.isActivityVisible() && TicTacToe.myContext != null) {
                    Log.e("", "isvisible is true");
                    Tab4.get_game_data(OnJSONCompleted.LOADGAME, TicTacToe.current_game_id, TicTacToe.myContext);
                } else {
                    Log.e("", "we came in the push receive and tried to pend tictactoe as intent");
                    builder.setContentTitle("Game turn");
                    tryIntent = new Intent(context, MainActivity.class);
                    tryIntent.putExtra("gameturn", "begon");
                }
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
            Log.e("", "No push data");
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