package nl.uva.kite.Doko;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.Fragments.Tabs.Tab1;

public class Friends extends Fragment {
    public static String[] friends;
    public static String[] pictures;
    public static String[] requests;
    private RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (RelativeLayout)inflater.inflate(R.layout.friends, container, false);

        if (Login.isLoggedIn()) {
            /* get friend list from DB and update list of friends in Fragment */
            Friends.get_friend_request_list(OnJSONCompleted.FRIENDREQUESTUPDATE, this.getActivity());
            Friends.get_friendlist(OnJSONCompleted.FRIENDLISTUPDATE, this.getActivity());
        }
        return layout;
    }

    /* adds a friend to the user's friend list */
    public static void add(String friendName, Context ctext) {
        if (!Login.isLoggedIn() || friendName.equals(Login.getLoginName()))
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.FRIENDADD);
        jr.execute("http://intotheblu.nl/friend_request_accept.php");
    }

    public static void deny_request(String friendName, Context ctext){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("friend", friendName));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.NONE);
        jr.execute("http://intotheblu.nl/friend_request_delete.php");
    }

    /* retrieves the user's friends list from DB */
    public static void get_friendlist(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/friends_list.php");
    }

    /* retrieves the user's friend request list from DB */
    public static void get_friend_request_list(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/friend_request_list.php");
    }

    public static String[] getFriendList() {
        return friends;
    }
}
