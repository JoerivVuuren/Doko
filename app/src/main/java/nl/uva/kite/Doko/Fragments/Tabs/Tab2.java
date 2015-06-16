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
import android.widget.EditText;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.JSONRetrieve;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;

public class Tab2 extends Fragment {
    public static String[] requests_game;
    public static String[] requests_debt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        get_request_list(OnJSONCompleted.ALLREQUESTUPDATE, this.getActivity());
//        ListView friendRequestsLv = (ListView)v.findViewById(R.id.friend_request_list);
//        ArrayList<String> friendRequests = new ArrayList<String>();
//        friendRequests.add("Staci Carr");
//        friendRequests.add("Staci Carr");
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friend_request_list_row,R.id.friend_request_name, friendRequests);
//        friendRequestsLv.setAdapter(listAdapter);
        return v;
    }

    /* retrieves the user's friend request list from DB */
    public static void get_request_list(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/all_request_list.php");
    }

    /* adds a friend to the user's friend list */
    public static void add(String friendName, Context ctext) {
        /*if (!Login.isLoggedIn() || friendName.equals(Login.getLoginName()))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FRIENDADD);
        jr.execute("http://intotheblu.nl/friend_request_accept.php");*/
        Log.e("", "add request to DB here");
    }

    public static void deny_request(String friendName, Context ctext){
        /*List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/friend_request_delete.php");*/
        Log.e("", "delete request from DB here");
    }
}