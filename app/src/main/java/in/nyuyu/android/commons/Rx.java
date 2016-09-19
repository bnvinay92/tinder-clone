package in.nyuyu.android.commons;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;

/**
 * Created by Vinay on 19/09/16.
 */
public class Rx {
    public static Single<FirebaseUser> user(FirebaseAuth firebaseAuth) {
        return Single.fromCallable(firebaseAuth::getCurrentUser);
    }

    public static Single<FirebaseUser> signInAnonymously(FirebaseAuth firebaseAuth) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<FirebaseUser>>() {
            @Override public void call(AsyncEmitter<FirebaseUser> tAsyncEmitter) {
                firebaseAuth.signInAnonymously().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tAsyncEmitter.onNext(task.getResult().getUser());
                        tAsyncEmitter.onCompleted();
                    } else {
                        tAsyncEmitter.onError(task.getException());
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.LATEST).toSingle();
    }
}
