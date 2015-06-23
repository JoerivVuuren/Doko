package nl.uva.kite.Doko.WallAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        WallViewHolder.vPosterName.setText(wi.vPosterName);
        WallViewHolder.vMessage.setText(wi.vMessage);
    }

    @Override
    public WallViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.wall_post, viewGroup, false);

        return new WallViewHolder(itemView);
    }

    public static class WallViewHolder extends RecyclerView.ViewHolder {
        static protected TextView vPosterName;
        static protected TextView vMessage;
//        static protected int vType;
//        static protected double vAmount;

        public WallViewHolder(View v) {
            super(v);
            vPosterName = (TextView) v.findViewById(R.id.wall_poster_name);
            vMessage = (TextView) v.findViewById(R.id.wall_post_message);
        }
    }
}
