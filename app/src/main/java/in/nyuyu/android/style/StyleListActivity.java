package in.nyuyu.android.style;

import android.os.Bundle;

import butterknife.BindView;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.cardstackview.CardStackView;
import in.nyuyu.android.cardstackview.Direction;
import timber.log.Timber;

public class StyleListActivity extends NyuyuActivity implements CardStackView.CardStackEventListener {

    @BindView(R.id.cardStackView) CardStackView cardStackView;
    CardAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_list);
        arrayAdapter = new CardAdapter(this);
        for (int i = 0; i < 20; i++) {
            arrayAdapter.add(String.valueOf(i));
        }
        cardStackView.setCardStackEventListener(this);
        cardStackView.setAdapter(arrayAdapter);
    }

    @Override public void onDrag(float percentage) {
        Timber.d("Percentage: %s", percentage);
    }

    @Override public void onSwipe(int index, Direction direction) {

    }

    @Override public void onTapUp(int index) {

    }
}

