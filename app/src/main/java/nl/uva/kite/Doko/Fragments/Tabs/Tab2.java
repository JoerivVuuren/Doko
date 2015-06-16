package nl.uva.kite.Doko.Fragments.Tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.OnJSONCompleted;
import nl.uva.kite.Doko.R;

public class Tab2 extends Fragment {
    ListView friendRequests;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT;}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        ListView friendRequestsLv = (ListView)v.findViewById(R.id.friend_request_list);
        ArrayList<String> friendRequests = new ArrayList<String>();
        friendRequests.add("Staci Carr");
        friendRequests.add("Staci Carr");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friend_request_list_row,R.id.friend_request_name, friendRequests);
        friendRequestsLv.setAdapter(listAdapter);
        return v;
    }
}