package controller;

import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductManager {

    private List<Product> arrOfProducts;

    public ProductManager() {
        this.arrOfProducts = new ArrayList<>();
    }

    /**
     * Adds a {@link model.Product} to the Product List
     *
     * @param product
     */
    public void add(Product product) {
        this.get().add(product);
    }

    /**
     * TODO
     */
    public void create() {

    }

    /**
     * Deletes a {@link model.Product} from the Product List
     *
     * @param product
     */
    public void delete(int product) {
        this.get().remove(product);
    }

    /**
     *
     * @return the list of products
     */
    public List<Product> get() {
        return this.arrOfProducts;
    }

    /**
     * Sets the current product list to the given one from the parameter.
     *
     * @param arrOfProducts
     */
    public void set(List<Product> arrOfProducts) {
        this.arrOfProducts = arrOfProducts;
    }

}
