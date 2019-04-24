package controller;

/**
 * This class handles the logic and memory storage of the item.
 *
 * @author Isaias Leos
 */
public class PriceFinder extends WebServerSocket {

    /**
     * Generate a simulated price of an item between $300.00-$400.00.
     *
     * @param url
     * @return random double between 300 - 400
     */
    public double getSimulatedPrice(String url) {
        //Temporary Check | TODO Remove
        return super.checkURL(url);
    }
}
