package in.nyuyu.android.style;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LikedStyleListQuery;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.nyuyu.android.style.values.LikedStyleListViewModel.create;
import static in.nyuyu.android.style.values.LikedStyleListViewState.EMPTY;
import static in.nyuyu.android.style.values.LikedStyleListViewState.LOADING;

/**
 * Created by Vinay on 20/09/16.
 */
public class LikedStyleListPresenter {

    public static final long TIMEOUT = 30L;
    private final LikedStyleListQuery listQuery;
    private final CurrentUserQuery userQuery;
    private LikedStyleListView view;
    private Subscription subscription;

    @Inject public LikedStyleListPresenter(LikedStyleListQuery listQuery,
                                           CurrentUserQuery userQuery) {
        this.listQuery = listQuery;
        this.userQuery = userQuery;
    }


    public void attachView(LikedStyleListView activity) {
        this.view = activity;
        subscription = userQuery.execute()
                .map(FirebaseUser::getUid)
                .flatMapObservable(userId -> listQuery.execute(userId)
                        .map(items -> items.isEmpty() ? create(EMPTY) : create(items))
                        .timeout(TIMEOUT, TimeUnit.SECONDS)
                        .startWith(create(LOADING)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewModel -> {
                            switch (viewModel.getState()) {
                                case LOADING:
                                    view.showLoading();
                                    break;
                                case LOADED:
                                    view.showStyles(viewModel.getItems());
                                    break;
                                case EMPTY:
                                    view.showEmpty();
                                    break;
                                case TIMED_OUT:
                                    view.showTimedOut();
                            }
                        },
                        throwable -> {
                            view.showError();
                            Timber.e(throwable, throwable.getMessage());
                        });
    }


    public void detachView() {
        subscription.unsubscribe();
    }
}
