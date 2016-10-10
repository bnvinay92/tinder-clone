package in.nyuyu.android.style.values;

import in.nyuyu.android.R;

/**
 * Created by Vinay on 20/09/16.
 */
public enum HairLength {

    ALL(R.id.drawer_stylelist_rbutton_all),
    SHORT(R.id.drawer_stylelist_rbutton_short),
    MEDIUM(R.id.drawer_stylelist_rbutton_medium),
    LONG(R.id.drawer_stylelist_rbutton_long);

    public static final String KEY = "preference_hairlength";

    HairLength(int resId) {
        this.resId = resId;
    }

    private final int resId;

    public int resId() {
        return resId;
    }

    public static HairLength fromResId(Integer resId) {
        switch (resId) {
            case R.id.drawer_stylelist_rbutton_all:
                return ALL;
            case R.id.drawer_stylelist_rbutton_short:
                return SHORT;
            case R.id.drawer_stylelist_rbutton_medium:
                return MEDIUM;
            case R.id.drawer_stylelist_rbutton_long:
                return LONG;
            default:
                return null;
        }
    }
}
