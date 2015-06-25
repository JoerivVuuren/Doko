package nl.uva.kite.Doko.Fragments.Tabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.JSONRetrieve;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.TicTacToe;

public class Tab4 extends Fragment {
    public static String[] game_opponents;
    public static double[] game_wagers;
    public static String[] game_id;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_4,container,false);
        get_game_list(OnJSONCompleted.GAMELISTUPDATE, this.getActivity());
        Log.e("", "send request for game list");

        final ListView gameReq = (ListView)v.findViewById(R.id.game_list);
        TextView nogActiveGames = (TextView)v.findViewById(R.id.empty_active_games);
        gameReq.setEmptyView(nogActiveGames);
        gameReq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                get_game_data(OnJSONCompleted.LOADGAME, game_id[position], view.getContext());
            }
        });
        return v;
    }

    /* retrieves the user's friend request list from DB */
    public static void get_game_list(int type, Context ctext) {
        if (!Login.isLoggedIn())
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + Groups.current_group_id));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, type);
        jr.execute("http://intotheblu.nl/game_list.php");
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
}