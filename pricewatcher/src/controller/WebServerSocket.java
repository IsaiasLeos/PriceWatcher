package controller;

/**
 *
 * @author Isaias Leos, Leslie Gomez
 */
public class WebServerSocket {

    public WebServerSocket() {

    }

    /**
     * Obtains the price of the {@link model.Product} according to the URL
     * handle.
     *
     * @param url product's url handle
     * @return return -1 if couldn't connect webpage otherwise product price
     */
    public double checkURL(String url) {
        if (url.contains("ebay")) {
            return getEbayPrice();
        } else if (url.contains("amazon")) {
            return getAmazonPrice();
        } else if (url.contains("walmart")) {
            return getWalmartPrice();
        } else if (url.contains("wish")) {
            return getWishPrice();
        }
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getEbayPrice() {
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getAmazonPrice() {
        //Code
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getWalmartPrice() {
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getWishPrice() {
        return -1.00;
    }

}
