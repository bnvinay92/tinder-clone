package in.nyuyu.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import hu.supercluster.paperwork.Paperwork;
import in.nyuyu.android.commons.CrashlyticsTree;
import io.fabric.sdk.android.Fabric;
import jonathanfinerty.once.Once;
import timber.log.Timber;

/**
 * Created by Vinay on 19/09/16.
 */
public class Nyuyu extends Application {

    public static final String GIT_SHA = "git_sha";
    public static final String BUILD_TIME = "build_time";
    public static final String TWITTER_KEY = "twitter_key";
    public static final String TWITTER_SECRET = "twitter_secret";

    private NyuyuComponent component;

    @Override public void onCreate() {
        super.onCreate();
        Paperwork paperwork = new Paperwork(this);

        if (BuildConfig.DEBUG) {
            String twitterKey = "DfqS6nWYsELasdshenSMCal3p";
            String twitterSecret = "o3aIv5RsgZ1NLo8XGpKUnL4Vp7ntjhuYaOe865FYYyANNWBKz1";
            TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
            Timber.plant(new Timber.DebugTree());
        } else {
            String twitterKey = paperwork.get(TWITTER_KEY);
            String twitterSecret = paperwork.get(TWITTER_SECRET);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Crashlytics());
            String buildTime = paperwork.get("build_time");
            String gitSha = paperwork.get("git_sha");
            Crashlytics.setString(GIT_SHA, gitSha);
            Crashlytics.setString(BUILD_TIME, buildTime);
            Crashlytics.setInt("version_code", BuildConfig.VERSION_CODE);
            Timber.plant(new CrashlyticsTree());
        }
        FacebookSdk.sdkInitialize(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        initComponent();
        Once.initialise(this);
    }

    public NyuyuComponent component() {
        if (component == null) {
            initComponent();
        }
        return component;
    }

    private void initComponent() {
        component = DaggerNyuyuComponent.builder()
                .nyuyuModule(new NyuyuModule(this))
                .build();
    }
}
