package in.nyuyu.android.salons;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.salons.queries.SalonListQuery;
import in.nyuyu.android.salons.values.SalonListItem;
import in.nyuyu.android.style.StyleListItem;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class SalonListActivity extends NyuyuActivity {

    public static final float DISPLACEMENT_UPDATE_THRESHOLD = 30;
    public static final int LOCATION_SETTINGS_REQUEST_CODE = 7001;

    LocationRequest request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setSmallestDisplacement(DISPLACEMENT_UPDATE_THRESHOLD);

    LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
            .addLocationRequest(request)
            .setAlwaysShow(true)
            .build();

    private Subscription subscription = Subscriptions.unsubscribed();

    @Inject SalonListQuery query;
    @BindView(R.id.salonlist_toolbar) Toolbar toolbar;
    @BindView(R.id.salonlist_rview) RecyclerView recyclerView;

    long styleId;
    ReactiveLocationProvider locationProvider;

    SalonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);
        ((Nyuyu) getApplication()).component().inject(this);
        styleId = getIntent().getLongExtra(StyleListItem.EXTRA_ID, -1L);
        locationProvider = new ReactiveLocationProvider(this);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.salonlist);
        toolbar.setTitle("Salons");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        adapter = new SalonListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override protected void onStart() {
        super.onStart();
        subscribeToLocationUpdates();
    }

    private void subscribeToLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need location permission", Toast.LENGTH_SHORT).show();
        } else {
            subscription = locationProvider.checkLocationSettings(settingsRequest)
                    .filter(result -> {
                        Status status = result.getStatus();
                        if (status.isSuccess()) {
                            return true;
                        } else {
                            try {
                                status.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                throw Exceptions.propagate(e);
                            }
                            return false;
                        }
                    })
                    .flatMap(result -> locationProvider.getUpdatedLocation(request))
                    .switchMap(location -> query.execute(location, styleId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showSalons,
                            throwable -> Timber.e(throwable, throwable.getMessage()));
        }
    }

    private void showSalons(List<SalonListItem> salonList) {
        Timber.d("Salons: %s", salonList);
        adapter.show(salonList);
    }

    @Override protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    subscription.unsubscribe();
                    subscribeToLocationUpdates();
                    break;
                case Activity.RESULT_OK:
                    subscription.unsubscribe();
                    subscribeToLocationUpdates();
            }
        }
    }
}
