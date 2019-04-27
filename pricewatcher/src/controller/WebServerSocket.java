package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Isaias Leos, Leslie Gomez
 */
public class WebServerSocket {

    public boolean isDone = false;

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
        } else {
            return -1;
        }
    }

    /**
     *
     * @return the price of an item that is from Ebay
     */
    private double getEbayPrice(String urlString) {
        HttpURLConnection con = null;
        String output = "";
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output = getPrice(line);
                    if (!output.equals("") && line.contains("notranslate")) {
                        return Double.parseDouble(output.substring(1, output.length()));
                    }
                }
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

    /**
     * Obtains the price of an item using RegEx.
     *
     * @param input the raw string
     * @return possible matches of prices within the raw string
     */
    private static String getPrice(String input) {
        String output = "";
        //Contains $, Followed by Digits, then a period or any number of digits before the period.
        //After the period any number of digits
        Pattern pattern = Pattern.compile("[$](([1-9]+\\.?\\d*)|([0]\\.\\d*)|[0])");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            output = matcher.group();
        }
        return output;
    }

    /**
     *
     * @return
     */
    private double getAmazonPrice(String urlString) {
        HttpURLConnection con = null;
        String output = "";
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            String encoding = con.getContentEncoding();
            if (encoding == null) {
                encoding = "utf-8";
            }
            InputStreamReader reader = null;
            if ("gzip".equals(encoding)) {
                reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
            } else {
                reader = new InputStreamReader(con.getInputStream(), encoding);
            }
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                output = getPrice(line);
                if (!output.equals("") && line.contains("priceBlockBuyingPriceString") || line.contains("priceBlockDealPriceString")) {
                    return Double.parseDouble(output.substring(1, output.length()));
                }
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

    /**
     *
     * @return
     */
    private double getWalmartPrice(String urlString) {
        HttpURLConnection con = null;
        String output = "";
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output = getPrice(line);
                    if (!output.equals("") && !line.contains("$0")) {
                        return Double.parseDouble(output.substring(1, output.length()));
                    }
                }
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

}
