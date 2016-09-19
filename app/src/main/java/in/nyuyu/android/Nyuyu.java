package in.nyuyu.android;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import hu.supercluster.paperwork.Paperwork;
import jonathanfinerty.once.Once;
import timber.log.Timber;

/**
 * Created by Vinay on 19/09/16.
 */
public class Nyuyu extends Application {

    private NyuyuComponent component;

    @Override public void onCreate() {
        super.onCreate();
        Paperwork paperwork = new Paperwork(this);
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Timber.plant(new Timber.DebugTree());
        } else {
            String buildTime = paperwork.get("build_time");
            String gitSha = paperwork.get("git_sha");
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
