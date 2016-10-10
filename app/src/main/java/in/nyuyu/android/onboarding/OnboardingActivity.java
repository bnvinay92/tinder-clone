package in.nyuyu.android.onboarding;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import in.nyuyu.android.R;
import in.nyuyu.android.commons.Scopes;
import in.nyuyu.android.style.StyleListActivity;
import jonathanfinerty.once.Once;

public class OnboardingActivity extends IntroActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        Once.markDone(Scopes.onboarding(firebaseAuth.getCurrentUser().getUid()));
        setSkipEnabled(false);
        setFullscreen(true);
        setFinishEnabled(true);
        addSlide(new SimpleSlide.Builder()
                .title("Explore")
                .description("Hairstyles by preference")
                .background(R.color.colorPrimary)
                .backgroundDark(android.R.color.black)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Save")
                .description("Shortlist the ones you like")
                .background(R.color.colorPrimary)
                .backgroundDark(android.R.color.black)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Learn")
                .description("Styling tips and which styles suit you best.")
                .background(R.color.colorPrimary)
                .backgroundDark(android.R.color.black)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Get the style")
                .description("At salons around you, exactly how you see it.")
                .background(R.color.colorPrimary)
                .backgroundDark(android.R.color.black)
                .build());
    }

    @Override public void finish() {
        super.finish();
        startActivity(new Intent(this,StyleListActivity.class));
    }
}
