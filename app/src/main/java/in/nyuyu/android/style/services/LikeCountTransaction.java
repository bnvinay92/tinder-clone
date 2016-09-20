package in.nyuyu.android.style.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import in.nyuyu.android.style.StyleListItem;

/**
 * Created by Vinay on 20/09/16.
 */

public class LikeCountTransaction {

    public static final String PATH = "styles/%s";
    private final DatabaseReference databaseReference;

    @Inject public LikeCountTransaction(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void execute(String styleId) {
        databaseReference.child(String.format(PATH, styleId)).runTransaction(new Transaction.Handler() {
            @Override public Transaction.Result doTransaction(MutableData mutableData) {
                MutableData likeCount = mutableData.child("likeCount");
                Long likeCountValue = likeCount.getValue(Long.class);
                if (likeCountValue != null) {
                    likeCount.setValue(likeCountValue + 1);
                    mutableData.child("likeModified").setValue(ServerValue.TIMESTAMP);
                    return Transaction.success(mutableData);
                } else {
                    return null;
                }
            }

            @Override public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot snapshot) {
                if (committed) {
                    List<String> filterPaths = snapshot.child("filters").getValue(new GenericTypeIndicator<List<String>>() {
                    });
                    Long id = snapshot.child("id").getValue(Long.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    Long likeCount = snapshot.child("likeCount").getValue(Long.class);
                    Long likeModified = snapshot.child("likeModified").getValue(Long.class);

                    StyleListItem item = new StyleListItem(id, imageUrl, likeCount, likeModified, name);
                    Map<String, Object> payload = new HashMap<String, Object>();
                    for (String path : filterPaths) {
                        payload.put(path, item);
                    }
                    databaseReference.updateChildren(payload);
                }
            }
        });
    }
}
