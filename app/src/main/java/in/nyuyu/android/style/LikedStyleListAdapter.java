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

    private OnItemClickListener listener;

    public StyleListItem get(int position) {
        return items.get(position);
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public LikedStyleListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = (OnItemClickListener) context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_likedstyle, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        StyleListItem item = items.get(position);
        Glide.with(context)
                .load(item.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(holder.image);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void setItems(List<StyleListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_likedstyle_iview);
            delete = (ImageView) itemView.findViewById(R.id.item_likedstyle_iview_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(delete, position);
                        }
                    }
                }
            });
        }
    }
}