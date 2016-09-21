package in.nyuyu.android.style;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 20/09/16.
 */
public class LikedStyleListAdapter extends RecyclerView.Adapter<LikedStyleListAdapter.ViewHolder> {

    private List<StyleListItem> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private final Context context;

    public LikedStyleListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_likedstyle, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        StyleListItem item = items.get(position);
        Glide.with(context)
                .load(item.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.image);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void setItems(List<StyleListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_likedstyle_iview);
        }
    }
}