package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * This class handles the logic and memory storage of the item.
 *
 * @author Isaias Leos
 */
public class PriceFinder extends WebServerSocket {

    public PriceFinder() {
    }

    /**
     * Generate a simulated price of an item between $300.00-$400.00.
     *
     * @param url
     * @return random double between 300 - 400
     */
    public double getPrice(String url) {
        return checkURL(url);
    }

    /**
     * Generate a simulated price of an item between $300.00-$400.00.
     *
     * @return random double between 300 - 400
     */
    @Deprecated
    public double getPrice() {
        Random rand = new Random();
        double minValue = 300.00;
        double maxValue = 400.00;
        return (new BigDecimal(minValue + (maxValue - minValue) * rand.nextDouble()).setScale(2, RoundingMode.CEILING).doubleValue());
    }
}
