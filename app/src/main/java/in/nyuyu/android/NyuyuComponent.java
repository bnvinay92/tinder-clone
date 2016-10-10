package in.nyuyu.android;

import javax.inject.Singleton;

import dagger.Component;
import in.nyuyu.android.salons.SalonListActivity;
import in.nyuyu.android.style.LikedStyleListActivity;
import in.nyuyu.android.style.StyleActivity;
import in.nyuyu.android.style.StyleListActivity;
import in.nyuyu.android.style.TestActivity;

/**
 * Created by Vinay on 20/09/16.
 */
@Singleton
@Component(modules = {NyuyuModule.class})
public interface NyuyuComponent {
    void inject(StyleListActivity target);
    void inject(LikedStyleListActivity target);
    void inject(TestActivity testActivity);
    void inject(SalonListActivity target);
    void inject(StyleActivity target);
}
