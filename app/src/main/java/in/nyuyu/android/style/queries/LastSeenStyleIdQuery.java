package in.nyuyu.android.style.queries;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
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
        return Rx.once(databaseReference.child(String.format(PATH, userId, styleListFilterParameters)))
                .map(snapshot -> snapshot.getValue(Long.class))
                .map(styleId -> styleId == null ? "0" : String.valueOf(styleId + 1));
    }
}
