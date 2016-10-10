package in.nyuyu.android.salons;

import android.app.Activity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.nyuyu.android.R;
import in.nyuyu.android.salons.values.SalonListItem;

/**
 * Created by Vinay on 03/10/16.
 */
public class SalonListAdapter extends RecyclerView.Adapter<SalonListAdapter.SalonViewHolder> {

    private final LayoutInflater inflater;
    private List<SalonListItem> salons;

    public SalonListAdapter(Activity activity) {
        inflater = activity.getLayoutInflater();
        salons = new ArrayList<>();
    }

    @Override public SalonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SalonViewHolder(inflater.inflate(R.layout.item_salon, parent, false));
    }

    @Override public void onBindViewHolder(SalonViewHolder holder, int position) {
        SalonListItem salon = salons.get(position);
        holder.name.setText(salon.getName());
        holder.distance.setText(String.valueOf(salon.getDistance()) + "m");
    }

    @Override public int getItemCount() {
        return salons.size();
    }

    public void show(List<SalonListItem> salonList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new SalonDiffCallback(salons, salonList));
        salons = salonList;
        result.dispatchUpdatesTo(this);
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView distance;

        public SalonViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.salon_name);
            distance = (TextView) itemView.findViewById(R.id.salon_distance);
        }
    }
}
