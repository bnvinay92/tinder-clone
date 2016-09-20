package in.nyuyu.android.style;

import in.nyuyu.android.style.values.Swipe;
import rx.Observable;

/**
 * Created by Vinay on 20/09/16.
 */
public interface SwipeEventFactory {
    Observable<Swipe> swipeIntents();
}
