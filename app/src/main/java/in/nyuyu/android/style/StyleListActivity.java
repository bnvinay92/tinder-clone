package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

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
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class StyleListActivity extends NyuyuActivity implements CardStackView.CardStackEventListener, Toolbar.OnMenuItemClickListener, StyleListView {

    @BindView(R.id.stylelist_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.stylelist_toolbar) Toolbar toolbar;
    @BindView(R.id.stylelist_cardstack) CardStackView cards;
    @BindView(R.id.drawer_stylelist_rgroup_hairlength) RadioGroup hairLength;

    @Inject StyleListPresenter listPresenter;
    @Inject NetworkStateQuery networkStateQuery;
    @Inject RxSharedPreferences rxSharedPreferences;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private StyleListAdapter adapter;
    private Preference<HairLength> hairLengthPreference;

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
        hairLengthPreference = rxSharedPreferences.getEnum(HairLength.KEY, HairLength.class);
        RxRadioGroup.checkedChanges(hairLength)
                .filter(resId -> resId != -1)
                .subscribe(resId -> {
                    if (resId == R.id.drawer_stylelist_rbutton_short) {
                        hairLengthPreference.set(HairLength.SHORT);
                    } else if (resId == R.id.drawer_stylelist_rbutton_medium) {
                        hairLengthPreference.set(HairLength.MEDIUM);
                    }
                });
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.stylelist);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initCardStack() {
        adapter = new StyleListAdapter(this);
        cards.setAdapter(adapter);
    }

    @Override protected void onStart() {
        super.onStart();
        subscriptions.add(networkStateQuery.execute()
                .subscribe(cards::setSwipeEnabled,
                        throwable -> Timber.e(throwable, throwable.getMessage())));
        listPresenter.attachView(this);
    }

    @Override protected void onStop() {
        super.onStop();
        listPresenter.detachView(isFinishing());
        subscriptions.clear();
    }

    @Override public void onDrag(float percentage) {
        Timber.d("Percentage: %s", percentage);
    }

    @Override public void onSwipe(int index, Direction direction) {

    }

    @Override public void onSwipeDenied(Direction direction) {
        Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
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
    }

    @Override public void showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override public void showCards(List<StyleListItem> items) {
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
}

