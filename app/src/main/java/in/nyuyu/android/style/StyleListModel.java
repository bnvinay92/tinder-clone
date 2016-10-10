package in.nyuyu.android.style;

import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LastSeenStyleIdQuery;
import in.nyuyu.android.style.queries.LikedStyleIdSetQuery;
import in.nyuyu.android.style.queries.StyleListFilterParametersQuery;
import in.nyuyu.android.style.queries.StyleListQuery;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static in.nyuyu.android.style.StyleListViewModel.create;
import static in.nyuyu.android.style.StyleListViewState.EMPTY;
import static in.nyuyu.android.style.StyleListViewState.ERROR;
import static in.nyuyu.android.style.StyleListViewState.LOADED;
import static in.nyuyu.android.style.StyleListViewState.LOADING;
import static in.nyuyu.android.style.StyleListViewState.TIMED_OUT;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListModel {

    public static final long TIMEOUT = 30L;
    private final StyleListFilterParametersQuery parametersQuery;
    private final CurrentUserQuery userQuery;
    private final LastSeenStyleIdQuery lastSeenStyleIdQuery;
    private final LikedStyleIdSetQuery likedStyleIdSetQuery;
    private final StyleListQuery styleListQuery;

    private Subscription subscription = Subscriptions.unsubscribed();
    private PublishRelay<StyleListViewModel> relay = PublishRelay.create();

    @Inject public StyleListModel(CurrentUserQuery userQuery,
                                  StyleListFilterParametersQuery parametersQuery,
                                  LastSeenStyleIdQuery lastSeenStyleIdQuery,
                                  LikedStyleIdSetQuery likedStyleIdSetQuery,
                                  StyleListQuery styleListQuery) {
        this.parametersQuery = parametersQuery;
        this.userQuery = userQuery;
        this.lastSeenStyleIdQuery = lastSeenStyleIdQuery;
        this.likedStyleIdSetQuery = likedStyleIdSetQuery;
        this.styleListQuery = styleListQuery;
    }

    public Observable<StyleListViewModel> viewStates() {
        if (subscription.isUnsubscribed()) {
            subscription = userQuery.execute()
                    .map(FirebaseUser::getUid)
                    .flatMapObservable(
                            userId -> parametersQuery.execute(userId)
                                    .switchMap(styleListFilterParameters -> lastSeenStyleIdQuery.execute(userId, styleListFilterParameters)
                                            .toObservable()
                                            .flatMap(startAtStyleId -> Observable.zip(
                                                    styleListQuery.execute(styleListFilterParameters, startAtStyleId).toObservable(),
                                                    likedStyleIdSetQuery.execute(userId).toObservable(),
                                                    (styles, likedStyles) -> {
                                                        Iterator<StyleListItem> iterator = styles.iterator();
                                                        while (iterator.hasNext()) {
                                                            if (likedStyles.contains(iterator.next().getId())) {
                                                                iterator.remove();
                                                            }
                                                        }
                                                        return styles;
                                                    })
                                                    .map(styles -> styles.isEmpty() ? create(EMPTY) : create(LOADED, styles)))
                                            .timeout(() -> Observable.timer(TIMEOUT, TimeUnit.SECONDS), styles -> Observable.never(), Observable.just(create(TIMED_OUT)))
                                            .startWith(create(LOADING))))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            relay,
                            throwable -> {
                                relay.call(create(ERROR));
                                Timber.e(throwable, throwable.getMessage());
                            });
        }
        return relay;
    }

    public void stop() {
        subscription.unsubscribe();
    }
}
