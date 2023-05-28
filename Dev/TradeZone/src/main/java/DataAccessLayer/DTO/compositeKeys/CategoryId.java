package DataAccessLayer.DTO.compositeKeys;

import DataAccessLayer.DTO.DTOStore;

import java.io.Serializable;
import java.util.Objects;

public class CategoryId implements Serializable {
    private String categoryName;
    //private DTOStore dtoStore;
    private String storeName;

    //    public CategoryId(String categoryName, DTOStore dtoStore) {
//        this.categoryName = categoryName;
//        this.dtoStore = dtoStore;
//    }
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
}

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        CategoryId category = (CategoryId) o;
//        return categoryName.equals(category.categoryName) && dtoStore.getStoreName().equals(category.dtoStore.getStoreName());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(categoryName, dtoStore.getStoreName());
//    }







/*  private String categoryName;
    private String storeName;

    public CategoryId(String categoryName, String storeName) {
        this.categoryName = categoryName;
        this.storeName = storeName;
    }
    public CategoryId(){

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
    }*/
