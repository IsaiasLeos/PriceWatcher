package network;

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
public class WebScrape {

    /**
     * Default Constructor.
     */
    public WebScrape() {
    }

    /**
     * Obtains the price of the {@link model.Product} according to the URL
     * handle.
     *
     * @param url product's URL handle
     * @return return -1 if couldn't connect webpage otherwise product price
     */
    public double priceScrape(String url) {
        if (url.contains("ebay")) {
            return scrapeEbay(url);
        } else if (url.contains("amazon")) {
            return scrapeAmazon(url);
        } else if (url.contains("walmart")) {
            return scrapeWalmart(url);
        } else {
            return -1;
        }
    }

    /**
     * Obtains the price of an item using RegEx. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits
     *
     * @param input the raw string
     * @return possible matches of prices within the raw string
     */
    private static String findPrice(String input) {
        String output = "";
        Pattern pattern = Pattern.compile("[$](([1-9]+\\.?\\d*)|([0]\\.\\d*)|[0])");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            output = matcher.group();
        }
        if (input.contains(",")) {
            String[] tokens = input.split(">|=");
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("\"\" content")) {
                    output = tokens[i + 1];
                    output = output.replaceAll("[\"]", "");
                }
            }
        }
        return output;
    }

    /**
     * Obtains the price of an item using RegEx. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     *
     * @return the price of an item that is from Ebay
     */
    private double scrapeEbay(String urlString) {
        HttpURLConnection con = null;
        String priceOutput = "";
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    priceOutput = findPrice(line);
                    if (!priceOutput.equals("")
                            && (line.contains("notranslate") && !line.contains("notranslate mm-strkthru"))
                            && !line.contains("notranslate vi-vpo-strkthru vi-vpo-now")) {
                        if (priceOutput.contains("$")) {
                            return Double.parseDouble(priceOutput.substring(1, priceOutput.length()));
                        } else {
                            return Double.parseDouble(priceOutput);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

    /**
     * Obtains the price of an item using RegEx. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     *
     * @return
     */
    private double scrapeAmazon(String urlString) {
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
                output = findPrice(line);
                if (!output.equals("") && line.contains("priceBlockBuyingPriceString") || line.contains("priceBlockDealPriceString") || line.contains("a-size-medium a-color-price priceBlockBenefitPriceString")) {
                    return Double.parseDouble(output.substring(1, output.length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

    /**
     * Obtains the price of an item using RegEx. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     *
     * @return
     */
    private double scrapeWalmart(String urlString) {
        HttpURLConnection con = null;
        String output = "";
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output = findPrice(line);
                    if (!output.equals("") && !output.contains("$0")) {
                        return Double.parseDouble(output.substring(1, output.length()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.00;
    }

}
