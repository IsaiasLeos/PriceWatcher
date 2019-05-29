package view;

import controller.PriceFinder;
import model.Product;
import storage.StorageManager;
import utils.Sorting;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class HW4 extends HW3 {

    private DefaultListModel<Product> defaultListModel;
    private StorageManager storageManager;
    private Sorting sortAlgorithm;
    private JPopupMenu popupMenu;
    private JList viewListCell;
    private JProgressBar download;

    public static void main(String[] args) {
        new HW4();
    }

    /**
     * Create and Configure the GUI.
     */
    @Override
    protected void createUI() {
        JPanel drawingBoard = new JPanel();
        popupMenu = createJPopupMenu();
        storageManager = new StorageManager();
        defaultListModel = createListModel(storageManager);
        viewListCell = createJList(defaultListModel);
        sortAlgorithm = new Sorting(storageManager, defaultListModel);
        viewListCell.addMouseListener(mouseListener());
        viewListCell.addMouseMotionListener(mouseMotionListener());
        viewListCell.setVisibleRowCount(3);
        download = new JProgressBar();
        download.setVisible(false);
        drawingBoard.add(new JScrollPane(viewListCell), BorderLayout.CENTER);
        drawingBoard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 16, 0, 16),
                BorderFactory.createLineBorder(Color.WHITE)));
        drawingBoard.setLayout(new GridLayout(1, 1));
        setJMenuBar(createJMenuBar());
        add(createJToolBar("ToolBar"), BorderLayout.NORTH);
        add(drawingBoard, BorderLayout.CENTER);
        add(download, BorderLayout.AFTER_LAST_LINE);
    }

    /**
     * Listen to when the mouse is either right clicking or left clicking on the
     * JList.
     *
     * @return mouse listener
     */
    @Override
    protected MouseAdapter mouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    popupMenu.show(viewListCell, mouseEvent.getX(), mouseEvent.getY());
                }
                if (mouseEvent.getClickCount() == 1) {
                    for (int i = 0; i < defaultListModel.getSize(); i++) {
                        if (mouseEvent.getX() < 40
                                && mouseEvent.getX() > 22
                                && mouseEvent.getY() < 32 + (i * 160)
                                && mouseEvent.getY() > 12 + (i * 160)) {
                            openClickableActionWeb();
                        }
                    }
                }
            }
        };
    }

    /**
     * Listen to when the mouse is moving or clicking on certain areas of the
     * JList.
     *
     * @return mouse motion listener
     */
    @Override
    protected MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() < 40
                        && e.getX() > 22
                        && e.getY() < 32 + (viewListCell.getSelectedIndex() * 160)
                        && e.getY() > 12 + (viewListCell.getSelectedIndex() * 160)
                        && viewListCell.getSelectedIndex() != -1) {
                    if (defaultListModel.get(viewListCell.getSelectedIndex()).getURL().contains("ebay")) {
                        defaultListModel.get(viewListCell.getSelectedIndex()).setIcon("ebay.png");
                    }
                    if (defaultListModel.get(viewListCell.getSelectedIndex()).getURL().contains("amazon")) {
                        defaultListModel.get(viewListCell.getSelectedIndex()).setIcon("amazon.png");
                    }
                    if (defaultListModel.get(viewListCell.getSelectedIndex()).getURL().contains("walmart")) {
                        defaultListModel.get(viewListCell.getSelectedIndex()).setIcon("walmart.png");
                    }
                    repaint();
                } else if (viewListCell.getSelectedIndex() > -1) {
                    defaultListModel.get(viewListCell.getSelectedIndex()).setIcon("webbrowser.png");
                    repaint();
                }
            }
        };
    }

    /**
     * @param product product to be updated
     */
    private void setPrice(Product product) {

        PriceFinder webPrice = new PriceFinder();
        new Thread(() -> {
            int progress = 0;
            download.setVisible(true);
            double price = webPrice.getPrice(product.getURL());
            progress += 30;
            download.setValue(progress);
            if (price == -1) {
                JLabel label = new JLabel(""
                        + "<html>"
                        + "<center>"
                        + "Error downloading information!<br>The following is not supported or has no current price available.<br>Want to open it in your browser?"
                        + "</center>"
                        + "</html>");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                int option = JOptionPane.showConfirmDialog(HW4.this,
                        label,
                        "Error",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "network.png"))));
                if (option == 0) {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI(product.getURL()));
                        } catch (URISyntaxException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                progress += 15;
                download.setValue(progress);
                if (product.getStartingPrice() == 0) {
                    product.setStartingPrice(price);
                    product.setCurrentPrice(price);
                } else {
                    product.checkPrice(price);
                }
                progress += 30;
                download.setValue(progress);
                storageManager.toStorage(storageManager.toJSON());
                progress += 25;
                download.setValue(progress);
                repaint();
            }
            progress = 0;
            download.setValue(progress);
            download.setVisible(false);
        }).start();
    }

    /**
     * Refreshes the list of products given within the {@link JList}
     *
     * @param event event
     */
    @Override
    protected void refreshButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            for (int i = 0; i < defaultListModel.getSize(); i++) {
                setPrice(defaultListModel.get(i));
            }
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Product List is Empty");
        }

    }

    /**
     * Refreshes the selected index inside of the {@link JList}
     *
     * @param event event
     */
    @Override
    protected void singleRefreshButtonClicked(ActionEvent event) {
        if (viewListCell.getSelectedIndex() > -1) {
            setPrice(defaultListModel.get(viewListCell.getSelectedIndex()));
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Not Selecting an Item");
        }
    }

    /**
     * Adds a {@link Product} to the current {@link JList}.There must be a
     * given name, URL, and price of the product. Date will be given to whatever
     * the given date is.
     */
    @Override
    protected void addButtonClicked(ActionEvent event) {
        JTextField name = new JTextField();
        JTextField url = new JTextField();
        JTextField price = new JTextField("0.0");
        price.setEditable(false);
        Object[] message = {
                "Product Name:", name,
                "Product URL:", url,
                "Product Price:", price
        };
        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Add",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "plus.png"))));
        //OK
        if (option == 0) {
            try {
                Product generatedProduct = new Product(url.getText(),
                        name.getText(),
                        super.getCurrentDate(),
                        0.0,
                        0.0,
                        0.0,
                        null,
                        false);
                setPrice(generatedProduct);
                defaultListModel.addElement(generatedProduct);
                storageManager.add(generatedProduct);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Please re-enter correct information.\n" + e);
            }
        }
        repaint();
    }

    /**
     * Searches for the {@link Product} Name and displays product that
     * contain the same letters while ignoring capitalization.
     *
     * @param event event
     */
    @Override
    protected void searchButtonClicked(ActionEvent event) {
        JTextField search = new JTextField();
        Object[] message = {
                "Search:", search
        };
        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Add",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "plus.png"))));
        if (option == 0) {
            sortAlgorithm.filterBy(search.getText());
            repaint();
        }
    }

    /**
     * Deletes the currently selected cell within the {@link JList}.
     *
     * @param event event
     */
    @Override
    protected void deleteButtonClicked(ActionEvent event) {
        if (viewListCell.getSelectedIndex() > -1) {
            int selected = JOptionPane.showConfirmDialog(this,
                    "Do you want to delete this item?",
                    "Delete", JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "delete.png"))));
            if (selected == 0) {
                storageManager.delete(viewListCell.getSelectedIndex());
                defaultListModel.remove(viewListCell.getSelectedIndex());
                storageManager.toStorage(storageManager.toJSON());
                repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not Selecting an Item.");
        }
    }

    /**
     * Edits the currently selected cell within the {@link JList}. While
     * editing, the current information of the selected cell's product will be
     * displayed within the {@link JOptionPane} given.
     *
     * @param event event
     */
    @Override
    protected void editButtonClicked(ActionEvent event) {
        if (viewListCell.getSelectedIndex() > -1) {
            Product generatedProduct = defaultListModel.get(viewListCell.getSelectedIndex());
            JTextField name = new JTextField(generatedProduct.getName());
            JTextField url = new JTextField(generatedProduct.getURL(), 5);
            JTextField price = new JTextField("" + (generatedProduct.getCurrentPrice()));
            price.setEditable(false);
            Object[] message = {
                    "Product Name:", name,
                    "Product URL:", url,
                    "Product Price:", price
            };
            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Edit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "edit.png"))));
            if (option == 0) {
                try {
                    defaultListModel.get(viewListCell.getSelectedIndex()).setName(name.getText());
                    defaultListModel.get(viewListCell.getSelectedIndex()).setURL(url.getText());
                    setPrice(defaultListModel.get(viewListCell.getSelectedIndex()));
                    repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please re-enter correct information.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not selecting an item.");
        }
    }

    /**
     *
     */
    @Override
    protected void openClickableActionWeb() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(defaultListModel.get(viewListCell.getSelectedIndex()).getURL()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Exits the program.
     */
    @Override
    protected void exitButtonClicked(ActionEvent event) {
        super.exitButtonClicked(event);
    }

    @Override
    protected void moveDownButtonClicked(ActionEvent event) {
        int size = defaultListModel.getSize();
        if (size > -1) {
            viewListCell.setSelectedIndex(size - 1);
        } else {
            JOptionPane.showMessageDialog(this, "No items available.");
        }
    }

    @Override
    protected void moveUpButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            viewListCell.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "No items available.");
        }
    }

    /**
     * Creates a {@link JToolBar} with the given
     * buttons.<li>{@link #refreshButtonClicked(ActionEvent)}</li>
     * <li>{@link #addButtonClicked(ActionEvent)}</li>
     * <li>{@link #searchButtonClicked(ActionEvent)}</li>
     * <li>{@link #moveUpButtonClicked(ActionEvent)}</li>
     * <li>{@link #moveDownButtonClicked(ActionEvent)}</li>
     * <li>{@link #singleRefreshButtonClicked(ActionEvent)}</li>
     * <li>{@link #openClickableActionWeb()}</li>
     * <li>{@link #deleteButtonClicked(ActionEvent)}</li>
     * <li>{@link #editButtonClicked(ActionEvent)}</li>
     *
     * @param title title of toolbar
     * @return JToolBar
     */
    @Override
    protected JToolBar createJToolBar(String title) {
        return super.createJToolBar(title);
    }

    /**
     * Create a button with the given label and tool-tip.
     *
     * @param label   name of the resource (image)
     * @param tooltip hover text
     * @return a button
     */
    @Override
    protected JButton createJButton(String label, String tooltip) {
        return super.createJButton(label, tooltip);
    }

    /**
     * Creates a {@link JPopupMenu} with the given actions. To be used on a
     * JList area only.
     * <li>{@link #singleRefreshButtonClicked(ActionEvent) }</li>
     * <li>{@link #openClickableActionWeb()}</li>
     * <li>{@link #editButtonClicked(ActionEvent) }</li>
     * <li>{@link #deleteButtonClicked(ActionEvent) }</li>
     *
     * @return a pop-up menu of items
     */
    private JPopupMenu createJPopupMenu() {
        JPopupMenu generatedPopupMenu = new JPopupMenu();
        JMenuItem price = createJMenuItem("Price", "checkmark.png", "Check Prices");
        price.addActionListener(this::singleRefreshButtonClicked);
        generatedPopupMenu.add(price);
        JMenuItem view = createJMenuItem("View", "webbrowser.png", "View Item in Browser");
        view.addActionListener((event) -> this.openClickableActionWeb());
        generatedPopupMenu.add(view);
        JMenuItem edit = createJMenuItem("Edit", "edit.png", "Edit selected Item");
        edit.addActionListener(this::editButtonClicked);
        generatedPopupMenu.add(edit);
        JMenuItem remove = createJMenuItem("Remove", "delete.png", "Delete Selected Item");
        remove.addActionListener(this::deleteButtonClicked);
        generatedPopupMenu.add(remove);
        generatedPopupMenu.addSeparator();
        JMenu filterMenu = new JMenu("Sort");
        ButtonGroup buttonGroup = new ButtonGroup();
        JMenuItem removeFilter = new JRadioButtonMenuItem("Remove Filter");
        removeFilter.addActionListener((event) -> this.sortAlgorithm.removeFilter());
        buttonGroup.add(removeFilter);
        filterMenu.add(removeFilter);
        JMenuItem filterAmazon = new JRadioButtonMenuItem("Amazon");
        filterAmazon.addActionListener((event) -> this.sortAlgorithm.filterBy("amazon"));
        buttonGroup.add(filterAmazon);
        filterMenu.add(filterAmazon);
        JMenuItem filterEbay = new JRadioButtonMenuItem("Ebay");
        filterEbay.addActionListener((event) -> this.sortAlgorithm.filterBy("ebay"));
        buttonGroup.add(filterEbay);
        filterMenu.add(filterEbay);
        JMenuItem filterWalmart = new JRadioButtonMenuItem("Walmart");
        filterWalmart.addActionListener((event) -> this.sortAlgorithm.filterBy("walmart"));
        buttonGroup.add(filterWalmart);
        filterMenu.add(filterWalmart);
        filterMenu.addSeparator();
        JMenuItem cname = new JMenuItem("Copy Name");
        generatedPopupMenu.add(filterMenu);
        generatedPopupMenu.addSeparator();
        cname.addActionListener((event) -> this.toClipboard(1));
        generatedPopupMenu.add(cname);
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.toClipboard(2));
        generatedPopupMenu.add(curl);
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.toClipboard(3));
        generatedPopupMenu.add(citem);

        return generatedPopupMenu;
    }

    /**
     * Creates a {@link DefaultListModel} of the Product Manager.
     *
     * @param storageManager manages storage
     * @return default list of the program
     */
    private DefaultListModel createListModel(StorageManager storageManager) {
        DefaultListModel generatedListModel = new DefaultListModel<>();
        try {
            storageManager.fromJSON();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        storageManager.get().forEach(generatedListModel::addElement);
        return generatedListModel;
    }

    /**
     * Creates a {@link JList} from a {@link DefaultListModel}.
     *
     * @param defaultListModel view list model
     * @return default list model
     */
    @Override
    protected JList createJList(DefaultListModel defaultListModel) {
        return super.createJList(defaultListModel);
    }

    /**
     * Switches the theme of the applications.
     *
     * @param theme name of theme
     */
    private void switchThemes(String theme) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (theme.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(this, "Failed to apply themes.");
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Creates a {@link JMenuBar} that contains the creator information, actions
     * for {@link Product} and sorting for the JList.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar fileMenuBar = new JMenuBar();

        JMenu themeMenu = new JMenu("Themes");
        JMenuItem themeWindows = new JMenuItem("Windows");
        themeWindows.addActionListener((event) -> this.switchThemes("Windows"));
        themeMenu.add(themeWindows);
        JMenuItem themeWindowsClassic = new JMenuItem("Windows Classic");
        themeWindowsClassic.addActionListener((event) -> this.switchThemes("Windows Classic"));
        themeMenu.add(themeWindowsClassic);
        JMenuItem themeWindowsMetal = new JMenuItem("Metal");
        themeWindowsMetal.addActionListener((event) -> this.switchThemes("Metal"));
        themeMenu.add(themeWindowsMetal);
        JMenuItem themeWindowsNimbus = new JMenuItem("Nimbus");
        themeWindowsNimbus.addActionListener((event) -> this.switchThemes("Nimbus"));
        themeMenu.add(themeWindowsNimbus);
        JMenuItem themeWindowsCDEMotif = new JMenuItem("CDE/Motif");
        themeWindowsCDEMotif.addActionListener((event) -> this.switchThemes("CDE/Motif"));
        themeMenu.add(themeWindowsCDEMotif);

        JMenu appMenu = new JMenu("App");
        JMenuItem about = createJMenuItem("About", "App Information", "about.png", KeyEvent.VK_A, ActionEvent.CTRL_MASK);
        about.addActionListener((event) -> aboutButtonClicked());
        appMenu.add(about);
        JMenuItem exit = createJMenuItem("Exit", "Exit Program", "plus.png", KeyEvent.VK_X, ActionEvent.CTRL_MASK);
        exit.addActionListener(this::exitButtonClicked);
        exit.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "plus.png"))));
        appMenu.add(exit);

        JMenu editMenu = new JMenu("Item");
        JMenuItem check = createJMenuItem("Check Prices", "Check Item Prices", "checkmark.png", KeyEvent.VK_C, ActionEvent.ALT_MASK);
        check.addActionListener(this::refreshButtonClicked);
        editMenu.add(check);
        JMenuItem add = createJMenuItem("Add", "Add a Product", "plus.png", KeyEvent.VK_A, ActionEvent.ALT_MASK);
        add.addActionListener(this::addButtonClicked);
        editMenu.add(add);
        editMenu.add(new JSeparator());
        JMenuItem search = createJMenuItem("Search", "Search for an Item", "search.png", KeyEvent.VK_S, ActionEvent.ALT_MASK);
        search.addActionListener(this::searchButtonClicked);
        search.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "search.png"))));
        editMenu.add(search);
        JMenuItem forward = createJMenuItem("Select First", "Move to First Item", "up.png", KeyEvent.VK_UP, ActionEvent.ALT_MASK);
        forward.addActionListener(this::moveUpButtonClicked);
        editMenu.add(forward);
        JMenuItem backward = createJMenuItem("Select Last", "Move to Last Item", "down.png", KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
        backward.addActionListener(this::moveDownButtonClicked);
        backward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/" + "down.png"))));
        editMenu.add(backward);

        JMenu sortMenu = new JMenu("Sort");
        ButtonGroup buttonGroup = new ButtonGroup();
        JMenuItem oldest = new JRadioButtonMenuItem("Oldest");
        oldest.addActionListener((event) -> this.sortAlgorithm.sortBy(0));
        buttonGroup.add(oldest);
        sortMenu.add(oldest);
        JMenuItem newest = new JRadioButtonMenuItem("Newest");
        newest.addActionListener((event) -> this.sortAlgorithm.sortBy(1));
        buttonGroup.add(newest);
        sortMenu.add(newest);
        sortMenu.addSeparator();
        JMenuItem ascend = new JRadioButtonMenuItem("Ascending Order");
        ascend.addActionListener((event) -> this.sortAlgorithm.sortBy(2));
        buttonGroup.add(ascend);
        sortMenu.add(ascend);
        JMenuItem descend = new JRadioButtonMenuItem("Descending Order");
        descend.addActionListener((event) -> this.sortAlgorithm.sortBy(3));
        buttonGroup.add(descend);
        sortMenu.add(descend);
        sortMenu.addSeparator();
        JMenuItem low = new JRadioButtonMenuItem("Lowest Price ($)");
        low.addActionListener((event) -> this.sortAlgorithm.sortBy(4));
        buttonGroup.add(low);
        sortMenu.add(low);
        JMenuItem high = new JRadioButtonMenuItem("Highest Price ($)");
        high.addActionListener((event) -> this.sortAlgorithm.sortBy(5));
        buttonGroup.add(high);
        sortMenu.add(high);
        JMenuItem priceChangeHigh = new JRadioButtonMenuItem("High Price Change (%)");
        priceChangeHigh.addActionListener((event) -> this.sortAlgorithm.sortBy(6));
        buttonGroup.add(priceChangeHigh);
        sortMenu.add(priceChangeHigh);
        JMenuItem priceChangeLow = new JRadioButtonMenuItem("Low Price Change (%)");
        priceChangeLow.addActionListener((event) -> this.sortAlgorithm.sortBy(7));
        buttonGroup.add(priceChangeLow);
        sortMenu.add(priceChangeLow);
        JMenu generatedNestedMenu = new JMenu("Selected");
        JMenuItem priceNested = createJMenuItem("Price", "checkmark.png", "Check Prices");
        priceNested.addActionListener(this::singleRefreshButtonClicked);
        generatedNestedMenu.add(priceNested);
        JMenuItem viewNested = createJMenuItem("View", "webbrowser.png", "View Item on Browser");
        viewNested.addActionListener((event) -> this.openClickableActionWeb());
        generatedNestedMenu.add(viewNested);
        JMenuItem editNested = createJMenuItem("Edit", "edit.png", "Edit selected Item");
        editNested.addActionListener(this::editButtonClicked);
        generatedNestedMenu.add(editNested);
        JMenuItem removeNested = createJMenuItem("Remove", "delete.png", "Delete selected Item");
        removeNested.addActionListener(this::deleteButtonClicked);
        generatedNestedMenu.add(removeNested);
        generatedNestedMenu.addSeparator();
        JMenuItem cname = new JMenuItem("Copy Name");
        cname.addActionListener((event) -> this.toClipboard(1));
        generatedNestedMenu.add(cname);
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.toClipboard(2));
        generatedNestedMenu.add(curl);
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.toClipboard(3));
        generatedNestedMenu.add(citem);
        editMenu.add(generatedNestedMenu);

        fileMenuBar.add(appMenu);
        fileMenuBar.add(editMenu);
        fileMenuBar.add(sortMenu);
        fileMenuBar.add(themeMenu);
        return fileMenuBar;
    }

    /**
     * Copies information inside of the JList to the System Clipboard.
     * Option 1 will obtain the name and put the information into the Clipboard.
     * Option 2 will obtain the URLand put the information into the Clipboard.
     * Option 3 will obtain the whole product information and put the
     * information into the Clipboard..
     *
     * @param option product information.
     */
    @Override
    protected void toClipboard(int option) {
        if (viewListCell.getSelectedIndex() > -1) {
            StringSelection selection = new StringSelection("");
            Clipboard clipboard;
            switch (option) {
                case 1:
                    selection = new StringSelection(defaultListModel.get(viewListCell.getSelectedIndex()).getName());
                    break;
                case 2:
                    selection = new StringSelection(defaultListModel.get(viewListCell.getSelectedIndex()).getURL());
                    break;
                case 3:
                    Product toClipboard = defaultListModel.get(viewListCell.getSelectedIndex());
                    selection = new StringSelection(
                            "Name:  " + toClipboard.getName() + "\n"
                                    + "URL:  " + toClipboard.getURL() + "\n"
                                    + "Price:  " + toClipboard.getCurrentPrice() + "\n"
                                    + "Change:  " + toClipboard.getChange() + "\n"
                                    + "Date Added:  " + toClipboard.getDate() + "\n"
                    );
                    break;
            }
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    /**
     * Creates a {@link JMenuItem} with the given label, key presses and if CTRL
     * or ALT is being pressed.
     *
     * @param label    name of the item
     * @param tooltip  hover text
     * @param itemName item name
     * @param key      {@link KeyEvent}
     * @param mask     modifier
     * @return jmenu
     */
    @Override
    protected JMenuItem createJMenuItem(String label, String tooltip, String itemName, int key, int mask) {
        return super.createJMenuItem(label, tooltip, itemName, key, mask);
    }

    /**
     * Creates a {@link JMenuItem} with the given parameter of a label from the
     * name of the button, name of the image file, and tool tip description.
     * Used to create a JMenuBar.
     *
     * @param label name of menu item
     * @return the JMenuItem
     */
    @Override
    protected JMenuItem createJMenuItem(String label, String imageName, String tooltip) {
        return super.createJMenuItem(label, imageName, tooltip);
    }

}
