package in.nyuyu.android.style.queries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.style.StyleListItem;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */
public class LikedStyleListQuery {

    public static final String PATH = "sessions/%s/styles/liked";

    private final DatabaseReference databaseReference;

    @Inject public LikedStyleListQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Observable<List<StyleListItem>> execute(String userId) {
        return Rx.values(databaseReference.child(String.format(PATH, userId)))
                .map(DataSnapshot::getChildren)
                .map(snapshots -> {
                    List<StyleListItem> items = new ArrayList<>();
                    for (DataSnapshot snapshot : snapshots) {
                        items.add(snapshot.getValue(StyleListItem.class));
                    }
                    return items;
                });
    }

    public void update(String userId, StyleListItem item) {
        databaseReference.child(String.format(PATH, userId))
                .child(String.valueOf(item.getId()))
                .setValue(item);
    }
}
