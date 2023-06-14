package DataAccessLayer.CompositeKeys;//package DataAccessLayer.DTO.compositeKeys;


import java.io.Serializable;
import java.util.Objects;

public class CategoryId implements Serializable {
    private String categoryName;
    private String storeName;

    public CategoryId(String categoryName, String storeName) {
        this.categoryName = categoryName;
        this.storeName = storeName;
    }

    public CategoryId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId category = (CategoryId) o;
        return categoryName.equals(category.categoryName) && storeName.equals(category.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, storeName);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStoreName() {
        return storeName;
    }
}
