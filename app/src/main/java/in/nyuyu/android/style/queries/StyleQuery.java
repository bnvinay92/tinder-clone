package in.nyuyu.android.style.queries;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.style.values.Style;
import rx.Observable;

/**
 * Created by Vinay on 04/10/16.
 */
public class StyleQuery {

    private final DatabaseReference styles;

    @Inject public StyleQuery(DatabaseReference databaseReference) {
        this.styles = databaseReference.child("styles");
    }


    public Observable<Style> execute(Long id) {
        return Rx.once(styles.child(String.valueOf(id)))
                .map(snapshot -> snapshot.getValue(Style.class))
                .toObservable();
    }
}
