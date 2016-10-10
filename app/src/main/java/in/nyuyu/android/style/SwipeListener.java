package in.nyuyu.android.style;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LastSeenStyleIdQuery;
import in.nyuyu.android.style.queries.LikeCountUpdater;
import in.nyuyu.android.style.queries.LikedStyleListQuery;
import in.nyuyu.android.style.queries.StyleListFilterParametersQuery;
import in.nyuyu.android.style.services.LikeCountTransaction;
import in.nyuyu.android.style.values.Swipe;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by Vinay on 20/09/16.
 */
@Singleton
public class SwipeListener {

    private SwipeEventFactory eventFactory;
    private Subscription subscription = Subscriptions.unsubscribed();

    private final StyleListFilterParametersQuery parametersQuery;
    private final CurrentUserQuery userQuery;
    private final LastSeenStyleIdQuery lastSeenStyleIdQuery;
    private final LikedStyleListQuery likedStyleListQuery;
    private final LikeCountTransaction likeCountTransaction;
    private final LikeCountUpdater likeCountUpdater;

    @Inject public SwipeListener(StyleListFilterParametersQuery parametersQuery,
                                 CurrentUserQuery userQuery,
                                 LastSeenStyleIdQuery lastSeenStyleIdQuery, LikedStyleListQuery likedStyleListQuery, LikeCountTransaction likeCountTransaction, LikeCountUpdater likeCountUpdater) {
        this.parametersQuery = parametersQuery;
        this.userQuery = userQuery;
        this.lastSeenStyleIdQuery = lastSeenStyleIdQuery;
        this.likedStyleListQuery = likedStyleListQuery;
        this.likeCountTransaction = likeCountTransaction;
        this.likeCountUpdater = likeCountUpdater;
    }


    public void attachView(SwipeEventFactory eventFactory) {
        this.eventFactory = eventFactory;
        subscription = userQuery.execute()
                .map(FirebaseUser::getUid)
                .toObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .switchMap(userId -> parametersQuery.execute(userId)
                        .concatMap(styleListFilterParameters -> eventFactory.swipeIntents()
                                .doOnNext(swipe -> lastSeenStyleIdQuery.update(userId, styleListFilterParameters, swipe))
                                .filter(Swipe::getLiked)
                                .map(Swipe::getItem)
                                .doOnNext(item -> likedStyleListQuery.update(userId, item))
                                .map(StyleListItem::getId)))
                .concatMap(styleId -> likeCountTransaction.execute(styleId)
                        .retry())
                .subscribe(likeCountUpdater::execute,
                        throwable -> Timber.e(throwable, throwable.getMessage()));
    }

    public void detachView(boolean finishing) {
        subscription.unsubscribe();
    }
}
