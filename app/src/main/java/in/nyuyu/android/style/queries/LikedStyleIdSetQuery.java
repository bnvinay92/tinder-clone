package in.nyuyu.android.style.queries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.Single;

/**
 * Created by Vinay on 20/09/16.
 */
public class LikedStyleIdSetQuery {
    public static final String PATH = "sessions/%s/styles/liked";
    private final DatabaseReference databaseReference;

    @Inject public LikedStyleIdSetQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Single<Set<String>> execute(String userId) {
        return Rx.once(databaseReference.child(String.format(PATH, userId)))
                .map(DataSnapshot::getChildren)
                .map(snapshots -> {
                    Set<String> likedStyleIds = new HashSet<>();
                    for (DataSnapshot snapshot : snapshots) {
                        likedStyleIds.add(snapshot.getKey());
                    }
                    return likedStyleIds;
                });
    }
}
