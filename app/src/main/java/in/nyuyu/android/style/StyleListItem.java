package in.nyuyu.android.style;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListItem implements Parcelable {
    public static final String EXTRA = "in.nyuyu.android.extras.EXTRA_STYLE_LIST_ITEM";
    public static final String EXTRA_ID = "in.nyuyu.android.extras.EXTRA_STYLE_ID";
    private Long id;
    private String imageUrl;
    private Long likeCount;
    private Long likeModified;
    private String name;

    public StyleListItem() {
    }

    public StyleListItem(Long id, String imageUrl, Long likeCount, Long likeModified, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.likeModified = likeModified;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getLikeModified() {
        return likeModified;
    }

    public String getName() {
        return name;
    }

    protected StyleListItem(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        imageUrl = in.readString();
        likeCount = in.readByte() == 0x00 ? null : in.readLong();
        likeModified = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(imageUrl);
        if (likeCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(likeCount);
        }
        if (likeModified == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(likeModified);
        }
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StyleListItem> CREATOR = new Parcelable.Creator<StyleListItem>() {
        @Override
        public StyleListItem createFromParcel(Parcel in) {
            return new StyleListItem(in);
        }

        @Override
        public StyleListItem[] newArray(int size) {
            return new StyleListItem[size];
        }
    };
}