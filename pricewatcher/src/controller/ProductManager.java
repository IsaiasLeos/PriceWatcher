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
     * Adds a {@link model.Product} to the Product List
     *
     * @param product
     */
    public void add(Product product) {
        this.getProducts().add(product);
    }

    /**
     * Deletes a {@link model.Product} from the Product List
     *
     * @param product
     */
    public void delete(Product product) {
        this.getProducts().remove(product);
    }

    /**
     *
     * @return the list of products
     */
    public List<Product> getList() {
        return this.getProducts();
    }

    /**
     *
     * @return the the list of products
     */
    public List<Product> getProducts() {
        return this.products;
    }

    /**
     * Sets the current product list to the given one from the parameter.
     *
     * @param products
     */
    public void setItems(List<Product> products) {
        this.products = products;
    }

}
