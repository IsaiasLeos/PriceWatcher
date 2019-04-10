package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class holds information about the current product and changes done to
 * it.
 *
 * @author Isaias Leos
 */
public class Product {

    private String productURL;
    private String productName;
    private String addedDate;
    private double productPrice;
    private double change;
    private double initialPrice;

    /**
     * Alternative constructor for the product with given information.
     *
     * @param productName name of product
     * @param currentURL current URL of the product
     * @param initialPrice price when first added
     * @param addedDate date product was added
     */
    public Product(String currentURL, String productName, double initialPrice, String addedDate) {
        this.productURL = currentURL;
        this.productName = productName;
        this.initialPrice = initialPrice;
        this.productPrice = initialPrice;
        this.addedDate = addedDate;
    }

    /**
     * Default constructor for the product.
     */
    public Product() {
    }

    /**
     * Sets the price to a new calculated price and updates the change.
     *
     * @param newPrice
     */
    public void checkPrice(double newPrice) {
        setProductPrice(newPrice);
        setChange(new BigDecimal(calculateProductChange(getInitialPrice(),
                getProductPrice())).setScale(2, RoundingMode.CEILING).doubleValue());
    }

    /**
     * This method computes the percentage difference between two numbers.
     *
     * @param initialPrice initial price of the product
     * @param newPrice the current price of the product
     * @return the difference shown as a percentage
     */
    private double calculateProductChange(double newPrice, double initialPrice) {
        return ((newPrice - initialPrice) / initialPrice) * 100;
    }

    /**
     *
     * @return returns the current URL of the item being watched.
     */
    public String getProductURL() {
        return productURL;
    }

    /**
     * Replaces the current URL of the item being watched.
     *
     * @param productURL the name of the URL that will be replacing the current
     * item name.
     */
    public void setCurrentURL(String productURL) {
        this.productURL = productURL;
    }

    /**
     *
     * @return returns the name of the current item.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Replaces the current Name of the item being watched.
     *
     * @param productName the name of the item that will be replacing the
     * current item.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return returns the price of the current item.
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Replaces the current Price of the item being watched.
     *
     * @param productPrice the price that will be replacing the current price of
     * the item.
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     *
     * @return the current date of when the product was added
     */
    public String getAddedDate() {
        return addedDate;
    }

    /**
     * Sets the date of when a product was added.
     *
     * @param addedDate
     */
    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    /**
     *
     * @return the value of change between the initial price and the new price.
     */
    public double getChange() {
        return change;
    }

    /**
     * Sets the value of difference between two prices.
     *
     * @param change
     */
    public void setChange(double change) {
        this.change = change;
    }

    /**
     * Get the initial price of a product.
     *
     * @return
     */
    public double getInitialPrice() {
        return initialPrice;
    }

    /**
     * Sets the initial price of an item, if wrong.
     *
     * @param initialPrice
     */
    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }
}
