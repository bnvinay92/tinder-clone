package in.nyuyu.android;

import android.app.Application;

import jonathanfinerty.once.Once;
import timber.log.Timber;

/**
 * Created by Vinay on 19/09/16.
 */
public class Nyuyu extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Once.initialise(this);
    }
}
