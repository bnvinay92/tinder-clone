package in.nyuyu.android;

import javax.inject.Singleton;

import dagger.Component;
import in.nyuyu.android.style.StyleListActivity;

/**
 * Created by Vinay on 20/09/16.
 */
@Singleton
@Component(modules = {NyuyuModule.class})
public interface NyuyuComponent {
    void inject(StyleListActivity target);
}
