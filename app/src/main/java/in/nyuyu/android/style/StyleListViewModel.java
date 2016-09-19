package in.nyuyu.android.style;

import java.util.List;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListViewModel {

    private StyleListViewState state;
    private List<StyleListItem> items;

    public StyleListViewModel(StyleListViewState state, List<StyleListItem> items) {
        this.state = state;
        this.items = items;
    }

    public StyleListViewState getState() {
        return state;
    }

    public List<StyleListItem> getItems() {
        return items;
    }

    public static StyleListViewModel create(StyleListViewState state) {
        return new StyleListViewModel(state, null);
    }

    public static StyleListViewModel create(StyleListViewState state, List<StyleListItem> items) {
        return new StyleListViewModel(state, items);
    }
}
