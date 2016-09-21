package in.nyuyu.android.style;

import java.util.List;

/**
 * Created by Vinay on 20/09/16.
 */
public interface LikedStyleListView {
    void showLoading();
    void showStyles(List<StyleListItem> items);
    void showEmpty();
    void showTimedOut();
    void showError();
}
