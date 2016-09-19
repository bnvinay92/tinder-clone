package in.nyuyu.android.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.commons.Rx;
import in.nyuyu.android.commons.Scopes;
import in.nyuyu.android.onboarding.OnboardingActivity;
import in.nyuyu.android.style.StyleListActivity;
import jonathanfinerty.once.Once;
import rx.Single;
import timber.log.Timber;

public class SplashActivity extends NyuyuActivity {

    FirebaseAuth firebaseAuth;
    @BindView(R.id.activity_splash) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        Rx.user(firebaseAuth)
                .flatMap(user -> user == null ? Rx.signInAnonymously(firebaseAuth) : Single.just(user))
                .map(FirebaseUser::getUid)
                .subscribe(
                        userId -> {
                            if (Once.beenDone(Once.THIS_APP_INSTALL, Scopes.onboarding(userId))) {
                                startActivity(new Intent(this, StyleListActivity.class));
                            } else {
                                FirebaseDatabase.getInstance().getReference("sessions").child(userId).keepSynced(true);
                                startActivity(new Intent(this, OnboardingActivity.class));
                            }
                            finish();
                        },
                        throwable -> {
                            if (throwable.getClass().equals(FirebaseNetworkException.class)) {
                                setContentView(R.layout.activity_splash);
                                Snackbar.make(coordinatorLayout, "You are offline x_x", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Retry", v -> recreate())
                                        .show();
                            } else {
                                Timber.e(throwable, throwable.getMessage());
                            }

                        });
    }
}
