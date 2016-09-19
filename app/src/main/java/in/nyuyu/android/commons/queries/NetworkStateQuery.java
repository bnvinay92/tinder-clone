package in.nyuyu.android.commons.queries;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */
public class NetworkStateQuery {

    private final DatabaseReference connectedRef;

    @Inject public NetworkStateQuery(DatabaseReference databaseReference) {
        connectedRef = databaseReference.child(".info/connected");
    }

    public Observable<Boolean> execute() {
        return Rx.values(connectedRef)
                .map(snapshot -> snapshot.getValue(Boolean.class));
    }
}
