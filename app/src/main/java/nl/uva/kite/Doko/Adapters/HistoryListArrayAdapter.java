/* TUTORIAL USED: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown_example */

package nl.uva.kite.Doko.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nl.uva.kite.Doko.Fragments.Tabs.Tab2;
import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.R;

public class HistoryListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] names;

    public HistoryListArrayAdapter(Context context, String[] names) {
        super(context, -1, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.history_list_row, parent, false);

        TextView opponent = (TextView)rowView.findViewById(R.id.history_list_name);
        TextView amount = (TextView)rowView.findViewById(R.id.history_list_amount);
        TextView reason = (TextView)rowView.findViewById(R.id.history_list_reason);
        TextView groupName = (TextView)rowView.findViewById(R.id.history_list_group_name);
        TextView dateTime = (TextView)rowView.findViewById(R.id.history_list_datetime);

        opponent.setText(names[position]);
        reason.setText(Tab2.h_reason[position]);
        groupName.setText(Tab2.h_group_name[position]);
        dateTime.setText(Tab2.h_datetime[position]);
        amount.setText(MainActivity.doubleToEuro(Tab2.h_amount[position]));

        if (Tab2.h_amount[position] < 0)
            /* color amount negative */
            amount.setTextColor(context.getResources().getColor(R.color.colorNegative));

        return rowView;
    }

}