package nl.uva.kite.Doko.Fragments.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import nl.uva.kite.Doko.Friends;
import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;

public class Tab3 extends Fragment {
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3,container,false);
        listView = (ListView)v.findViewById(R.id.groups_list);
        //FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.groups_fab);
        //fab.attachToListView(listView);

        /* code voor ListView + FloatingActionButton:

        listView = (ListView)v.findViewById(R.id.fabulous_list);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabulous_fab);
        fab.attachToListView(listView);*/

        return v;
    }

    public void onResume() {
        super.onResume();
        Log.e("", "On RESUME");
        if(Login.isLoggedIn()) {
            Groups.get_groupmembers(3, listView.getContext());
        }
    }
}