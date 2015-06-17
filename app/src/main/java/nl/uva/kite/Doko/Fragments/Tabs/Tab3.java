package nl.uva.kite.Doko.Fragments.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import nl.uva.kite.Doko.Friends;
import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;

public class Tab3 extends Fragment {
    public static ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3,container,false);

        listView = (ListView) v.findViewById(R.id.groups_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (listView.getItemAtPosition(position).toString());
                Log.e("", "Clicked on " + selectedFromList);
            }
        });
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
//        set refresh color
        swipeView.setColorSchemeColors(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );

//        sweet refresh on pulldown
        swipeView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeView.setRefreshing(true);
                        if (Login.isLoggedIn()) {
//                            update the list!
                            Groups.get_groupmembers(listView.getContext());
                        }
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeView.setRefreshing(false);

                            }
                        }, 1000);
                    }
                });

//        let  onscrollListene handle enabling/disabling mechanism
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("", "On RESUME");
        if (Login.isLoggedIn()) {
            Groups.get_groupmembers(Tab3.listView.getContext());
        }
    }
}