package in.nyuyu.android.salons.queries;

import android.location.Location;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.nyuyu.android.salons.values.SalonListItem;
import rx.AsyncEmitter;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Vinay on 02/10/16.
 */
public class SalonListQuery {

    private final Index schools;

    @Inject public SalonListQuery(Client client) {
        schools = client.getIndex("salons");
        schools.enableSearchCache();
    }

    public Observable<List<SalonListItem>> execute(Location location, long styleId) {

        Timber.d("Finding salons who have claimed: %s", styleId);
        JSONArray tagFilters = new JSONArray();
        tagFilters.put(styleId);
        String highlightAttributes[] = new String[0];
        Query query = new Query()
                .setFilters(String.valueOf(styleId))
                .setAroundLatLng(new Query.LatLng(location.getLatitude(), location.getLongitude()))
                .setMinimumAroundRadius(100)
                .setAttributesToHighlight(highlightAttributes)
                .setGetRankingInfo(true);
        return Observable.<JSONObject>fromEmitter(emitter -> {
            CompletionHandler handler = (jsonObject, exception) -> {
                if (exception != null) {
                    emitter.onError(exception);
                } else {
                    emitter.onNext(jsonObject);
                    emitter.onCompleted();
                }
            };
            Request request = schools.searchAsync(query, handler);
            emitter.setCancellation(request::cancel);
        }, AsyncEmitter.BackpressureMode.LATEST)
                .map(jsonObject -> {
                    JSONArray hits = jsonObject.optJSONArray("hits");
                    List<SalonListItem> items = new ArrayList<>();
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hit = hits.optJSONObject(i);
                        Timber.d("Salon: %s", hit.optString("name"));
                        SalonListItem item = new SalonListItem(
                                hit.optString("id"),
                                hit.optString("name"),
                                hit.optString("branchId"),
                                toStringArray(hit.optJSONArray("facilities")),
                                hit.optString("gender"),
                                hit.optString("area"),
                                hit.optJSONObject("_rankingInfo").optDouble("geoDistance")
                        );
                        items.add(item);
                    }
                    return items;
                });
    }

    private List<String> toStringArray(JSONArray strings) {
        List<String> facilities = new ArrayList<>();
        for (int i = 0; i < strings.length(); i++) {
            String facility = strings.optString(i);
            if (!facility.isEmpty()) {
                facilities.add(facility);
            }
        }
        return facilities;
    }
}
