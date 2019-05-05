/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import model.Product;
import storage.StorageManager;

/**
 *
 * @author Isaias Leos, Leslie Gomez
 */
public class Sorting {

    private DefaultListModel defaultListModel;
    private StorageManager storageManager;
    public boolean isFilter = false;

    /**
     *
     * @param storageManager
     * @param defaultListModel
     */
    public Sorting(StorageManager storageManager, DefaultListModel defaultListModel) {
        this.defaultListModel = defaultListModel;
        this.storageManager = storageManager;
    }

    /**
     *
     */
    public void sortOld() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product2.getDate().compareTo(product1.getDate()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortNew() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product1.getDate().compareTo(product2.getDate()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortNameAscending() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product2.getName().compareTo(product1.getName()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortNameDescending() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> product1.getName().compareTo(product2.getName()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortHigh() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product1.getCurrentPrice()).compareTo(product2.getCurrentPrice()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortLow() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product2.getCurrentPrice()).compareTo(product1.getCurrentPrice()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortChangeHigh() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product2.getChange()).compareTo(product1.getChange()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     */
    public void sortChangeLow() {
        List<Product> products = storageManager.get();
        Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product1.getChange()).compareTo(product2.getChange()));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
    }

    /**
     *
     * @param filter
     */
    public void filterBy(String filter) {
        if (isFilter) {
            removeFilter();
        }
        List<Product> products = storageManager.get();
        products.removeIf(s -> !s.getURL().contains(filter));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
        isFilter = true;
    }

    /**
     *
     * @param filter
     */
    public void filterName(String filter) {
        if (isFilter) {
            removeFilter();
        }
        List<Product> products = storageManager.get();
        products.removeIf(s -> !s.getName().contains(filter));
        storageManager.set(products);
        defaultListModel.removeAllElements();
        defaultListModel.addAll(products);
        isFilter = true;
    }

    /**
     *
     */
    public void removeFilter() {
        if (isFilter) {
            try {
                storageManager.remove();
                storageManager.fromJSON();
                storageManager.get().forEach((element) -> defaultListModel.addElement(element));
                isFilter = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
