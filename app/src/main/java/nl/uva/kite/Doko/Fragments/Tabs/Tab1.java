package nl.uva.kite.Doko.Fragments.Tabs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.uva.kite.Doko.DownloadImageTask;
import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.JSONRetrieve;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.Adapters.WallAdapter;
import nl.uva.kite.Doko.Adapters.WallInfo;


public class Tab1 extends Fragment {
    public static RecyclerView mRecyclerView;
    public static RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView.Adapter mAdapter;
    public static WallAdapter wa;

    /* wall data */
    public static String[] w_player1;
    public static String[] w_player2;
    public static double[] w_amount;
    public static String[] w_datetime;
    public static int[] w_type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.wall_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainerWall);
        //        set refresh color
        swipeView.setColorSchemeColors(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );
        //        sweet refresh on pulldown spinner
        swipeView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeView.setRefreshing(true);
                        if (Login.isLoggedIn()) {
                            wall_get_list(swipeView.getContext());
                        }
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeView.setRefreshing(false);

                            }
                        }, 1000);
                    }
                });

        /* update wall info */
        wall_get_list(v.getContext());

        return v;
    }

    /* retrieves the wall for this group from DB */
    public static void wall_get_list(Context ctext) {
        if (!Login.isLoggedIn() || Groups.current_group_id < 0)
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + Groups.current_group_id));
        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.WALLLIST);
        jr.execute("http://intotheblu.nl/wall_list.php");
    }

    /* adds a new wall item */
    public static void wall_add(String player1, String player2, double amount, int typ, int groupId, Context ctext) {
        int new_group = groupId;
        if (new_group == -1)
            new_group = Groups.current_group_id;

        if (!Login.isLoggedIn() || Groups.current_group_id < 0)
            return;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", Login.getLoginName()));
        params.add(new BasicNameValuePair("password", Login.getPassword()));
        params.add(new BasicNameValuePair("group_id", "" + new_group));
        params.add(new BasicNameValuePair("player1", player1));

        if (player2 != null)
            params.add(new BasicNameValuePair("player2", player2));

        params.add(new BasicNameValuePair("amount", "" + amount));
        params.add(new BasicNameValuePair("type", "" + typ));

        JSONRetrieve jr = new JSONRetrieve(ctext, params, OnJSONCompleted.WALLADD);
        jr.execute("http://intotheblu.nl/wall_add.php");
    }

    public static List generate_wall(Context ctext){
        List<WallInfo> dataSet = new ArrayList<>();

        for (int i = 0; w_player1 != null && i < w_player1.length; i++){
            WallInfo wi = new WallInfo();
            wi.vType = w_type[i];
            wi.vAmount = w_amount[i];
            wi.vGameName = "Tic Tac Toe";
            wi.vDateTime = w_datetime[i];
            wi.vUserName = w_player1[i];
            wi.vOpponentName = w_player2[i];
            wi.vGroupName = Groups.current_group_name;
            dataSet.add(wi);
        }
        return dataSet;
    }
}
