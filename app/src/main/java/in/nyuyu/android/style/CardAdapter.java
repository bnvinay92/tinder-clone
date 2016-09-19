package in.nyuyu.android.style;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 19/09/16.
 */
public class CardAdapter extends ArrayAdapter<String> {

    public CardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            contentView = View.inflate(getContext(), R.layout.item_style, null);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        holder.textView.setText(getItem(position));

        return contentView;
    }

    private static class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.item_style_tview);
        }
    }

}
