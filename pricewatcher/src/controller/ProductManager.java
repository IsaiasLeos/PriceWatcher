package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Product;

public class ProductManager {

    private List<Product> items;

    public ProductManager() {
        this.items = new ArrayList<>();
    }

    /**
     *
     * @param product
     */
    public void add(Product product) {
        this.getItems().add(product);
    }

    /**
     *
     * @param item
     */
    public void delete(Product item) {
        this.getItems().remove(item);
    }

    /**
     *
     * @return
     */
    public List<Product> getList() {
        return this.getItems();
    }

    /**
     * @return the items
     */
    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Product> items) {
        this.items = items;
    }

}
