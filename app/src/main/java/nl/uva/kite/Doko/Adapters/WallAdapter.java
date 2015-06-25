package nl.uva.kite.Doko.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.R;

/**
 * Created by whenislunch on 22-6-15.
 */
public class WallAdapter extends RecyclerView.Adapter<WallAdapter.WallViewHolder>{
    private List<WallInfo> wallList;

    public WallAdapter(List<WallInfo> wallList){
        this.wallList = wallList;
    }

    @Override
    public int getItemCount() {
        return wallList.size();
    }

    public void onBindViewHolder(WallViewHolder contactViewHolder, int i) {
        WallInfo wi = wallList.get(i);
//        username is always the same
        WallViewHolder.vUserName.setText(wi.vUserName);
        WallViewHolder.vDateTime.setText(wi.vDateTime);
//        choose the type of post and continue appropriately
        switch(wi.vType){
//          Creating group
            case 1:
                WallViewHolder.vMessage.setText("Today I, " + wi.vUserName + " created the group "
                        + wi.vGroupName + "!");
                break;
//          Winning a game
            case 2:
                WallViewHolder.vMessage.setText("After challenging and beating " +
                        wi.vOpponentName + " in a game of " + wi.vGameName +  " I won " +
                        MainActivity.doubleToEuro(wi.vAmount) + "!");
                break;
//          Losing a game
            case 3:
                WallViewHolder.vMessage.setText("I challenged " + wi.vOpponentName +
                        " to a game of " + wi.vGameName + " but I lost the game as well as " +
                        MainActivity.doubleToEuro(wi.vAmount) + "...");
                break;
//          added a credit
            case 4:
                WallViewHolder.vMessage.setText("A new credit has been made from " + wi.vUserName
                + " to " + wi.vOpponentName + " for " + MainActivity.doubleToEuro(wi.vAmount));
                break;
//            added a debt
            case 5:
                WallViewHolder.vMessage.setText(wi.vUserName + " has opened a debt of " +
                        MainActivity.doubleToEuro(wi.vAmount) + " to " + wi.vOpponentName + "!");
                break;
            case 6:
                WallViewHolder.vMessage.setText(wi.vOpponentName + " was added to the group!");
                break;
            case 7:
//                tie!
                WallViewHolder.vMessage.setText("After having played a game of " + wi.vGameName +
                        " with " + wi.vOpponentName + " it ended in a tie and neither of us won!");
        }
    }

    @Override
    public WallViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.wall_post, viewGroup, false);

        return new WallViewHolder(itemView);
    }

    public static class WallViewHolder extends RecyclerView.ViewHolder {
        static protected TextView vUserName;
        static protected TextView vMessage;
        static protected TextView vDateTime;
        static protected String vOpponentName;
        static protected int vType;
        static protected double vAmount;

        public WallViewHolder(View v) {
            super(v);
            vUserName = (TextView) v.findViewById(R.id.wall_poster_name);
            vMessage = (TextView) v.findViewById(R.id.wall_post_message);
            vDateTime = (TextView) v.findViewById(R.id.wall_post_datetime);
        }
    }
}
