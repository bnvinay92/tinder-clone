package in.nyuyu.android.style.values;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

/**
 * Created by Vinay on 04/10/16.
 */

public class Style {

    private Long id;
    private String name;
    private String description;
    private String minAge;
    private String maxAge;
    private String gender;
    private String length;
    private Map<String, Object> likeInfo;
    private Map<String, Product> products;
    private Map<String, Equipment> equipments;
    private Map<String, Service> services;
    private List<String> faceShapes;
    private String maintenance;
    private String modelFaceShape;
    private String suitsOccasions;
    private String texture;
    private String thickness;

    public Style() {
    }

    public List<String> getFaceShapes() {
        return faceShapes;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Map<String, Equipment> getEquipments() {
        return equipments;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMinAge() {
        return minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getGender() {
        return gender;
    }

    public String getLength() {
        return length;
    }

    public Map<String, Object> getLikeInfo() {
        return likeInfo;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public String getModelFaceShape() {
        return modelFaceShape;
    }

    public String getSuitsOccasions() {
        return suitsOccasions;
    }

    public String getTexture() {
        return texture;
    }

    public String getThickness() {
        return thickness;
    }

    @Exclude public Long getLikes() {
        return (Long) likeInfo.get("likeCount");
    }
}
