package in.nyuyu.android.style.queries;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;

import in.nyuyu.android.style.values.FilterTheme;
import in.nyuyu.android.style.values.Gender;
import in.nyuyu.android.style.values.HairLength;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListFilterParametersQuery {

    private static final String KEY = "%s_stylefilterparameters";
    private final Preference<HairLength> hairLengthPreference;
    private final Preference<Gender> genderPreference;
    private final Preference<FilterTheme> themePreference;

    @Inject public StyleListFilterParametersQuery(Preference<HairLength> hairLengthPreference, Preference<Gender> genderPreference, Preference<FilterTheme> themePreference) {
        this.hairLengthPreference = hairLengthPreference;
        this.genderPreference = genderPreference;
        this.themePreference = themePreference;
    }

    public Observable<String> execute(String userId) {
        return Observable.combineLatest(hairLengthPreference.asObservable(),
                genderPreference.asObservable(),
                themePreference.asObservable(),
                ((hairLength, gender, filterTheme) -> {
                    if (hairLength == null || gender == null || filterTheme == null) {
                        return "";
                    } else {
                        return hairLength.name().toLowerCase() + "_" +
                                filterTheme.name().toLowerCase() + "_" +
                                gender.name().toLowerCase();
                    }
                }))
                .filter(parameters -> !parameters.isEmpty())
                .distinctUntilChanged()
                .share();
    }

    public String get() {
        HairLength hairLength = hairLengthPreference.get();
        Gender gender = genderPreference.get();
        FilterTheme filterTheme = themePreference.get();

        if (hairLength == null || gender == null || filterTheme == null) {
            return "";
        } else {
            return hairLength.name().toLowerCase() + "_" +
                    filterTheme.name().toLowerCase() + "_" +
                    gender.name().toLowerCase();
        }
    }
}
