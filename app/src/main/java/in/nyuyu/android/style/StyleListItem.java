package in.nyuyu.android.style;

/**
 * Created by Vinay on 20/09/16.
 */
public class StyleListItem {
    private Long id;
    private String imageUrl;
    private Long likeCount;
    private Long likeModified;
    private String name;

    public StyleListItem() {
    }

    public StyleListItem(Long id, String imageUrl, Long likeCount, Long likeModified, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.likeModified = likeModified;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getLikeModified() {
        return likeModified;
    }

    public String getName() {
        return name;
    }
}
