package in.nyuyu.android.style;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListPresenter {

    private StyleListView view;
    private Subscription subscription;

    private final StyleListModel model;

    @Inject public StyleListPresenter(StyleListModel model) {
        this.model = model;
    }

    public void attachView(StyleListView activity) {
        this.view = activity;
        subscription = model.viewStates().subscribe(viewState -> {
            switch (viewState.getState()) {
                case LOADING:
                    view.clearCards();
                    view.showLoading();
                    break;
                case LOADED:
                    view.showCards(viewState.getItems());
                    break;
                case EMPTY:
                    view.showEmpty();
                    break;
                case TIMED_OUT:
                    view.showTimedOut();
                    break;
                case ERROR:
                    view.showError();
            }
        });
    }

    public void detachView(boolean finishing) {
        this.view = null;
        subscription.unsubscribe();
        if (finishing) {
            model.stop();
        }
    }
}
