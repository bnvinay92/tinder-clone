package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;

public class LikedStyleListActivity extends NyuyuActivity implements LikedStyleListView, LikedStyleListAdapter.OnItemClickListener {

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
        adapter.setItems(new ArrayList<>());

    }

    @Override public void showTimedOut() {
        Toast.makeText(this, "TimedOut", Toast.LENGTH_SHORT).show();
    }

    @Override public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override public void onItemClick(View itemView, int position) {
        StyleListItem item = adapter.get(position);
        if (itemView.getId() == R.id.item_likedstyle_iview_delete) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference(String.format("sessions/%s/styles/liked/%s", userId, item.getId())).setValue(null);
            adapter.remove(position);
        } else {
            Intent styleActivityStarter = new Intent(this, StyleActivity.class);
            styleActivityStarter.putExtra(StyleListItem.EXTRA, item);
            startActivity(styleActivityStarter);
        }
    }
}
