package model;

import java.awt.Image;
import java.net.URL;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.imageio.ImageIO;

/**
 * This class holds information about the current product and changes done to
 * it.
 *
 * @author Isaias Leos
 */
public class Product {

    private String url;
    private String name;
    private String date;
    private double currentPrice;
    private double change;
    private double startingPrice;
    private Image productIcon;
    private boolean sound;

    /**
     *
     */
    public Product() {
    }

    /**
     * Alternative constructor for the product with given information.
     *
     * @param name name of product
     * @param url current URL of the product
     * @param date date product was added
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Product(String url, String name, String date) {
        this.url = url;
        this.name = name;
        this.currentPrice = startingPrice;
        this.date = date;
        this.productIcon = getImage("webbrowser.png");
        if (this.url.contains("amazon")) {
            urlSanitize();
        }
    }

    public Product(String url, String name, double startingPrice, String date) {
        this.url = url;
        this.name = name;
        this.startingPrice = startingPrice;
        this.currentPrice = this.startingPrice;
        this.date = date;
        this.productIcon = getImage("webbrowser.png");
        if (this.url.contains("amazon")) {
            urlSanitize();
        }
    }

    /**
     * Sets the price to a new calculated price and updates the change.
     *
     * @param price
     */
    public void checkPrice(double price) {
        setCurrentPrice(price);
        setChange(new BigDecimal(calcChange(getStartingPrice(), getCurrentPrice())).setScale(2, RoundingMode.CEILING).doubleValue());
        sound = true;
    }

    /**
     * This method computes the percentage difference between two numbers.
     *
     * @param initialPrice initial price of the product
     * @param newPrice the current price of the product
     * @return the difference shown as a percentage
     */
    private double calcChange(double newPrice, double initialPrice) {
        return ((newPrice - initialPrice) / initialPrice) * 100;
    }

    /**
     *
     * @return
     */
    public boolean getSound() {
        return sound;
    }

    /**
     *
     * @param playSound
     */
    public void setSound(boolean playSound) {
        this.sound = playSound;
    }

    /**
     *
     * @return returns the current URL of the item being watched.
     */
    public String getURL() {
        return url;
    }

    /**
     * Replaces the current URL of the item being watched.
     *
     * @param url the name of the URL that will be replacing the current item
     * name.
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     *
     * @return returns the name of the current item.
     */
    public String getName() {
        return name;
    }

    /**
     * Replaces the current Name of the item being watched.
     *
     * @param name the name of the item that will be replacing the current item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return returns the price of the current item.
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Replaces the current Price of the item being watched.
     *
     * @param price the price that will be replacing the current price of the
     * item.
     */
    public void setCurrentPrice(double price) {
        this.currentPrice = price;
    }

    /**
     *
     * @return the current date of when the product was added
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of when a product was added.
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
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
    public double getStartingPrice() {
        return startingPrice;
    }

    /**
     * Sets the initial price of an item, if wrong.
     *
     * @param price
     */
    public void setStartingPrice(double price) {
        this.startingPrice = price;
    }

    /**
     *
     * @return
     */
    public Image getIcon() {
        if (productIcon == null) {
            this.productIcon = getImage("webbrowser.png");
        }
        return productIcon;
    }

    /**
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.productIcon = getImage(icon);
    }

    /**
     * Return the image stored in the given file.
     *
     * @param file
     * @return
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Image getImage(String file) {
        try {
            URL url = new URL(getClass().getResource("/resources/"), file);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void urlSanitize() {
        String[] sanitize = url.split("/");
        String newUrl = "";
        for (int i = 0; i < sanitize.length; i++) {
            if (sanitize[i].contains("ref") && !sanitize[i].contains("?ref")) {
                sanitize[i] = "";
            }
            if (sanitize[i].contains("?ref")) {
                String[] fixURL = sanitize[i].split("[?]");
                sanitize[i] = fixURL[0];
            }
            if (sanitize[i].equals("/")) {
                sanitize[i] = "";
            }
            newUrl += sanitize[i] + "/";
        }
        setURL(newUrl);
    }

}
