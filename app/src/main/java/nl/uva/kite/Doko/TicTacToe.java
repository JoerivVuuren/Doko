package nl.uva.kite.Doko;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends Activity implements OnClickListener {

    // x=true en O=false
    boolean round = true;
    int total_round = 0;
    Button[] array_buttons = null;
    Button button, button1, button2, button3, button4, button5, button6, button7, button8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

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

        for (Button b : array_buttons) {
            b.setOnClickListener(this);
        }

        Button new_game = (Button) findViewById(R.id.newgame);
        new_game.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                round = true;
                total_round = 0;
                startAndstop(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        buttonClicked(v);
    }

    private void buttonClicked(View v) {
        Button b = (Button) v;

        if (round) {
            b.setText("X");
        } else {
            b.setText("O");
        }

        b.setClickable(false);
        b.setBackgroundColor(Color.LTGRAY);
        total_round++;
        round = !round;
        hero();
    }

    private void hero() {

        boolean won = false;

        // check voor horizontaal:
        if ((button.getText() == button1.getText() && button1.getText() == button2.getText() && !button.isClickable())
                || (button3.getText() == button4.getText() && button4.getText() == button5.getText() && !button3.isClickable())
                || (button6.getText() == button7.getText() && button7.getText() == button8.getText() && !button6.isClickable())) {

            won = true;
            // check voor verticaal:
        } else if ((button.getText() == button3.getText() && button3.getText() == button6.getText() && !button.isClickable())
                || (button1.getText() == button4.getText() && button4.getText() == button7.getText() && !button4.isClickable())
                || (button2.getText() == button5.getText() && button5.getText() == button8.getText() && !button8.isClickable())) {

            won = true;
            // check voor schuin:
        } else if ((button.getText() == button4.getText() && button4.getText() == button8.getText() && !button.isClickable())
                || (button2.getText() == button4.getText() && button4.getText() == button6.getText() && !button4.isClickable())) {

            won = true;
        }


        if (won) {
            if (!round) {
                notify("Speler X wint!");
            } else {
                notify("Speler O wint!");
                startAndstop(false);
            }
        } else if (total_round == 9) {
            notify("Het is gelijk!");
        }

    }

    private void notify(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void startGame(String player1, String player2, Context ctext) {
        if (!Login.isLoggedIn() || player1.equals(player2))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("player1", player1));
        params.add(new BasicNameValuePair("player2", player2));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.STARTGAME);
        jr.execute("http://intotheblu.nl/game_request_accept.php");
    }

    public static void update(int game_id, int veld0, int veld1, int veld2, int veld3, int veld4, int veld5, int veld6, int veld7, int veld8, String player1, String player2, Double amount, String turn) {
        //setgamedata
    }

    private void startAndstop(boolean enable) {
        for (Button b : array_buttons) {
            b.setText("");
            b.setClickable(enable);

            if (enable) {
                b.setBackgroundColor(Color.parseColor("#33e563"));
            } else {
                b.setBackgroundColor(Color.LTGRAY);
            }

        }
    }
}