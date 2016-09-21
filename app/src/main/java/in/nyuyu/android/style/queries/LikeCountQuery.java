package in.nyuyu.android.style.queries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.Observable;

/**
 * Created by Vinay on 21/09/16.
 */
public class LikeCountQuery {

    public static final String PATH = "sessions/%s/styles/liked";
    private final DatabaseReference databaseReference;

    @Inject public LikeCountQuery(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }


    public Observable<String> execute(String userId) {
        return Rx.values(databaseReference.child(String.format(PATH, userId)))
                .map(DataSnapshot::getChildrenCount)
                .map(String::valueOf);
    }
}
