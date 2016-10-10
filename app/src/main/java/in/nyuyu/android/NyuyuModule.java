package in.nyuyu.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.algolia.search.saas.Client;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.supercluster.paperwork.Paperwork;
import in.nyuyu.android.style.values.FilterTheme;
import in.nyuyu.android.style.values.Gender;
import in.nyuyu.android.style.values.HairLength;

/**
 * Created by Vinay on 20/09/16.
 */
@Module
public class NyuyuModule {

    public static final String ALGOLIA_APP_ID = "algolia_app_id";
    public static final String ALGOLIA_API_KEY = "algolia_api_key";

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

    @Provides FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides DatabaseReference databaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides Preference<HairLength> hairLengthPreference(FirebaseAuth firebaseAuth, RxSharedPreferences rxSharedPreferences) {
        return rxSharedPreferences.getEnum(firebaseAuth.getCurrentUser().getUid() + "_" + HairLength.KEY, HairLength.class);
    }

    @Provides Preference<Gender> genderPreference(FirebaseAuth firebaseAuth, RxSharedPreferences rxSharedPreferences) {
        return rxSharedPreferences.getEnum(firebaseAuth.getCurrentUser().getUid() + "_" + Gender.KEY, Gender.class);
    }

    @Provides Preference<FilterTheme> filterThemePreference(FirebaseAuth firebaseAuth, RxSharedPreferences rxSharedPreferences) {
        return rxSharedPreferences.getEnum(firebaseAuth.getCurrentUser().getUid() + "_" + FilterTheme.KEY, FilterTheme.class);
    }

    @Provides Client client(Paperwork paperwork) {
        String applicationID = paperwork.get(ALGOLIA_APP_ID);
        String apiKey = paperwork.get(ALGOLIA_API_KEY);
        return new Client(applicationID, apiKey);
    }

}
