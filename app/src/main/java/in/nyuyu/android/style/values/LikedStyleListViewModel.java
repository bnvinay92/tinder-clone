package in.nyuyu.android.style.values;

import java.util.List;

import in.nyuyu.android.style.StyleListItem;

/**
 * Created by Vinay on 20/09/16.
 */
public class LikedStyleListViewModel {
    private LikedStyleListViewState state;
    private List<StyleListItem> items;

    private LikedStyleListViewModel(LikedStyleListViewState state, List<StyleListItem> items) {
        this.state = state;
        this.items = items;
    }

    public LikedStyleListViewState getState() {
        return state;
    }

    public List<StyleListItem> getItems() {
        return items;
    }

    public static LikedStyleListViewModel create(LikedStyleListViewState state) {
        return new LikedStyleListViewModel(state, null);
    }

    public static LikedStyleListViewModel create(List<StyleListItem> items) {
        return new LikedStyleListViewModel(LikedStyleListViewState.LOADED, items);
    }
}
