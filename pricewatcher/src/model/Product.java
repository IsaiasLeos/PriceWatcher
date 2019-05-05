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
     * Default Constructor
     */
    public Product() {
    }

    /**
     * Creates a product given all the parameters.
     *
     * @param url
     * @param name
     * @param date
     * @param currentPrice
     * @param change
     * @param startingPrice
     * @param productIcon
     * @param sound
     */
    public Product(String url, String name, String date, double currentPrice, double change, double startingPrice, Image productIcon, boolean sound) {
        this.url = url;
        this.name = name;
        this.date = date;
        this.currentPrice = currentPrice;
        this.change = change;
        this.startingPrice = startingPrice;
        this.productIcon = getIcon();
        this.sound = sound;
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
     * Returns a boolean that indicates if the current Product can play a sound.
     *
     * @return {@link Boolean} flag
     */
    public boolean getSound() {
        return sound;
    }

    /**
     * Sets a boolean that indicates if the current Product can play a sound.
     *
     * @param playSound flag
     */
    public void setSound(boolean playSound) {
        this.sound = playSound;
    }

    /**
     * Returns the current URL of the Product.
     *
     * @return {@link String} URL
     */
    public String getURL() {
        return url;
    }

    /**
     * Replaces the current URL of the item being watched.
     *
     * @param url {@link String} URL
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Returns the name of the current item.
     *
     * @return {@link String} Name
     */
    public String getName() {
        return name;
    }

    /**
     * Replaces the current Name of the item being watched.
     *
     * @param name {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the current item.
     *
     * @return {@link Double} Current Price
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
     * Returns the current date of when the product was added.
     *
     * @return {@link String}
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
     * Return the value of change between the initial price and the new price.
     *
     * @return {@link Double} Change
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
     * @return {@link Double} Starting Price
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
     * Returns the current icon that will Render with this specific Product.
     *
     * See {@link view.ItemView} for more information on what image will be
     * rendering.
     *
     * @return {@link Image}
     */
    public Image getIcon() {
        if (productIcon == null) {
            this.productIcon = getImage("webbrowser.png");
        }
        return productIcon;
    }

    /**
     * Sets the icon that will Render with this specific Product
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

    /**
     * Will remove unwanted information from an Amazon Link.
     */
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
            if (sanitize[i].equals("/") || sanitize[i].equals("//")) {
                sanitize[i] = "";
            }
            newUrl += sanitize[i] + "/";
        }
        if (newUrl.contains("//")) {
            newUrl = newUrl.substring(0, newUrl.length() - 1);
        }
        setURL(newUrl);
    }

}
