/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import model.Product;
import storage.StorageManager;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

/**
 * This class is in charge of sorting and filters everything from
 * {@link controller.ProductManager} and {@link DefaultListModel}
 *
 * @author Isaias Leos, Leslie Gomez
 */
public class Sorting {

    private boolean isFilter = false;
    private DefaultListModel defaultListModel;
    private StorageManager storageManager;

    /**
     * Default Constructor.
     *
     * @param storageManager
     * @param defaultListModel
     */
    public Sorting(StorageManager storageManager, DefaultListModel defaultListModel) {
        this.defaultListModel = defaultListModel;
        this.storageManager = storageManager;
    }

    public void sortBy(int sortCode) {
        List<Product> products = storageManager.get();
        if (sortCode == 0) {
            Collections.sort(products, (Product product2, Product product1) -> product2.getDate().compareTo(product1.getDate()));
        } else if (sortCode == 1) {
            Collections.sort(products, (Product product2, Product product1) -> product1.getDate().compareTo(product2.getDate()));
        } else if (sortCode == 2) {
            Collections.sort(products, (Product product2, Product product1) -> product2.getName().compareTo(product1.getName()));
        } else if (sortCode == 3) {
            Collections.sort(products, (Product product2, Product product1) -> product1.getName().compareTo(product2.getName()));
        } else if (sortCode == 4) {
            Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product1.getCurrentPrice()).compareTo(product2.getCurrentPrice()));
        } else if (sortCode == 5) {
            Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product2.getCurrentPrice()).compareTo(product1.getCurrentPrice()));
        } else if (sortCode == 6) {
            Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product2.getChange()).compareTo(product1.getChange()));
        } else if (sortCode == 7) {
            Collections.sort(products, (Product product2, Product product1) -> Double.valueOf("" + product1.getChange()).compareTo(product2.getChange()));
        }
        storageManager.set(products);
        defaultListModel.removeAllElements();
        storageManager.get().forEach((element) -> defaultListModel.addElement(element));
    }

    /**
     * Filters the JList and {@link controller.ProductManager} given the URL.
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
        storageManager.get().forEach((element) -> defaultListModel.addElement(element));
        isFilter = true;
    }

    /**
     * Removes the current filter applied to the JList and
     * {@link controller.ProductManager}.
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
