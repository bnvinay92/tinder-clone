package in.nyuyu.android.style;

import java.util.List;

/**
 * Created by Vinay on 20/09/16.
 */
public interface StyleListView {
    void clearCards();
    void showLoading();
    void showCards(List<StyleListItem> items);
    void showEmpty();
    void showTimedOut();
    void showError();
}
