package nl.uva.kite.Doko.Fragments.Tabs;

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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.WallAdapter.WallAdapter;
import nl.uva.kite.Doko.WallAdapter.WallInfo;


public class Tab1 extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

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
////                            update the list!
//                            Groups.get_groupmembers(listView.getContext());
                        }
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeView.setRefreshing(false);

                            }
                        }, 1000);
                    }
                });



        WallAdapter wa = new WallAdapter(generateStuff(4));
        mRecyclerView.setAdapter(wa);
        return v;
    }

    private List generateStuff(int size){
        List<WallInfo> dataSet = new ArrayList<>();
        for(int i = 0 ; i < size ; i++){
            WallInfo wi = new WallInfo();
            wi.vType = i;
            wi.vAmount = 69.99;
            wi.vGameName = "Tic Tac Toe";
            wi.vUserName = "Chiquita Banaan";
            wi.vOpponentName = "Matthijs Bes";
            wi.vGroupName = Groups.current_group_name;
            dataSet.add(wi);
        }
        return dataSet;
    }
}
