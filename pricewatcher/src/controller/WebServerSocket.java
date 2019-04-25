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
            return getEbayPrice(url);
        } else if (url.contains("amazon")) {
            return getAmazonPrice(url);
        } else if (url.contains("walmart")) {
            return getWalmartPrice(url);
        } else if (url.contains("wish")) {
            return getWishPrice(url);
        }
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getEbayPrice(String urlString) {
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getAmazonPrice(String urlString) {

        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getWalmartPrice(String urlString) {
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getWishPrice(String urlString) {
        return -1.00;
    }

}
