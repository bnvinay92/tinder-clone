package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.SessionListener;

import javax.inject.Inject;

import butterknife.OnClick;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.style.services.DigitsAuthenticationService;
import in.nyuyu.android.style.services.FacebookAuthenticationService;
import timber.log.Timber;

public class TestActivity extends NyuyuActivity {

    @Inject DigitsAuthenticationService service;
    @Inject FacebookAuthenticationService facebookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ((Nyuyu) getApplication()).component().inject(this);
        facebookService.signOut();
        Digits.addSessionListener(new SessionListener() {
            @Override public void changed(DigitsSession newSession) {
                Timber.d("SessionListener: %s", newSession.toString());
            }
        });

    }

    @OnClick(R.id.test_button_otp)
    public void otp() {
        if (service.isUserAuthenticated()) {
            Toast.makeText(this, "Phone number already registered: " + service.phoneNumber(), Toast.LENGTH_SHORT).show();
        } else {
            service.session()
                    .flatMap(session -> service.update(session).toObservable())
                    .subscribe(aVoid -> {
                            },
                            throwable -> Timber.e(throwable, throwable.getMessage()),
                            () -> Timber.d("Completed"));
        }

    }

    @OnClick(R.id.test_button_facebook)
    public void facebook() {
        if (facebookService.isUserAuthenticated()) {
            Toast.makeText(this, "Already authenticated", Toast.LENGTH_SHORT).show();
        } else {
            facebookService.authenticate(this)
                    .flatMap(accessToken -> Rx.signInWithFacebook(accessToken).toObservable())
                    .subscribe(aVoid -> {
                            },
                            throwable -> Timber.e(throwable, throwable.getMessage()),
                            () -> Timber.d("Completed"));
        }
    }

    @OnClick(R.id.test_button_digits_clear)
    public void clearDigits() {
        service.signOut();
    }

    @OnClick(R.id.test_button_facebook_clear)
    public void clearFacebook() {
        facebookService.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookService.onActivityResult(requestCode, resultCode, data);
    }
}
