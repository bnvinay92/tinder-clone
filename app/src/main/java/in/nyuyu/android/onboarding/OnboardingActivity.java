package in.nyuyu.android.onboarding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import in.nyuyu.android.R;
import in.nyuyu.android.commons.Scopes;
import jonathanfinerty.once.Once;

public class OnboardingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        firebaseAuth = FirebaseAuth.getInstance();
        Once.markDone(Scopes.onboarding(firebaseAuth.getCurrentUser().getUid()));
    }
}
