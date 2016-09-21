package in.nyuyu.android.style.queries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Vinay on 21/09/16.
 */
public class LikeCountUpdater {

    private final DatabaseReference databaseReference;

    @Inject public LikeCountUpdater(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference.child("stylefilters");
    }

    public void execute(DataSnapshot snapshot) {
        List<String> filterPaths = snapshot.child("filters").getValue(new GenericTypeIndicator<List<String>>() {
        });
        Long likeCount = snapshot.child("likeCount").getValue(Long.class);
        Long likeModified = snapshot.child("likeModified").getValue(Long.class);
        Map<String, Object> payload = new HashMap<>();
        for (String path : filterPaths) {
            payload.put(path.concat("/likeCount"), likeCount);
            payload.put(path.concat("/likeModified"), likeModified);
        }
        databaseReference.updateChildren(payload);
    }
}
