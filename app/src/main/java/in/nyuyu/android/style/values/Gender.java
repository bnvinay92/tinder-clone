package in.nyuyu.android.style.values;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 20/09/16.
 */
public enum Gender {
    MEN(R.id.drawer_stylelist_rbutton_male),
    WOMEN(R.id.drawer_stylelist_rbutton_female);

    public static final String KEY = "preference_gender";
    private final int resId;

    Gender(int resId) {
        this.resId = resId;
    }

    public int resId() {
        return resId;
    }

    public static Gender fromResId(Integer resId) {
        switch (resId) {
            case R.id.drawer_stylelist_rbutton_male:
                return MEN;
            case R.id.drawer_stylelist_rbutton_female:
                return WOMEN;
            default:
                return null;
        }
    }
}
