package in.nyuyu.android.style;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LikeCountQuery;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Vinay on 21/09/16.
 */
public class LikeCountPresenter {

    private LikeCountView view;
    private Subscription subscription;

    private final LikeCountQuery countQuery;
    private final CurrentUserQuery userQuery;

    @Inject public LikeCountPresenter(LikeCountQuery countQuery, CurrentUserQuery userQuery) {
        this.countQuery = countQuery;
        this.userQuery = userQuery;
    }

    public void attachView(LikeCountView activity) {
        this.view = activity;
        subscription = userQuery.execute()
                .map(FirebaseUser::getUid)
                .flatMapObservable(countQuery::execute)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setLikeCount,
                        throwable -> Timber.e(throwable, throwable.getMessage()));
    }

    public void detachView() {
        subscription.unsubscribe();
    }
}
