package in.nyuyu.android.style;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListPresenter {

    private StyleListView view;
    private Subscription subscription;

    @Inject public StyleListPresenter() {
    }

    public void attachView(StyleListView activity) {
        this.view = activity;
    }

    public void detachView(boolean finishing) {
        this.view = null;
    }
}
