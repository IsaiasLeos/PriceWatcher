/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import controller.ProductManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is in charge of maintaining storing information and reading it
 * maintaining offline storage.
 *
 * @author Isaias Leos
 */
public class StorageManager extends ProductManager {

    /**
     * @return generated jsonarray obtained from product manager
     */
    public JSONArray toJSON() {
        return new JSONArray(get());
    }

    /**
     * Generates a JSONArray from the current {@link ProductManager}.
     *
     * @param arr json array
     */
    public void toStorage(JSONArray arr) {
        try (FileWriter file = new FileWriter(new File("src/resources/products.json"))) {
            file.write(arr.toString());
        } catch (IOException ex) {
            Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads a JSON file and inputs all information into a
     * {@link ProductManager}.
     *
     * @throws FileNotFoundException couldn't find the json file
     */
    public void fromJSON() throws FileNotFoundException {
        JSONTokener tokener = new JSONTokener(new FileInputStream(new File("src/resources/products.json")));
        JSONArray productListJSON = new JSONArray(tokener);
        for (int i = 0; i < productListJSON.length(); i++) {
            JSONObject productJSON = productListJSON.getJSONObject(i);
            create(productJSON.getString("name"),
                    productJSON.getString("date"),
                    productJSON.getDouble("currentPrice"),
                    productJSON.getDouble("startingPrice"),
                    productJSON.getString("URL"),
                    productJSON.getDouble("change")
            );
        }

    }
}
