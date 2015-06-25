package nl.uva.kite.Doko;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends Activity implements OnClickListener {

    public static Context myContext;


    private static boolean activityVisible;
    public static String current_turn;
    public static String player1;
    public static String player2;
    public static String current_game_id;
    public static Button[] array_buttons = null;
    public static Button button, button1, button2, button3, button4, button5, button6, button7, button8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        myContext = this;

        activityVisible = true;

        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        array_buttons = new Button[] {button, button1, button2, button3, button4, button5, button6, button7, button8};

        String classtype = getIntent().getStringExtra("class");
        /*if(classtype != null && classtype.equals("GETUPDATE")) {
            get_game_data();
        }*/
        if(classtype != null && classtype.equals("UPDATEGAME")) {
            String[] fields = new String[9];
            fields[0] = getIntent().getStringExtra("veld0");
            fields[1] = getIntent().getStringExtra("veld1");
            fields[2] = getIntent().getStringExtra("veld2");
            fields[3] = getIntent().getStringExtra("veld3");
            fields[4] = getIntent().getStringExtra("veld4");
            fields[5] = getIntent().getStringExtra("veld5");
            fields[6] = getIntent().getStringExtra("veld6");
            fields[7] = getIntent().getStringExtra("veld7");
            fields[8] = getIntent().getStringExtra("veld8");
            player1 = getIntent().getStringExtra("player1");
            player2 = getIntent().getStringExtra("player2");
            update(getIntent().getStringExtra("game_id"), fields, getIntent().getStringExtra("turn"));
        }

        for (Button b : array_buttons) {
            b.setOnClickListener(this);
        }

    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    @Override
    public void onResume() {
        super.onResume();
        activityVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        activityVisible = false;
    }

    @Override
    public void onClick(View v) {
        buttonClicked(v);
    }

    private void buttonClicked(View v) {
        Log.e("", "button clicked by |" + current_turn + "|");
        if(!current_turn.contains(Login.getLoginName())) {
            return;
        }

        Button b = (Button) v;

        if (player1.contains(Login.getLoginName())) {
            b.setText("O");
        } else {
            b.setText("X");
        }

        b.setClickable(false);
        b.setBackgroundColor(Color.LTGRAY);
        if(current_turn.equals(player1)){
            current_turn = player2;

        }else{
            current_turn = player1;
        }
        String[] button_int_val = new String[9];
        for(int i = 0; i < 9; i ++) {
            if(array_buttons[i].getText().equals("X")) {
                button_int_val[i] = "2";
            } else if(array_buttons[i].getText().equals("O")) {
                button_int_val[i] = "1";
            } else {
                button_int_val[i] = "0";
            }
        }
        setGameData(current_game_id, button_int_val,current_turn, v.getContext());
        hero(v);
    }

    private void hero(View v) {

        boolean won = false;

        // check voor horizontaal:
        if ((button.getText().equals(button1.getText()) && button1.getText().equals(button2.getText()) && button.getText() != "")
                || (button3.getText().equals(button4.getText()) && button4.getText().equals(button5.getText()) && button3.getText() != "")
                || (button6.getText().equals(button7.getText()) && button7.getText().equals(button8.getText()) && button6.getText() != "")) {
            won = true;
            // check voor verticaal:
        } else if ((button.getText().equals(button3.getText()) && button3.getText().equals(button6.getText()) && button.getText() != "")
                || (button1.getText().equals(button4.getText()) && button4.getText().equals(button7.getText()) && button1.getText() != "")
                || (button2.getText().equals(button5.getText()) && button5.getText().equals(button8.getText()) && button2.getText() != "")) {

            won = true;
            // check voor schuin:
        } else if ((button.getText().equals(button4.getText()) && button4.getText().equals(button8.getText()) && button.getText() != "")
                || (button2.getText().equals(button4.getText()) && button4.getText().equals(button6.getText()) && button2.getText() != "")) {
            won = true;
        }


        if (won) {
            if (current_turn.equals(player1)) {
                notify("Speler X wint!");
                finishGame(player2, current_game_id, v.getContext());
            } else {
                notify("Speler O wint!");
                finishGame(player1, current_game_id, v.getContext());
            }
        } else {
            boolean done = true;
            for(int i = 0; i < 9; i ++) {
                if(array_buttons[i].getText() == "") {
                    done = false;
                }
            }
            if(done) {
                finishGame("none", current_game_id, v.getContext());
                notify("Het is gelijk!");
            }
        }

    }

    private void notify(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void startGame(String player1, String player2, double amount, int request_id, Context ctext) {
        if (!Login.isLoggedIn() || player1.equals(player2))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("player1", player1));
        params.add(new BasicNameValuePair("player2", player2));
        params.add(new BasicNameValuePair("amount", "" + amount));
        params.add(new BasicNameValuePair("request_id", "" + request_id));
        params.add(new BasicNameValuePair("group_id", "" + Groups.current_group_id));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.STARTGAME);
        jr.execute("http://intotheblu.nl/game_request_accept.php");
    }

    public static void finishGame(String winner, String game_id, Context ctext) {
        if (!Login.isLoggedIn() || player1.equals(player2))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("winner", winner));
        params.add(new BasicNameValuePair("game_id", game_id));

        try {
            ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", winner + " has just won a game of Tic Tac Toe!");
            jsonObject.put("friendName", Login.getLoginName());
            jsonObject.put("class", "gamefinish");
            jsonObject.put("groupID", Integer.toString(Groups.current_group_id));
            jsonObject.put("groupName", Groups.current_group_name);
            ParsePush push = new ParsePush();
            pushQuery.whereEqualTo("username", current_turn);
            push.setQuery(pushQuery); // Set our Installation query
            push.setData(jsonObject);
            push.sendInBackground();
        } catch(JSONException e) {
            Log.e("", "failed JSON.");
        }

        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FINISHGAME);
        jr.execute("http://intotheblu.nl/game_finish.php");
    }

    public static void setGameData(String game_id, String fields[], String turn, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("game_id", game_id));
        params.add(new BasicNameValuePair("veld0", fields[0]));
        params.add(new BasicNameValuePair("veld1", fields[1]));
        params.add(new BasicNameValuePair("veld2", fields[2]));
        params.add(new BasicNameValuePair("veld3", fields[3]));
        params.add(new BasicNameValuePair("veld4", fields[4]));
        params.add(new BasicNameValuePair("veld5", fields[5]));
        params.add(new BasicNameValuePair("veld6", fields[6]));
        params.add(new BasicNameValuePair("veld7", fields[7]));
        params.add(new BasicNameValuePair("veld8", fields[8]));
        params.add(new BasicNameValuePair("turn", turn));
        try {
            ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", Login.getLoginName() + " has just played a turn in Tic Tac Toe!");
            jsonObject.put("friendName", Login.getLoginName());
            jsonObject.put("class", "gameturn");
            jsonObject.put("groupID", Integer.toString(Groups.current_group_id));
            jsonObject.put("groupName", Groups.current_group_name);
            ParsePush push = new ParsePush();
            pushQuery.whereEqualTo("username", turn);
            push.setQuery(pushQuery); // Set our Installation query
            push.setData(jsonObject);
            push.sendInBackground();
        } catch(JSONException e) {
            Log.e("", "failed JSON.");
        }

        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.UPDATEGAME);
        jr.execute("http://intotheblu.nl/game_set_data.php");
    }

    public static void get_game_data(int type, String game_id, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("game_id", game_id));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/game_get_state.php");
    }

    public static void update(String game_id, String[] fields, String turn) {
        Log.e("", "i updated");
        for(int i = 0; i < 9; i ++) {
            Log.e("", "fields is |" + fields[i] + "|");
            if(fields[i].contains("1")) {
                array_buttons[i].setText("O");
                array_buttons[i].setClickable(false);
                array_buttons[i].setBackgroundColor(Color.LTGRAY);
                Log.e("", "i even tried to set O");
            }
            else if(fields[i].contains("2")) {
                array_buttons[i].setText("X");
                array_buttons[i].setClickable(false);
                array_buttons[i].setBackgroundColor(Color.LTGRAY);
                Log.e("", "i even tried to set X");
            }
            else {
                array_buttons[i].setClickable(true);
            }
        }
        current_turn = turn;
        current_game_id = game_id;
    }
}