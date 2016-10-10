package in.nyuyu.android.salons.values;

import java.util.List;

/**
 * Created by Vinay on 02/10/16.
 */
public class SalonListItem {
    private final String id;
    private final String name;
    private final String branchId;
    private final List<String> facilities;
    private final String gender;
    private final String area;
    private final double distance;

    public SalonListItem(String id, String name, String branchId, List<String> facilities, String gender, String area, double distance) {
        this.id = id;
        this.name = name;
        this.branchId = branchId;
        this.facilities = facilities;
        this.gender = gender;
        this.area = area;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBranchId() {
        return branchId;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public String getGender() {
        return gender;
    }

    public String getArea() {
        return area;
    }


}
