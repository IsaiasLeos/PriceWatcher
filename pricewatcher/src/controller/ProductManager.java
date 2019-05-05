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
     *
     * @param name
     * @param date
     * @param currentPrice
     * @param startingPrice
     * @param URL
     * @param change
     */
    public void create(String name, String date, double currentPrice, double startingPrice, String URL, double change) {
        Product generated = new Product();
        generated.setName(name);
        generated.setDate(date);
        generated.setCurrentPrice(currentPrice);
        generated.setStartingPrice(startingPrice);
        generated.setURL(URL);
        generated.setChange(change);
        add(generated);
    }

    /**
     * Sets the current product list to the given one from the parameter.
     *
     * @param arrOfProducts
     */
    public void set(List<Product> arrOfProducts) {
        this.arrOfProducts = arrOfProducts;
    }

    /**
     *
     */
    public void remove() {
        this.arrOfProducts = new ArrayList<>();
    }

}
