package in.nyuyu.android.salons;

import android.support.v7.util.DiffUtil;

import java.util.List;

import in.nyuyu.android.salons.values.SalonListItem;

/**
 * Created by Vinay on 03/10/16.
 */
public class SalonDiffCallback extends DiffUtil.Callback {

    private final List<SalonListItem> oldSalons;
    private final List<SalonListItem> newSalons;

    public SalonDiffCallback(List<SalonListItem> oldSalons, List<SalonListItem> newSalons) {
        this.oldSalons = oldSalons;
        this.newSalons = newSalons;
    }

    @Override public int getOldListSize() {
        return oldSalons.size();
    }

    @Override public int getNewListSize() {
        return newSalons.size();
    }

    @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSalons.get(oldItemPosition).getId().equals(newSalons.get(newItemPosition).getId());
    }

    @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSalons.get(oldItemPosition).equals(newSalons.get(newItemPosition));
    }
}
