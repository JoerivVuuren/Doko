package nl.uva.kite.Doko.Fragments.Tabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.JSONRetrieve;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.TicTacToe;

public class Tab2 extends Fragment {
    public static String[] requests_game;
    public static int[] requests_game_id;
    public static double[] requests_game_amount;
    public static String[] requests_game_name;
    public static String[] requests_debit;
    public static int[] requests_debit_id;
    public static double[] requests_debit_amount;
    public static String[] requests_debit_reason;
    public static String[] requests_credit;
    public static int[] requests_credit_id;
    public static double[] requests_credit_amount;
    public static String[] requests_credit_reason;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        get_request_list(OnJSONCompleted.ALLREQUESTUPDATE, this.getActivity());

        /* display user's name */
        TextView meName = (TextView)v.findViewById(R.id.me_name);
        meName.setText(Login.getLoginName());
//        ListView friendRequestsLv = (ListView)v.findViewById(R.id.friend_request_list);
//        ArrayList<String> friendRequests = new ArrayList<String>();
//        friendRequests.add("Staci Carr");
//        friendRequests.add("Staci Carr");
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friend_request_list_row,R.id.friend_request_name, friendRequests);
//        friendRequestsLv.setAdapter(listAdapter);

        final ListView gameReq = (ListView)v.findViewById(R.id.game_request_list);
        gameReq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final String selectedFromList = (gameReq.getItemAtPosition(position).toString());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                alertDialogBuilder.setTitle("Accept the game from " + selectedFromList + "?");

                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TicTacToe.startGame(selectedFromList, Login.getLoginName(),requests_game_amount[position], requests_game_id[position], view.getContext());
                                view.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                denyGame(requests_game_id[position], view.getContext());
                                view.setVisibility(View.GONE);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        final ListView debitReq = (ListView)v.findViewById(R.id.debit_request_list);
        debitReq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final String selectedFromList = (debitReq.getItemAtPosition(position).toString());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                alertDialogBuilder.setTitle("Accept debit from " + selectedFromList + "?");

                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                addDebt(requests_debit_amount[position], requests_debit[position], Login.getLoginName(),
                                        requests_debit_reason[position], requests_debit_id[position], Groups.current_group_id,
                                        view.getContext(), "debit");
                                view.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteDebtRequest(requests_debit_id[position], "debit", view.getContext());
                                view.setVisibility(View.GONE);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        final ListView creditReq = (ListView)v.findViewById(R.id.credit_request_list);
        creditReq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final String selectedFromList = (creditReq.getItemAtPosition(position).toString());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                alertDialogBuilder.setTitle("Accept credit from " + selectedFromList + "?");

                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                addDebt(requests_credit_amount[position], Login.getLoginName(), requests_credit[position],
                                        requests_credit_reason[position], requests_credit_id[position], Groups.current_group_id,
                                        view.getContext(), "credit");
                                view.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteDebtRequest(requests_credit_id[position], "credit", view.getContext());
                                view.setVisibility(View.GONE);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        return v;
    }

    /* retrieves the user's friend request list from DB */
    public static void get_request_list(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + Groups.current_group_id));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/all_request_list.php");
    }

    /* hoi joeri */
    public static void acceptGame(int gameID, Context ctext) {
        /*if (!Login.isLoggedIn() || friendName.equals(Login.getLoginName()))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FRIENDADD);
        jr.execute("http://intotheblu.nl/friend_request_accept.php");*/
        Log.e("", "remove gameID request from DB and start game...");
    }

    public static void denyGame(int gameID, Context ctext){
        /*List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/friend_request_delete.php");*/
        Log.e("", "delete request from DB here");
    }

    public void addDebt(double debt, String creditor, String debitor, String reason, int requestID, int groupID, Context ctext, String debtType) {
        if (!Login.isLoggedIn() || creditor.equals(debitor))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("creditor", creditor));
        params.add(new BasicNameValuePair("debitor", debitor));
        params.add(new BasicNameValuePair("request_id", "" + requestID));
        params.add(new BasicNameValuePair("group_id", "" + groupID));
        params.add(new BasicNameValuePair("origin", reason));
        params.add(new BasicNameValuePair("debt", "" + debt));
        params.add(new BasicNameValuePair("type", debtType));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.DEBTADD);
        jr.execute("http://intotheblu.nl/debt_add.php");
    }

    public void deleteDebtRequest(int requestID, String debtType, Context ctext){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("request_id", "" + requestID));
        params.add(new BasicNameValuePair("type", debtType));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/debt_request_delete.php");
    }
}