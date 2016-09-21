package in.nyuyu.android.style;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListAdapter extends ArrayAdapter<StyleListItem> {

    private final LayoutInflater inflater;

    public StyleListAdapter(Context context) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull @Override public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_style, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        StyleListItem model = getItem(position);

        Glide.with(getContext())
                .load(model.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(holder.image);
        holder.name.setText(model.getName());
        holder.likeCount.setText(String.valueOf(model.getLikeCount()));
        return view;
    }

    private static class ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView likeCount;

        public ViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.item_style_tview_name);
            this.image = (ImageView) view.findViewById(R.id.item_style_iview);
            this.likeCount = (TextView) view.findViewById(R.id.item_style_tview_likecount);
        }
    }
}
