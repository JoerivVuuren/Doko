/* TUTORIAL USED: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown_example */
package nl.uva.kite.Doko.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nl.uva.kite.Doko.Fragments.Tabs.Tab2;
import nl.uva.kite.Doko.Fragments.Tabs.Tab4;
import nl.uva.kite.Doko.MainActivity;
import nl.uva.kite.Doko.R;

public class RequestListArrayAdapter extends ArrayAdapter<String> {
    public static int GAME = 1;
    public static int CREDIT = 2;
    public static int DEBIT = 3;
    public static int GAMELIST = 4;
    private final Context context;
    private final String[] names;
    public int type;

    public RequestListArrayAdapter(Context context, String[] names) {
        super(context, -1, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.requests_list_row, parent, false);

        TextView opponent = (TextView)rowView.findViewById(R.id.request_list_name);
        TextView amount = (TextView)rowView.findViewById(R.id.request_list_amount);
        TextView reason = (TextView)rowView.findViewById(R.id.request_list_reason);

        /* set member name */
        opponent.setText(names[position]);

        if (type == GAME) {
            /* set name of game, amount and opponent */
            reason.setText(/*Tab2.requests_game_name[position]*/"Tic Tac Toe");
            amount.setText(MainActivity.doubleToEuro(Tab2.requests_game_amount[position]));
            opponent.setText(Tab2.requests_game[position]);
        }
        else if (type == DEBIT){
            /* Set reason of debt, amount and opponent */
            reason.setText(Tab2.requests_debit_reason[position]);
            amount.setText(MainActivity.doubleToEuro(Tab2.requests_debit_amount[position]));
            opponent.setText(Tab2.requests_debit[position]);

            /* color amount */
            amount.setTextColor(context.getResources().getColor(R.color.colorNegative));
        }
        else if (type == CREDIT) {
            /* Set reason of credit, amount and opponent */
            reason.setText(Tab2.requests_credit_reason[position]);
            amount.setText(MainActivity.doubleToEuro(Tab2.requests_credit_amount[position]));
            opponent.setText(Tab2.requests_credit[position]);
        }
        else if (type == GAMELIST) {
             /* Set reason of debt, amount and opponent */
            reason.setText("Tic Tac Toe");
            amount.setText(MainActivity.doubleToEuro(Tab4.game_wagers[position]));
            opponent.setText(Tab4.game_opponents[position]);
        }
        return rowView;
    }

    public void setType(int t) {
        this.type = t;
    }
}
