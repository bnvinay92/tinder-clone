package in.nyuyu.android.style;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f2prateek.rx.preferences.Preference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.cardstackview.CardStackView;
import in.nyuyu.android.cardstackview.Direction;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.commons.queries.NetworkStateQuery;
import in.nyuyu.android.style.values.FilterTheme;
import in.nyuyu.android.style.values.Gender;
import in.nyuyu.android.style.values.HairLength;
import in.nyuyu.android.style.values.Swipe;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class StyleListActivity extends NyuyuActivity implements CardStackView.CardStackEventListener,
        Toolbar.OnMenuItemClickListener, DrawerLayout.DrawerListener,
        StyleListView, SwipeEventFactory, LikeCountView {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final String ITEM_COUNT_KEY = "items_count";

    @BindView(R.id.stylelist_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.stylelist_toolbar) Toolbar toolbar;
    @BindView(R.id.stylelist_cardstack) CardStackView cards;
    @BindView(R.id.stylelist_statusview) ImageView statusImageView;

    @BindView(R.id.drawer_stylelist_rgroup_hairlength) RadioGroup hairLength;
    @BindView(R.id.drawer_stylelist_rgroup_gender) RadioGroup gender;
    @BindView(R.id.drawer_stylelist_rgroup_theme) RadioGroup theme;

    @Inject StyleListPresenter listPresenter;
    @Inject LikeCountPresenter likeCountPresenter;
    @Inject SwipeListener swipeListener;
    @Inject NetworkStateQuery networkStateQuery;
    @Inject RefreshLastSeenStyleId reloadUsecase;
    @Inject Preference<HairLength> hairLengthPreference;
    @Inject Preference<Gender> genderPreference;
    @Inject Preference<FilterTheme> themePreference;

    private Drawable loadingDrawable;
    private Drawable reloadDrawable;
    private Drawable emptyDrawable;
    private Drawable timeOutDrawable;
    private Drawable errorDrawable;
    private Drawable emptyPreferencesDrawable;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private CompositeSubscription drawerSubscriptions = new CompositeSubscription();
    private StyleListAdapter adapter;
    private PublishRelay<Swipe> swipeIntents = PublishRelay.create();

    private int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDrawable = ContextCompat.getDrawable(this, R.drawable.ic_loading_black_24dp);
        reloadDrawable = ContextCompat.getDrawable(this, R.drawable.ic_reload_black_24dp);
        emptyDrawable = ContextCompat.getDrawable(this, R.drawable.ic_empty_black_24dp);
        timeOutDrawable = ContextCompat.getDrawable(this, R.drawable.ic_timeout_black_24dp);
        errorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_error_black_24dp);
        emptyPreferencesDrawable = ContextCompat.getDrawable(this, R.drawable.ic_empty_prefs_black_24dp);
        setContentView(R.layout.activity_style_list);
        ((Nyuyu) getApplication()).component().inject(this);
        initToolbar();
        initCardStack();
        initDrawer();
    }

    private void initDrawer() {
        drawerLayout.addDrawerListener(this);
        HairLength userHairLength = hairLengthPreference.get();
        Gender userGender = genderPreference.get();
        FilterTheme userFilterTheme = themePreference.get();
        if (userFilterTheme != null) {
            theme.check(themePreference.get().resId());
        }
        if (userHairLength != null) {
            hairLength.check(hairLengthPreference.get().resId());
        }
        if (userGender != null) {
            gender.check(genderPreference.get().resId());
        }
        if (userFilterTheme == null || userGender == null || userHairLength == null) {
            showEmptyPreferences();
            openDrawer();
        }
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.stylelist);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override public void setLikeCount(int badgeCount) {
        //TODO
    }

    private void initCardStack() {
        adapter = new StyleListAdapter(this);
        cards.setCardStackEventListener(this);
        cards.setAdapter(adapter);
    }

    @Override protected void onStart() {
        super.onStart();
        subscriptions.add(networkStateQuery.execute()
                .subscribe(
                        connected -> cards.setSwipeEnabled(connected),
                        throwable -> Timber.e(throwable, throwable.getMessage())));
        listPresenter.attachView(this);
        swipeListener.attachView(this);
        likeCountPresenter.attachView(this);
    }

    @Override protected void onStop() {
        super.onStop();
        likeCountPresenter.detachView();
        swipeListener.detachView(isFinishing());
        listPresenter.detachView(isFinishing());
        subscriptions.clear();
    }

    @Override public void onDrag(float percentage) {

    }

    @Override public void onSwipe(int index, Direction direction) {
        itemCount--;
        Boolean liked = direction == Direction.BottomRight || direction == Direction.TopRight;
        StyleListItem item = adapter.getItem(index);
        swipeIntents.call(new Swipe(item, liked));
        if (liked) {
            Glide.with(this)
                    .load(item.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .preload();
        }
    }

    @Override public void onSwipeDenied(Direction direction) {
        Toast.makeText(this, "Requires network connectivity", Toast.LENGTH_SHORT).show();
    }

    @Override public void onTapUp(int index) {
        StyleListItem item = adapter.getItem(index);
        Intent styleActivityStarter = new Intent(this, StyleActivity.class);
        styleActivityStarter.putExtra(StyleListItem.EXTRA, item);
        startActivity(styleActivityStarter);
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stylelist_filter:
                openDrawer();
                return true;
            case R.id.menu_stylelist_shortlist:
                startActivity(new Intent(this, LikedStyleListActivity.class));
                return true;
            case R.id.menu_stylelist_test:
                startActivity(new Intent(this, TestActivity.class));
                return true;
            default:
                throw new IllegalStateException("Unidentified menu item " + item.getItemId());
        }
    }

    @OnClick(R.id.stylelist_statusview)
    public void onStatusClick() {
        Drawable statusDrawable = statusImageView.getDrawable();
        if (itemCount == 0) {
            if (statusDrawable == emptyPreferencesDrawable) {
                openDrawer();
            } else if (statusDrawable != loadingDrawable) {
                if (statusDrawable == reloadDrawable || statusDrawable == emptyDrawable) {
                    reloadUsecase.execute();
                }
                listPresenter.detachView(true);
                listPresenter.attachView(this);
            }
        }

    }

    @OnClick(R.id.stylelist_fab_undo)
    public void undo() {
        StyleListItem item = (StyleListItem) cards.reverse();
        if (item != null) {
            itemCount++;
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference(String.format("sessions/%s/styles/liked/%s", userId, item.getId())).setValue(null);
        }
    }

    @OnClick(R.id.stylelist_fab_dislike)
    public void dislike() {
        if (itemCount > 0) {
            cards.discard(Direction.BottomLeft);
        }
    }

    @OnClick(R.id.stylelist_fab_like)
    public void like() {
        if (itemCount > 0) {
            cards.discard(Direction.TopRight);
        }
    }

    @OnClick(R.id.stylelist_fab_share)
    public void share() {

    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END);
        onDrawerOpened(drawerLayout);
    }

    @OnClick(R.id.drawer_stylelist_ibutton_close)
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END);
        onDrawerClosed(drawerLayout);
    }

    @Override public void clearCards() {
        itemCount = 0;
        adapter.clear();
        cards.init(true);
    }

    @Override public void showLoading() {
        statusImageView.setImageDrawable(loadingDrawable);
    }

    @Override public void showCards(List<StyleListItem> items) {
        statusImageView.setImageDrawable(reloadDrawable);
        itemCount = items.size();
        cards.init(false);
        adapter.addAll(items);
    }

    @Override public void showEmpty() {
        statusImageView.setImageDrawable(emptyDrawable);
    }

    private void showEmptyPreferences() {
        statusImageView.setImageDrawable(emptyPreferencesDrawable);
    }

    @Override public void showTimedOut() {
        statusImageView.setImageDrawable(timeOutDrawable);
    }

    @Override public void showError() {
        statusImageView.setImageDrawable(errorDrawable);
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {
        swipeListener.detachView(false);
        drawerSubscriptions.addAll(
                RxRadioGroup.checkedChanges(gender).map(Gender::fromResId).subscribe(genderPreference.asAction()),
                RxRadioGroup.checkedChanges(theme).map(FilterTheme::fromResId).subscribe(themePreference.asAction()),
                RxRadioGroup.checkedChanges(hairLength).map(HairLength::fromResId).subscribe(hairLengthPreference.asAction())
        );
    }

    @Override public void onDrawerClosed(View drawerView) {
        drawerSubscriptions.clear();
        swipeListener.attachView(this);
    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    @Override public Observable<Swipe> swipeIntents() {
        return swipeIntents.asObservable();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ITEM_COUNT_KEY, itemCount);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            itemCount = savedInstanceState.getInt(ITEM_COUNT_KEY, 0);
    }

    @OnClick(R.id.drawer_stylelist_button_close)
    public void drawerDone() {
        drawerLayout.addDrawerListener(this);
        HairLength userHairLength = hairLengthPreference.get();
        Gender userGender = genderPreference.get();
        FilterTheme userFilterTheme = themePreference.get();
        if (userFilterTheme == null || userGender == null || userHairLength == null) {
            Toast.makeText(this, "Please choose hairstyle preferences", Toast.LENGTH_SHORT).show();
        } else {
            closeDrawer();
        }
    }
}

