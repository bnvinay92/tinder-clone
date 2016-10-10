package in.nyuyu.android.style.values;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 24/09/16.
 */
public enum FilterTheme {
    CASUAL(R.id.drawer_stylelist_rbutton_casual),
    FORMAL(R.id.drawer_stylelist_rbutton_formal);
    public static final String KEY = "preference_theme";

    private final int resId;

    FilterTheme(int resId) {
        this.resId = resId;
    }

    public int resId() {
        return resId;
    }

    public static FilterTheme fromResId(Integer resId) {
        switch (resId) {
            case R.id.drawer_stylelist_rbutton_formal:
                return FORMAL;
            case R.id.drawer_stylelist_rbutton_casual:
                return CASUAL;
            default:
                return null;
        }
    }
}
