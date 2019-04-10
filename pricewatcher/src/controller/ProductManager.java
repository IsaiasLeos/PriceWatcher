package controller;

import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductManager {

    private List<Product> products;

    public ProductManager() {
        this.products = new ArrayList<>();
    }

    /**
     *
     * @param product
     */
    public void add(Product product) {
        this.getProducts().add(product);
    }

    /**
     *
     * @param product
     */
    public void delete(Product product) {
        this.getProducts().remove(product);
    }

    /**
     *
     * @return
     */
    public List<Product> getList() {
        return this.getProducts();
    }

    /**
     * @return the items
     */
    public List<Product> getProducts() {
        return this.products;
    }

    /**
     * @param products
     */
    public void setItems(List<Product> products) {
        this.products = products;
    }

}
