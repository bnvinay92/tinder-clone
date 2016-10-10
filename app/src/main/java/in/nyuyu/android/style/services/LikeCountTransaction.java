package in.nyuyu.android.style.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */

public class LikeCountTransaction {

    public static final String PATH = "styles/%s/likeInfo";
    private final DatabaseReference databaseReference;

    @Inject public LikeCountTransaction(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Observable<DataSnapshot> execute(Long styleId) {
        String path = String.format(PATH, styleId);
        return Rx.transact(databaseReference.child(path), mutableData -> {
            MutableData likeCount = mutableData.child("likeCount");
            Long likeCountValue = likeCount.getValue(Long.class);
            if (likeCountValue != null) {
                likeCount.setValue(likeCountValue + 1);
                mutableData.child("likeModified").setValue(ServerValue.TIMESTAMP);
            }
            return Transaction.success(mutableData);
        }).toObservable();
    }
}
