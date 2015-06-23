package nl.uva.kite.Doko.GroupAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.uva.kite.Doko.R;


/**
 * Created by whenislunch on 19-6-15.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private List<GroupInfo> groupList;

    public GroupAdapter(List<GroupInfo> groupList) {
        this.groupList = groupList;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public void onBindViewHolder(GroupViewHolder contactViewHolder, int i) {
        GroupInfo gi = groupList.get(i);
        GroupViewHolder.vGroupName.setText(gi.groupName);
        GroupViewHolder.vMemberCount.setText("" + gi.memberCount);
//        GroupViewHolder.vEmail.setText(ci.email);
//        GroupViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.group_list_row, viewGroup, false);

        return new GroupViewHolder(itemView);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        static protected TextView vGroupName;
        static protected TextView vMemberCount;

        public GroupViewHolder(View v) {
            super(v);
            vGroupName =  (TextView) v.findViewById(R.id.group_list_name);
            vMemberCount = (TextView)  v.findViewById(R.id.group_member_count);
        }
    }
}
