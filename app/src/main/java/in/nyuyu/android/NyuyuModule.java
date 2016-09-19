package in.nyuyu.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.supercluster.paperwork.Paperwork;

/**
 * Created by Vinay on 20/09/16.
 */
@Module
public class NyuyuModule {

    private final Application application;

    public NyuyuModule(Application application) {
        this.application = application;
    }

    @Provides Context context() {
        return application;
    }

    @Provides @Singleton Paperwork paperwork(Context context) {
        return new Paperwork(context);
    }

    @Provides @Singleton SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton RxSharedPreferences rxPreferences(SharedPreferences sharedPreferences) {
        return RxSharedPreferences.create(sharedPreferences);
    }

    @Provides @Singleton FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides @Singleton DatabaseReference databaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

}
