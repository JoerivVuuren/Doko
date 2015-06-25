/* TUTORIAL USED: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown_example */
package nl.uva.kite.Doko.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.uva.kite.Doko.DownloadImageTask;
import nl.uva.kite.Doko.Groups;
import nl.uva.kite.Doko.Login;
import nl.uva.kite.Doko.R;

public class FriendListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] names;

    public FriendListArrayAdapter(Context context, String[] names) {
        super(context, -1, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.member_list_row, parent, false);

        TextView memberName = (TextView)rowView.findViewById(R.id.member_group_list_name);
        //TextView memberDebt = (TextView)rowView.findViewById(R.id.group_list_debt);
        TextView memberUserLevel = (TextView)rowView.findViewById(R.id.member_group_list_user_level);
        CircleImageView memberPic = (CircleImageView)rowView.findViewById(R.id.group_list_image);

        /* set member name and debt */
        memberName.setText(names[position]);
        //memberDebt.setText(Groups.current_group_debts_euro[position]);

        /* set member profile picture */
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        try {
            downloadImageTask.setImageFromURL(memberPic, "http://intotheblu.nl/image/"
                    + names[position] + ".jpg");
        }
        catch(Exception e) {
        }


        return rowView;
    }
}
