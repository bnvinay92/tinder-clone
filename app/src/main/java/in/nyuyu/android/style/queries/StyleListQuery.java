package in.nyuyu.android.style.queries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.style.StyleListItem;
import rx.Single;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListQuery {

    public static final String PATH = "styles/filters/%s";
    private final DatabaseReference databaseReference;

    @Inject public StyleListQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Single<List<StyleListItem>> execute(String styleListFilterParameters, String startAtStyleId) {
        return Rx.once(databaseReference.child(String.format(PATH, styleListFilterParameters)).orderByKey().startAt(startAtStyleId))
                .map(DataSnapshot::getChildren)
                .map(snapshots -> {
                    List<StyleListItem> items = new ArrayList<>();
                    for (DataSnapshot snapshot : snapshots) {
                        items.add(snapshot.getValue(StyleListItem.class));
                    }
                    return items;
                });
    }
}
