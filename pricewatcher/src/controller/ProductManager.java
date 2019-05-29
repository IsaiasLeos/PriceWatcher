package controller;

import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private List<Product> arrOfProducts;

    /**
     * Generates a Empty list of Products.
     */
    public ProductManager() {
        this.arrOfProducts = new ArrayList<>();
    }

    /**
     * Adds a {@link model.Product} to the Product List.
     *
     * @param product product
     */
    public void add(Product product) {
        this.get().add(product);
    }

    /**
     * Deletes a {@link model.Product} from the Product List.
     *
     * @param product product
     */
    public void delete(int product) {
        this.get().remove(product);
    }

    /**
     * @return the list of products
     */
    public List<Product> get() {
        return this.arrOfProducts;
    }

    /**
     * Creates a Product and adds it to the existing ArrayList.
     *
     * @param name name of product
     * @param date date when product was added
     * @param currentPrice current price of the product
     * @param startingPrice initial price of the product
     * @param URL url pertaining to the product
     * @param change the change between previous price and current
     */
    protected void create(String name, String date, double currentPrice, double startingPrice, String URL, double change) {
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
     * @param arrOfProducts a list of products
     */
    public void set(List<Product> arrOfProducts) {
        this.arrOfProducts = arrOfProducts;
    }

    /**
     * Removes all Product's from the existing ArrayList.
     */
    public void remove() {
        this.arrOfProducts = new ArrayList<>();
    }

}
