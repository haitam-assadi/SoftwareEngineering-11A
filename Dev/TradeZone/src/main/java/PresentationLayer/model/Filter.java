package PresentationLayer.model;

import DTO.ProductDTO;

import java.util.List;

public class Filter {
    private List<ProductDTO> products;
    private String filterPrice = "";
    private String filterCategory = "";
    private String min;
    private String max;
    private String categoryName;

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public String getFilterPrice() {
        return filterPrice;
    }

    public void setFilterPrice(String filterPrice) {
        this.filterPrice = filterPrice;
    }

    public String getFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(String filterCategory) {
        this.filterCategory = filterCategory;
    }

    public int getMin() {
        if(min.equals(""))
            return 0;
        return Integer.parseInt(min);
    }

    public void setMin(String min) {
        this.min = min;
    }

    public int getMax() {
        if(max.equals(""))
            return Integer.MAX_VALUE;
        return Integer.parseInt(max);
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
