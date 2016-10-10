package in.nyuyu.android.style.services;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import in.nyuyu.android.commons.Rx;
import rx.AsyncEmitter;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Vinay on 27/09/16.
 */

public class DigitsAuthenticationService {

    private final DatabaseReference databaseReference;

    @Inject public DigitsAuthenticationService(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference.child("users");
    }

    public Observable<DigitsSession> session() {
        return Observable.<DigitsSession>fromEmitter(emitter -> {
            AuthCallback authCallback = new AuthCallback() {
                @Override public void success(DigitsSession session, String phoneNumber) {
                    Timber.d("On next authcallback");
                    emitter.onNext(session);
                    emitter.onCompleted();
                }

                @Override public void failure(DigitsException error) {
                    emitter.onError(error);
                }
            };
            Digits.authenticate(new AuthConfig.Builder()
                    .withPhoneNumber("+91")
                    .withAuthCallBack(authCallback)
                    .build());
            Timber.d("Called authenticate");
        }, AsyncEmitter.BackpressureMode.BUFFER)
                .doOnNext(session -> Timber.d("Session: %s", session));

    }

    public Completable update(DigitsSession session) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", session.getPhoneNumber());
        payload.put("digitsId", session.getId());
        Timber.d("Payload created");
        return Rx.set(databaseReference.child(userId), payload);
    }

    public boolean isUserAuthenticated() {
        return Digits.getActiveSession() != null;
    }

    public String phoneNumber() {
        return Digits.getActiveSession().getPhoneNumber();
    }

    public void signOut() {
        Digits.clearActiveSession();
    }
}
