package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
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
import in.nyuyu.android.style.values.HairLength;
import in.nyuyu.android.style.values.Swipe;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class StyleListActivity extends NyuyuActivity implements CardStackView.CardStackEventListener,
        Toolbar.OnMenuItemClickListener, DrawerLayout.DrawerListener,
        StyleListView, SwipeEventFactory {

    @BindView(R.id.stylelist_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.stylelist_toolbar) Toolbar toolbar;
    @BindView(R.id.stylelist_cardstack) CardStackView cards;
    @BindView(R.id.drawer_stylelist_rgroup_hairlength) RadioGroup hairLength;

    @Inject StyleListPresenter listPresenter;
    @Inject SwipeListener swipeListener;
    @Inject NetworkStateQuery networkStateQuery;
    @Inject RxSharedPreferences rxSharedPreferences;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private StyleListAdapter adapter;
    private Preference<HairLength> hairLengthPreference;
    private PublishRelay<Swipe> swipeIntents = PublishRelay.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_list);
        ((Nyuyu) getApplication()).component().inject(this);
        initToolbar();
        initCardStack();
        initDrawer();
    }

    private void initDrawer() {
        drawerLayout.addDrawerListener(this);
        hairLengthPreference = rxSharedPreferences.getEnum(HairLength.KEY, HairLength.class);
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.stylelist);
        toolbar.setOnMenuItemClickListener(this);
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
        subscriptions.add(RxRadioGroup.checkedChanges(hairLength)
                .filter(resId -> resId != -1)
                .subscribe(resId -> {
                    if (resId == R.id.drawer_stylelist_rbutton_short) {
                        hairLengthPreference.set(HairLength.SHORT);
                    } else if (resId == R.id.drawer_stylelist_rbutton_medium) {
                        hairLengthPreference.set(HairLength.MEDIUM);
                    }
                }));
        listPresenter.attachView(this);
        swipeListener.attachView(this);
    }

    @Override protected void onStop() {
        super.onStop();
        swipeListener.detachView(isFinishing());
        listPresenter.detachView(isFinishing());
        subscriptions.clear();
    }

    @Override public void onDrag(float percentage) {
        Timber.d("Percentage: %s", percentage);
    }

    @Override public void onSwipe(int index, Direction direction) {
        Boolean liked = direction == Direction.BottomRight || direction == Direction.TopRight;
        swipeIntents.call(new Swipe(adapter.getItem(index), liked));
    }

    @Override public void onSwipeDenied(Direction direction) {
        Toast.makeText(this, "Requires network connectivity", Toast.LENGTH_SHORT).show();
    }

    @Override public void onTapUp(int index) {

    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stylelist_filter:
                openDrawer();
                return true;
            case R.id.menu_stylelist_shortlist:
                startActivity(new Intent(this, LikedStyleListActivity.class));
                return true;
            default:
                throw new IllegalStateException("Unidentified menu item " + item.getItemId());
        }
    }

    @OnClick(R.id.drawer_stylelist_ibutton_close)
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override public void clearCards() {
        adapter.clear();
        cards.init(true);
    }

    @Override public void showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override public void showCards(List<StyleListItem> items) {
        cards.init(false);
        adapter.addAll(items);
    }

    @Override public void showEmpty() {
        Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
    }

    @Override public void showTimedOut() {
        Toast.makeText(this, "TimedOut", Toast.LENGTH_SHORT).show();
    }

    @Override public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {
        swipeListener.detachView(false);
    }

    @Override public void onDrawerClosed(View drawerView) {
        swipeListener.attachView(this);
    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    @Override public Observable<Swipe> swipeIntents() {
        return swipeIntents.asObservable();
    }

}

