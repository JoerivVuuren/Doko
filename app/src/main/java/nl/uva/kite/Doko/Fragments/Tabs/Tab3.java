package nl.uva.kite.Doko.Fragments.Tabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import nl.uva.kite.Doko.Adapters.ListViewHeightFix;
import nl.uva.kite.Doko.Adapters.UserHistoryArrayAdapter;
import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.R;

public class Tab3 extends Fragment {
    public static ListView listView;
    public static View thisView;

    public static String[] h_opponent;
    public static double[] h_amount;
    public static String[] h_datetime;
    public static String[] h_reason;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3,container,false);

        listView = (ListView) v.findViewById(R.id.groups_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // do this when an item of the list is clicked
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (listView.getItemAtPosition(position).toString());
                Groups.get_history_user(Groups.current_group_id, selectedFromList, view);
            }
        });
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
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

        /* update group member list */
        Groups.get_groupmembers(this.getActivity());

        return v;
    }

    /* displays user info in a popup window */
    public static void showPopup(String username, Activity a) {
        if (thisView == null)
            return;

        View popupView = a.getLayoutInflater().inflate(R.layout.popup_user, null);
        PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        /* set title */
        TextView title = (TextView)popupView.findViewById(R.id.popup_user_name);
        title.setText("Debt history for " + username);

        /* create a ListView for user's history */
        ListView historyView = (ListView)popupView.findViewById(R.id.popup_user_history);
        TextView noHistory = (TextView)a.findViewById(R.id.no_user_history);
        historyView.setEmptyView(noHistory);
        UserHistoryArrayAdapter historyAdapter = new UserHistoryArrayAdapter(thisView.getContext(), h_opponent);
        historyView.setAdapter(historyAdapter);
        ListViewHeightFix.setListViewHeightBasedOnChildren(historyView);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(thisView, Gravity.CENTER_HORIZONTAL, 0, -650);

    }
}