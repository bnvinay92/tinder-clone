package in.nyuyu.android.style;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LastSeenStyleIdQuery;
import in.nyuyu.android.style.queries.StyleListFilterParametersQuery;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Vinay on 20/09/16.
 */
@Singleton
public class SwipeListener {

    private SwipeEventFactory eventFactory;
    private Subscription subscription;

    private final StyleListFilterParametersQuery parametersQuery;
    private final CurrentUserQuery userQuery;
    private final LastSeenStyleIdQuery lastSeenStyleIdQuery;

    @Inject public SwipeListener(StyleListFilterParametersQuery parametersQuery,
                                 CurrentUserQuery userQuery,
                                 LastSeenStyleIdQuery lastSeenStyleIdQuery) {
        this.parametersQuery = parametersQuery;
        this.userQuery = userQuery;
        this.lastSeenStyleIdQuery = lastSeenStyleIdQuery;
    }

    public void attachView(SwipeEventFactory eventFactory) {
        this.eventFactory = eventFactory;
        subscription = userQuery.execute()
                .map(FirebaseUser::getUid)
                .toObservable()
                .concatMap(userId -> Observable.combineLatest(
                        eventFactory.swipeIntents(),
                        parametersQuery.execute(userId),
                        (swipe, styleListFilterParameters) -> {
                            lastSeenStyleIdQuery.update(userId, styleListFilterParameters, swipe);
                            return swipe;
                        }))
                .subscribe(
                        swipe -> {
                            //TODO Analytics
                        },
                        throwable -> Timber.e(throwable, throwable.getMessage()));
    }

    public void detachView(boolean finishing) {
        subscription.unsubscribe();
    }
}
