package in.nyuyu.android.style;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import in.nyuyu.android.Nyuyu;
import in.nyuyu.android.R;
import in.nyuyu.android.commons.NyuyuActivity;
import in.nyuyu.android.salons.SalonListActivity;
import in.nyuyu.android.style.queries.StyleQuery;
import in.nyuyu.android.style.values.Equipment;
import in.nyuyu.android.style.values.Product;
import in.nyuyu.android.style.values.Service;
import in.nyuyu.android.style.values.Style;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class StyleActivity extends NyuyuActivity {

    @BindView(R.id.style_iview) ImageView styleImageView;
    @BindView(R.id.style_name) TextView name;
    @BindView(R.id.style_description) TextView description;
    @BindView(R.id.style_age) TextView age;
    @BindView(R.id.style_faceshape) TextView faceShape;
    @BindView(R.id.style_length) TextView length;
    @BindView(R.id.style_maintenance) TextView maintenance;
    @BindView(R.id.style_occasion) TextView occasion;
    @BindView(R.id.style_thickness) TextView thickness;
    @BindView(R.id.style_texture) TextView texture;
    @BindView(R.id.style_products) TextView products;
    @BindView(R.id.style_equipments) TextView equipments;
    @BindView(R.id.style_services) TextView services;
    @BindView(R.id.style_suitable_faceshapes) TextView faceShapes;

    private StyleListItem item;

    @Inject StyleQuery query;
    private Subscription subscription = Subscriptions.unsubscribed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
        ((Nyuyu) getApplication()).component().inject(this);
        item = getIntent().getParcelableExtra(StyleListItem.EXTRA);
        Glide.with(this)
                .load(item.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(styleImageView);
    }

    @OnClick(R.id.style_cta)
    public void onCtaClick() {
        Intent salonListActivityStarter = new Intent(this, SalonListActivity.class);
        salonListActivityStarter.putExtra(StyleListItem.EXTRA_ID, item.getId());
        startActivity(salonListActivityStarter);
    }

    @Override protected void onStart() {
        super.onStart();
        subscription = query.execute(item.getId())
                .subscribe(this::showStyle);
    }

    private void showStyle(Style style) {
        name.setText("Name: " + style.getName());
        description.setText("Description: " + style.getDescription());
        age.setText("Age: " + style.getMinAge() + " - " + style.getMaxAge());
        faceShape.setText("Model face shape: " + style.getModelFaceShape());
        length.setText("Length: " + style.getLength());
        maintenance.setText("Maintenance: " + style.getMaintenance());
        occasion.setText("Occasion: " + style.getSuitsOccasions());
        thickness.setText("Thickness: " + style.getThickness());
        texture.setText("Texture: " + style.getTexture());
        String productText = "Products: ";
        if (style.getProducts() != null) {
            for (Map.Entry<String, Product> entry : style.getProducts().entrySet()) {
                productText += entry.getValue().getName() + ", ";
            }
        }
        String equipmentText = "Equipments: ";
        if (style.getEquipments() != null) {
            for (Map.Entry<String, Equipment> entry : style.getEquipments().entrySet()) {
                equipmentText += entry.getValue().getName() + ", ";
            }
        }
        String servicesText = "Services: ";
        if (style.getServices() != null) {
            for (Map.Entry<String, Service> entry : style.getServices().entrySet()) {
                servicesText += entry.getValue().getName() + ", ";
            }
        }
        products.setText(productText);
        services.setText(servicesText);
        equipments.setText(equipmentText);
        String faceShapesText = "Suitable for face shapes: ";
        if (style.getFaceShapes() != null) {
            for (String faceShape : style.getFaceShapes()) {
                faceShapesText += faceShape + ", ";
            }
        }
        faceShapes.setText(faceShapesText);
    }

    @Override protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }

    @OnClick(R.id.style_iview)
    public void onImageClick() {
        finish();
    }
}
