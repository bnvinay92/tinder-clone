package in.nyuyu.android.style;

import javax.inject.Inject;

import in.nyuyu.android.commons.queries.CurrentUserQuery;
import in.nyuyu.android.style.queries.LastSeenStyleIdQuery;
import in.nyuyu.android.style.queries.StyleListFilterParametersQuery;

/**
 * Created by Vinay on 24/09/16.
 */
public class RefreshLastSeenStyleId {

    private final StyleListFilterParametersQuery filterParametersQuery;
    private final LastSeenStyleIdQuery lastSeenStyleIdQuery;
    private final CurrentUserQuery userQuery;

    @Inject public RefreshLastSeenStyleId(StyleListFilterParametersQuery filterParametersQuery, LastSeenStyleIdQuery lastSeenStyleIdQuery, CurrentUserQuery userQuery) {
        this.filterParametersQuery = filterParametersQuery;
        this.lastSeenStyleIdQuery = lastSeenStyleIdQuery;
        this.userQuery = userQuery;
    }

    public void execute() {
        String filterParams = filterParametersQuery.get();
        if (!filterParams.isEmpty()) {
            String userId = userQuery.getUserId();
            lastSeenStyleIdQuery.delete(userId, filterParams);
        }
    }
}
