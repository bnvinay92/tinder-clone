package in.nyuyu.android.style;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;

public class StyleActivity extends NyuyuActivity {

    @BindView(R.id.style_iview) ImageView styleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
        StyleListItem item = getIntent().getParcelableExtra(StyleListItem.EXTRA);
        Glide.with(this)
                .load(item.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(styleImageView);
    }
}
