package in.nyuyu.android.style;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;

public class LikedStyleListActivity extends NyuyuActivity implements LikedStyleListView {

    @BindView(R.id.likedlist_toolbar) Toolbar toolbar;
    @BindView(R.id.likedlist_rview) RecyclerView recyclerView;

    @Inject LikedStyleListPresenter presenter;
    private LikedStyleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_style_list);
        ((Nyuyu) getApplication()).component().inject(this);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        adapter = new LikedStyleListAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override public void showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override public void showStyles(List<StyleListItem> items) {
        adapter.setItems(items);
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
