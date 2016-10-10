package in.nyuyu.android.salons.values;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Vinay on 02/10/16.
 */

public class SalonQueryParameters implements Parcelable {
    public static final String EXTRA = "in.nyuyu.android.extras.EXTRA_SALON_PARAMS";
    private final String styleId;

    public SalonQueryParameters(String styleId) {
        this.styleId = styleId;
    }

    @Nullable public String getStyleId() {
        return styleId;
    }

    protected SalonQueryParameters(Parcel in) {
        styleId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(styleId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SalonQueryParameters> CREATOR = new Parcelable.Creator<SalonQueryParameters>() {
        @Override
        public SalonQueryParameters createFromParcel(Parcel in) {
            return new SalonQueryParameters(in);
        }

        @Override
        public SalonQueryParameters[] newArray(int size) {
            return new SalonQueryParameters[size];
        }
    };
}
