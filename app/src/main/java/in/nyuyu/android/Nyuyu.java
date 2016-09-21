package in.nyuyu.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;

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

    private NyuyuComponent component;

    @Override public void onCreate() {
        super.onCreate();
        Paperwork paperwork = new Paperwork(this);
        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            String buildTime = paperwork.get("build_time");
            String gitSha = paperwork.get("git_sha");
            Crashlytics.setString(GIT_SHA, gitSha);
            Crashlytics.setString(BUILD_TIME, buildTime);
            Timber.plant(new CrashlyticsTree());
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
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
