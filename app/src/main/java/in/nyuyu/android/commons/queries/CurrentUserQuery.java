package in.nyuyu.android.commons.queries;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.Single;

/**
 * Created by Vinay on 20/09/16.
 */
public class CurrentUserQuery {

    private final FirebaseAuth firebaseAuth;

    @Inject public CurrentUserQuery(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Single<FirebaseUser> execute() {
        return Rx.user(firebaseAuth);
    }

    public String getUserId() {
        return firebaseAuth.getCurrentUser().getUid();
    }
}
