/* TUTORIAL USED: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown_example */
package nl.uva.kite.Doko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberlistArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] names;

    public MemberlistArrayAdapter(Context context, String[] names) {
        super(context, -1, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.member_list_row, parent, false);

        TextView memberName = (TextView)rowView.findViewById(R.id.member_group_list_name);
        TextView memberDebt = (TextView)rowView.findViewById(R.id.group_list_debt);
        TextView memberUserLevel = (TextView)rowView.findViewById(R.id.member_group_list_user_level);
        CircleImageView memberPic = (CircleImageView)rowView.findViewById(R.id.group_list_image);

        /* set member name and debt */
        memberName.setText(names[position]);
        memberDebt.setText(Groups.current_group_debts_euro[position]);

        /* color member debt */
        if (Groups.current_group_debts[position] < 0)
            memberDebt.setTextColor(context.getResources().getColor(R.color.colorNegative));

        /* display member Administrator level */
        if (Groups.current_group_admin_name.equals(names[position]))
            memberUserLevel.setText("Administrator");

        /* set member profile picture */
        if (Groups.current_group_pictures[position].equals("artin.jpg"))
            memberPic.setImageResource(R.drawable.artin);
        else
            memberPic.setImageResource(R.drawable.aka);

        return rowView;
    }
}
