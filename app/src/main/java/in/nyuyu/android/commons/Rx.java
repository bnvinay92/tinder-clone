package in.nyuyu.android.commons;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

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

    public static Observable<DataSnapshot> values(Query query) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<DataSnapshot>>() {
            @Override public void call(AsyncEmitter<DataSnapshot> dataSnapshotAsyncEmitter) {
                ValueEventListener listener = new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshotAsyncEmitter.onNext(dataSnapshot);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {
                        dataSnapshotAsyncEmitter.onError(databaseError.toException());
                    }
                };
                dataSnapshotAsyncEmitter.setCancellation(() -> query.removeEventListener(listener));
                query.addValueEventListener(listener);
            }
        }, AsyncEmitter.BackpressureMode.BUFFER);
    }

    public static Single<DataSnapshot> once(Query query) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<DataSnapshot>>() {
            @Override public void call(AsyncEmitter<DataSnapshot> dataSnapshotAsyncEmitter) {
                ValueEventListener listener = new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshotAsyncEmitter.onNext(dataSnapshot);
                        dataSnapshotAsyncEmitter.onCompleted();
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {
                        dataSnapshotAsyncEmitter.onError(databaseError.toException());
                    }
                };
                dataSnapshotAsyncEmitter.setCancellation(() -> query.removeEventListener(listener));
                query.addListenerForSingleValueEvent(listener);
            }
        }, AsyncEmitter.BackpressureMode.LATEST).toSingle();
    }

    public static Single<DataSnapshot> transact(DatabaseReference databaseReference, TransactionExecutor handler) {
        return Observable.<DataSnapshot>fromEmitter(emitter -> databaseReference.runTransaction(new Transaction.Handler() {
            @Override public Transaction.Result doTransaction(MutableData mutableData) {
                return handler.execute(mutableData);
            }

            @Override public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (committed) {
                    emitter.onNext(dataSnapshot);
                    emitter.onCompleted();
                } else {
                    if (databaseError != null) {
                        emitter.onError(databaseError.toException());
                    } else {
                        emitter.onError(new Throwable("Transaction did not commit"));
                    }
                }
            }
        }), AsyncEmitter.BackpressureMode.LATEST).toSingle();
    }

    public interface TransactionExecutor {
        Transaction.Result execute(MutableData mutableData);
    }
}
