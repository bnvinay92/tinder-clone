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

    public static final String PATH = "sessions/%s/styles/last_seen/%s";
    private final DatabaseReference databaseReference;

    @Inject public LastSeenStyleIdQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Single<String> execute(String userId, String styleListFilterParameters) {
        return Rx.once(databaseReference.child(path(userId, styleListFilterParameters)))
                .map(snapshot -> snapshot.getValue(Swipe.class))
                .map(swipe -> swipe == null ? "0" : String.valueOf(swipe.getItem().getId() + 1));
    }

    public void update(String userId, String styleListFilterParameters, Swipe swipe) {
        databaseReference.child(path(userId, styleListFilterParameters)).setValue(swipe);
    }

    public static String path(String userId, String styleListFilterParameters) {
        return String.format(PATH, userId, styleListFilterParameters);
    }
}
