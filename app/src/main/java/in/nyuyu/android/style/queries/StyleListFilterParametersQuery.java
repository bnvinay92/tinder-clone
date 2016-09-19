package in.nyuyu.android.style.queries;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Inject;

import in.nyuyu.android.style.values.HairLength;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListFilterParametersQuery {

    private static final String KEY = "%s_stylefilterparameters";
    private final Preference<HairLength> hairLengthPreference;

    @Inject public StyleListFilterParametersQuery(RxSharedPreferences rxSharedPreferences) {
        this.hairLengthPreference = rxSharedPreferences.getEnum(HairLength.KEY, HairLength.class);
    }

    public Observable<String> execute(String userId) {
        return hairLengthPreference.asObservable()
                .map(hairLength -> hairLength == HairLength.SHORT ? "male_18-30_short_cut_diamond" : "male_18-30_short_cut_oval");
    }
}
