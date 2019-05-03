/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import model.Product;
import storage.StorageManager;

/**
 *
 * @author Blade
 */
public class Sorting {

    /**
     *
     */
    public Sorting() {
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortOld(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product2.getDate().compareTo(product1.getDate()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortNew(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product1.getDate().compareTo(product2.getDate()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortNameAscending(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product2.getName().compareTo(product1.getName()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortNameDescending(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product1.getName().compareTo(product2.getName()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortHigh(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> ("" + product1.getCurrentPrice()).compareTo("" + product2.getCurrentPrice()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortLow(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> ("" + product2.getCurrentPrice()).compareTo("" + product1.getCurrentPrice()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortChangeHigh(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> ("" + product2.getChange()).compareTo("" + product1.getChange()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public void sortChangeLow(StorageManager storageManager, DefaultListModel defaultListModel) {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> ("" + product2.getChange()).compareTo("" + product1.getChange()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    private void removeFilterBy() {

    }

}
