/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import model.Product;
import controller.ProductManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Blade
 */
public class StorageManager extends ProductManager {

    /**
     *
     * @return
     */
    public JSONArray toJSON() {

        return new JSONArray(get());
    }

    /**
     *
     * @param arr
     */
    public void toStorage(JSONArray arr) {
        try ( FileWriter file = new FileWriter(new File("src/resources/products.json"))) {
            file.write(arr.toString());
        } catch (IOException ex) {
            Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fromJSON() throws FileNotFoundException {

        JSONTokener tokener = new JSONTokener(new FileInputStream(new File("src/resources/products.json")));
        JSONArray productListJSON = new JSONArray(tokener);
        JSONObject productJSON;
        for (int i = 0; i < productListJSON.length(); i++) {
            productJSON = productListJSON.getJSONObject(i);
            Product creationProduct = new Product();
            creationProduct.setDate(productJSON.getString("date"));
            creationProduct.setCurrentPrice(productJSON.getInt("currentPrice"));
            creationProduct.setStartingPrice(productJSON.getInt("startingPrice"));
            creationProduct.setName(productJSON.getString("name"));
            creationProduct.setURL(productJSON.getString("URL"));
            creationProduct.setChange(productJSON.getInt("change"));
            add(creationProduct);
        }
    }
}
