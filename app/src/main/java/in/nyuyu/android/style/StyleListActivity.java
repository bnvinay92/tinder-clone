package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import in.nyuyu.android.LikedStyleListActivity;
import in.nyuyu.android.R;
import in.nyuyu.android.cardstackview.CardStackView;
import in.nyuyu.android.cardstackview.Direction;
import in.nyuyu.android.commons.NyuyuActivity;
import timber.log.Timber;

public class StyleListActivity extends NyuyuActivity implements CardStackView.CardStackEventListener, Toolbar.OnMenuItemClickListener, StyleListView {

    @BindView(R.id.stylelist_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.stylelist_toolbar) Toolbar toolbar;
    @BindView(R.id.stylelist_cardstack) CardStackView cards;

    CardAdapter adapter;
    @Inject StyleListPresenter listPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_list);
        initToolbar();
        initCardStack();
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.stylelist);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initCardStack() {
        adapter = new CardAdapter(this);
        for (int i = 0; i < 20; i++)
            adapter.add(String.valueOf(i));
        cards.setAdapter(adapter);
    }

    @Override protected void onStart() {
        super.onStart();
        listPresenter.attachView(this);
    }

    @Override protected void onStop() {
        super.onStop();
        listPresenter.detachView(isFinishing());
    }

    @Override public void onDrag(float percentage) {
        Timber.d("Percentage: %s", percentage);
    }

    @Override public void onSwipe(int index, Direction direction) {

    }

    @Override public void onSwipeDenied(Direction direction) {

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
}

