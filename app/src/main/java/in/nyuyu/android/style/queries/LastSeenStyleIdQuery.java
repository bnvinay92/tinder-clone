package in.nyuyu.android.style.queries;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.style.values.Swipe;
import rx.Single;

/**
 * Created by Vinay on 20/09/16.
 */
public class LastSeenStyleIdQuery {

    public static final String PATH = "sessions/%s/styles/last_seen/%s/item/id";
    public static final String WRITE_PATH = "sessions/%s/styles/last_seen/%s";
    private final DatabaseReference databaseReference;

    @Inject public LastSeenStyleIdQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Single<String> execute(String userId, String styleListFilterParameters) {
        return Rx.once(databaseReference.child(String.format(PATH, userId, styleListFilterParameters)))
                .map(snapshot -> snapshot.getValue(Long.class))
                .map(id -> id == null ? "-1" : String.valueOf(id + 1));
    }

    public void update(String userId, String styleListFilterParameters, Swipe swipe) {
        databaseReference.child(String.format(WRITE_PATH, userId, styleListFilterParameters)).setValue(swipe);
    }

    public void delete(String userId, String styleListFilterParameters) {
        databaseReference.child(String.format(WRITE_PATH, userId, styleListFilterParameters)).setValue(null);
    }
}
