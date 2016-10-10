package in.nyuyu.android.style.services;

import android.app.Activity;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import javax.inject.Inject;

import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by Vinay on 27/09/16.
 */
public class FacebookAuthenticationService {

    private final CallbackManager callbackManager;
    private final LoginManager loginManager;

    @Inject public FacebookAuthenticationService() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public Observable<AccessToken> authenticate(Activity activity) {
        return Observable.fromEmitter(emitter -> {
            FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
                @Override public void onSuccess(LoginResult loginResult) {
                    emitter.onNext(loginResult.getAccessToken());
                    emitter.onCompleted();
                }

                @Override public void onCancel() {
                    emitter.onCompleted();
                }

                @Override public void onError(FacebookException error) {
                    emitter.onError(error);
                }
            };
            loginManager.registerCallback(callbackManager, callback);
            loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
        }, AsyncEmitter.BackpressureMode.LATEST);
    }

    public boolean isUserAuthenticated() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void signOut() {
        loginManager.logOut();
    }
}
