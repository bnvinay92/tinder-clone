package in.nyuyu.android.style.values;

import in.nyuyu.android.style.StyleListItem;

/**
 * Created by Vinay on 20/09/16.
 */
public class Swipe {
    private StyleListItem item;
    private Boolean liked;

    public Swipe() {
    }

    public Swipe(StyleListItem item, boolean liked) {
        this.item = item;
        this.liked = liked;
    }

    public StyleListItem getItem() {
        return item;
    }

    public Boolean getLiked() {
        return liked;
    }
}
