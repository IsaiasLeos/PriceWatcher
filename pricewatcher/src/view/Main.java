package view;

import controller.PriceFinder;
import controller.ProductManager;
import model.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    private int time = 4;

    private JLabel msgBar = new JLabel("");
    private JList jListRenderer;
    private JPopupMenu popupMenu;
    private DefaultListModel<Product> defaultListModel;

    private final static String RESOURCE_DIR = "resources/";
    private final static Dimension DEFAULT_SIZE = new Dimension(600, 400);

    private Product product;
    private Renderer renderer;
    private ProductManager originalProductManager;
    private PriceFinder webPrice;

    /**
     * Create a Dialog of Default Size (600,400).
     */
    public Main() {
        this(DEFAULT_SIZE);
    }

    /**
     * Create a new dialog of the given Dimensions (600,400).
     *
     * @param dim
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Main(Dimension dim) {
        super("Price Watcher");
        createDefaultProduct();
        setLayout(new BorderLayout());
        setSize(dim);
        createGUI();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        showMessage("Welcome!", time);
        pack();
    }

    /**
     * Create and Configure the GUI.
     *
     */
    private void createGUI() {
        JPanel controlPanel = createControlPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        add(controlPanel, BorderLayout.CENTER);
        JPanel drawingBoard = new JPanel();
        jListRenderer = createJList(defaultListModel);
        mouseListener();
        mouseMotionListener();
        jListRenderer.setVisibleRowCount(3);
        drawingBoard.add(new JScrollPane(jListRenderer));
        drawingBoard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 16, 0, 16),
                BorderFactory.createLineBorder(Color.WHITE)));
        drawingBoard.setLayout(new GridLayout(1, 1));
        add(drawingBoard, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /**
     * Create a control panel consisting of a Tool Bar, Menu Bar, and a Pop-up
     * Menu.
     */
    private JPanel createControlPanel() {
        createJPopupMenu();
        createJMenuBar();
        add(createJToolBar("Toolbar"), BorderLayout.NORTH);
        return new JPanel();
    }

    /**
     * Show briefly the given string in the message bar.
     */
    private void showMessage(String msg, int time) {
        msgBar.setText(msg);
        new Thread(() -> {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
            }
            if (msg.equals(msgBar.getText())) {
                SwingUtilities.invokeLater(() -> msgBar.setText(" "));
            }
        }).start();
    }

    /**
     * Listen to when the mouse is either right clicking or left clicking on the
     * JList.
     *
     * @param mouseEvent
     */
    private void mouseListener() {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    popupMenu.show(jListRenderer, mouseEvent.getX(), mouseEvent.getY());
                }
                if (mouseEvent.getClickCount() == 1) {
                    for (int i = 0; i < defaultListModel.getSize(); i++) {
                        if ((mouseEvent.getX() < 40 && mouseEvent.getX() > 22) && (mouseEvent.getY() < 32 + (i * 160) && mouseEvent.getY() > 12 + (i * 160))) {
                            openWeb();
                        }
                    }
                }
                if (mouseEvent.getClickCount() == 2) {
                    openWeb();
                }
            }
        };
        jListRenderer.addMouseListener(mouseListener);
    }

    /**
     * Listen to when the mouse is moving or clicking on certain areas of the
     * JList.
     *
     * @param mouseEvent
     */
    private void mouseMotionListener() {
        MouseMotionListener mouseMotion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if ((e.getX() < 40 && e.getX() > 22) && (e.getY() < 32 + (jListRenderer.getSelectedIndex() * 160) && e.getY() > 12 + (jListRenderer.getSelectedIndex() * 160)) && jListRenderer.getSelectedIndex() != -1) {
                    if (defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL().contains("ebay")) {
                        defaultListModel.get(jListRenderer.getSelectedIndex()).setProductIcon("ebay.png");
                    }
                    if (defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL().contains("amazon")) {
                        defaultListModel.get(jListRenderer.getSelectedIndex()).setProductIcon("amazon.png");
                    }
                    if (defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL().contains("walmart")) {
                        defaultListModel.get(jListRenderer.getSelectedIndex()).setProductIcon("walmart.png");
                    }
                    repaint();
                } else if (jListRenderer.getSelectedIndex() > -1) {
                    defaultListModel.get(jListRenderer.getSelectedIndex()).setProductIcon("webbrowser.png");
                    repaint();
                }
            }
        };
        jListRenderer.addMouseMotionListener(mouseMotion);

    }

    /**
     * Create a {@link model.Product} given the parameters.
     *
     * @param itemURL the URL that links to the item
     * @param itemName the name of the product
     * @param itemPrice the initial price of the item
     * @param itemDateAdded the date of when the product was added
     * @return
     */
    private Product createProduct(String itemURL, String itemName, double itemPrice, String itemDateAdded) {
        return new Product(itemURL, itemName, itemPrice, itemDateAdded);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private void createDefaultProduct() {
        originalProductManager = new ProductManager();
        String productURL = "https://www.amazon.com/Nintendo-Console-Resolution-Surround-Customize/dp/B07M5ZQSKV";
        String productName = "Nintendo Switch";
        double productInitialPrice = 359.99;
        String productDateAdded = "1/30/2019";
        originalProductManager.add(createProduct(productURL, productName, productInitialPrice, productDateAdded));
        defaultListModel = createListModel(originalProductManager);
        webPrice = new PriceFinder();
    }

    /**
     * Refreshes the list of products given within the {@link JList}
     *
     * @param event
     */
    @SuppressWarnings("deprecation")
    private void refreshButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() != 0) {
            for (int i = 0; i < defaultListModel.getSize(); i++) {
                defaultListModel.get(i).checkPrice(webPrice.getSimulatedPrice(defaultListModel.get(i).getProductURL()));
            }
            repaint();
            showMessage("Refreshing...", time);
        } else {
            showMessage("Product List is Empty", time);
        }

    }

    /**
     * Refreshes the selected index inside of the {@link JList}
     *
     * @param event
     */
    @SuppressWarnings("deprecation")
    private void singleRefreshButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            defaultListModel.get(jListRenderer.getSelectedIndex()).checkPrice(webPrice.getSimulatedPrice(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL()));
            repaint();
            showMessage("Refreshing...", time);
        } else {
            showMessage("Not Selecting an Item", time);
        }

    }

    /**
     * Adds a {@link model.Product} to the current {@link JList}. There must be
     * a given name, URL, and price of the product. Date will be given to
     * whatever the given date is.
     *
     * @param event
     */
    private void addButtonClicked(ActionEvent event) {
        JTextField name = new JTextField();
        JTextField url = new JTextField();
        JTextField price = new JTextField();
        Object[] message = {
            "Product Name:", name,
            "Product URL:", url,
            "Product Price:", price
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        //OK
        if (option == 0) {
            try {
                Product generatedProduct = createProduct(url.getText(), name.getText(), Double.parseDouble(price.getText()), getCurrentDate());
                defaultListModel.addElement(generatedProduct);
                showMessage("Product Successfully Added", time);
            } catch (NumberFormatException e) {
                showMessage("Please re-enter correct information.", time);
            } catch (IllegalArgumentException e) {
                showMessage("Please re-enter correct information.", time);
            }
        }
        //Cancel 
        if (option == 2) {

        }
        //Closed
        if (option == -1) {

        }
    }

    /**
     * Gets the current date.
     *
     * @return current date in MM/dd/yyyy format
     *
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /**
     * Searches for the {@link model.Product} Name and displays product that
     * contain the same letters while ignoring capitalization.
     *
     * @param event
     */
    private void searchButtonClicked(ActionEvent event) {
        JTextField search = new JTextField();
        Object[] message = {
            "Search:", search
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        if (option == 0) {
            for (int i = 0; i < defaultListModel.getSize(); i++) {
                if (defaultListModel.get(i).getProductName().toLowerCase().contains(search.getText().toLowerCase())) {

                }
            }
            repaint();
        }
    }

    /**
     * Moves to the top of cell within the {@link JList}.
     *
     * @param event
     */
    private void moveUpButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            jListRenderer.setSelectedIndex(0);
        }
    }

    /**
     * Moves to the last of the cell within the {@link JList}.
     *
     * @param event
     */
    private void moveDownButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            jListRenderer.setSelectedIndex(defaultListModel.getSize() - 1);

        }
    }

    /**
     * Deletes the currently selected cell within the {@link JList}.
     *
     * @param event
     */
    private void deleteButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            defaultListModel.remove(jListRenderer.getSelectedIndex());
            repaint();
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     * Edits the currently selected cell within the {@link JList}. While
     * editing, the current information of the selected cell's product will be
     * displayed within the {@link JOptionPane} given.
     *
     * @param event
     */
    private void editButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            Product generatedProduct = defaultListModel.get(jListRenderer.getSelectedIndex());
            JTextField name = new JTextField(generatedProduct.getProductName());
            JTextField url = new JTextField(generatedProduct.getProductURL(), 5);
            JTextField price = new JTextField("" + (generatedProduct.getInitialPrice()));
            Object[] message = {
                "Product Name:", name,
                "Product URL:", url,
                "Product Price:", price
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Edit", JOptionPane.PLAIN_MESSAGE, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
            if (option == 0) {
                try {
                    originalProductManager.delete(product);
                    defaultListModel.get(jListRenderer.getSelectedIndex()).setProductName(name.getText());
                    defaultListModel.get(jListRenderer.getSelectedIndex()).setCurrentURL(url.getText());
                    defaultListModel.get(jListRenderer.getSelectedIndex()).setInitialPrice(Double.parseDouble(price.getText()));
                    defaultListModel.get(jListRenderer.getSelectedIndex()).setProductPrice(Double.parseDouble(price.getText()));
                    originalProductManager.add(product);
                    repaint();
                    showMessage("Succesfully Edited a Product", time);
                } catch (NumberFormatException e) {
                    showMessage("Please re-enter correct information.", time);
                }
            }
            //Close Button
            if (option == -1) {

            }
            //Cancel
            if (option == 2) {

            }
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     * Redirects the user to the {@link URI} of the currently selected cell or
     * {@link model.Product}
     *
     * @param event
     */
    private void openWeb(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL()));

                } catch (URISyntaxException | IOException ex) {
                }
            }
            showMessage("Opening Webpage", time);
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     * Redirects the user to the {@link URI} of the currently selected cell or
     * {@link model.Product}.
     */
    private void openWeb() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL()));
            } catch (URISyntaxException | IOException ex) {
            }
        }
    }

    /**
     * Shows a {@link JLabel} with the information of who worked on this project
     * and a link to the source code.
     *
     * @param event
     */
    private void aboutButtonClicked(ActionEvent event) {
        JLabel label = new JLabel("<html>"
                + "<center>"
                + "<strong>PriceWatcher v3.6</strong>"
                + "<br>Creators:<br><br><i><sup>Isaias Leos<br>Leslie Gomez</sup>"
                + "</i><br><strong><a href=\"https://github.com/IsaiasLeos/PriceWatcher\">Github</a><strong></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/IsaiasLeos/PriceWatcher"));
                } catch (URISyntaxException | IOException error) {
                    error.printStackTrace();
                }
            }
        });
        JOptionPane.showMessageDialog(null, label, "About", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Exits the program.
     *
     * @param event
     */
    private void exitButtonClicked(ActionEvent event) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     *
     * @param event
     */
    private void sortOld(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNew(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNameAscending(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNameDescending(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortHigh(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortLow(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortChange(ActionEvent event) {
    }

    /**
     * Creates a {@link JToolBar} with the given buttons.
     * <li>{@link #refreshButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #addButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #searchButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #moveUpButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #moveDownButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #singleRefreshButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #openWeb(java.awt.event.ActionEvent)}</li>
     * <li>{@link #deleteButtonClicked(java.awt.event.ActionEvent)}</li>
     * <li>{@link #editButtonClicked(java.awt.event.ActionEvent) }</li>
     *
     * @return JToolBar
     */
    private JToolBar createJToolBar(String title) {
        JToolBar toolBar = new JToolBar(title);
        JButton checkmark = createJButton("checkmark.png", "Check Item Prices", false);
        checkmark.addActionListener((event) -> this.refreshButtonClicked(event));
        toolBar.add(checkmark);
        JButton add = createJButton("plus.png", "Add Product", false);
        add.addActionListener((event) -> this.addButtonClicked(event));
        toolBar.add(add);
        JButton search = createJButton("search.png", "Search for a Item", false);
        search.addActionListener((event) -> this.searchButtonClicked(event));
        toolBar.add(search);
        JButton upButton = createJButton("up.png", "Move to First Item", false);
        upButton.addActionListener((event) -> this.moveUpButtonClicked(event));
        toolBar.add(upButton);
        JButton downButton = createJButton("down.png", "Move to Last Item", false);
        downButton.addActionListener((event) -> this.moveDownButtonClicked(event));
        toolBar.add(downButton);
        toolBar.addSeparator();
        JButton singleRefresh = createJButton("refresh.png", "Refresh Selected Item", false);
        singleRefresh.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        toolBar.add(singleRefresh);
        JButton openLink = createJButton("webbrowser.png", "Open in Browser", false);
        openLink.addActionListener((event) -> this.openWeb(event));
        toolBar.add(openLink);
        JButton delete = createJButton("delete.png", "Delete Selected Item", false);
        delete.addActionListener((event) -> this.deleteButtonClicked(event));
        toolBar.add(delete);
        JButton edit = createJButton("edit.png", "Edit Selected Item", false);
        edit.addActionListener((event) -> this.editButtonClicked(event));
        toolBar.add(edit);
        toolBar.addSeparator();
        JButton about = createJButton("about.png", "App Information", false);
        about.addActionListener((event) -> this.aboutButtonClicked(event));
        toolBar.add(about);
        return toolBar;
    }

    /**
     * Creates a {@link JPopupMenu} with the given actions. To be used on a
     * JList area only.
     * <li>{@link #singleRefreshButtonClicked(java.awt.event.ActionEvent) }</li>
     * <li>{@link #openWeb(java.awt.event.ActionEvent) }</li>
     * <li>{@link #editButtonClicked(java.awt.event.ActionEvent) }</li>
     * <li>{@link #deleteButtonClicked(java.awt.event.ActionEvent) }</li>
     * <li>{@link #copyToClipboard(java.lang.String) }</li>
     *
     * @return a pop-up menu of items
     */
    private void createJPopupMenu() {
        JPopupMenu generatedPopupMenu = new JPopupMenu();
        JMenuItem price = createJMenutItem("Price", "checkmark.png", "Check Prices");
        price.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        JMenuItem view = createJMenutItem("View", "webbrowser.png", "View Item in Browser");
        view.addActionListener((event) -> this.openWeb(event));
        JMenuItem edit = createJMenutItem("Edit", "edit.png", "Edit selected Item");
        edit.addActionListener((event) -> this.editButtonClicked(event));
        JMenuItem remove = createJMenutItem("Remove", "delete.png", "Delete Selected Item");
        remove.addActionListener((event) -> this.deleteButtonClicked(event));
        JMenuItem cname = new JMenuItem("Copy Name");
        cname.addActionListener((event) -> this.copyToClipboard(1));
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.copyToClipboard(2));
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.copyToClipboard(3));
        generatedPopupMenu.add(price);
        generatedPopupMenu.add(view);
        generatedPopupMenu.add(edit);
        generatedPopupMenu.add(remove);
        generatedPopupMenu.addSeparator();
        generatedPopupMenu.add(cname);
        generatedPopupMenu.add(curl);
        generatedPopupMenu.add(citem);
        popupMenu = generatedPopupMenu;
    }

    /**
     * Creates a {@link DefaultListModel} of the {@link ProductManager}.
     *
     * @param originalProductManager
     * @return
     */
    public DefaultListModel createListModel(ProductManager originalProductManager) {
        DefaultListModel generatedListModel = new DefaultListModel<>();
        originalProductManager.getProducts().forEach(generatedListModel::addElement);
        return generatedListModel;
    }

    /**
     * Creates a {@link JList} from a {@link DefaultListModel}.
     *
     * @return
     */
    private JList createJList(DefaultListModel defaultListModel) {
        JList generatedJList = new JList<>(defaultListModel);
        renderer = new Renderer();
        generatedJList.setCellRenderer(renderer);
        return generatedJList;
    }

    /**
     * Creates a {@link JMenuBar} that contains the creator information, actions
     * for {@link model.Product} and sorting for the JList.
     */
    private void createJMenuBar() {
        JMenuBar fileMenuBar = new JMenuBar();
        JMenu appMenu = new JMenu("App");
        JMenu editMenu = new JMenu("Item");
        JMenu sortMenu = new JMenu("Sort");
        JMenuItem about = createJMenuItem("About", "App Information", "about.png", KeyEvent.VK_A, ActionEvent.CTRL_MASK);
        about.addActionListener((event) -> this.aboutButtonClicked(event));
        appMenu.add(about);
        JMenuItem exit = createJMenuItem("Exit", "Exit Program", "plus.png", KeyEvent.VK_X, ActionEvent.CTRL_MASK);
        exit.addActionListener((event) -> this.exitButtonClicked(event));
        exit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        appMenu.add(exit);
        JMenuItem check = createJMenuItem("Check Prices", "Check Item Prices", "checkmark.png", KeyEvent.VK_C, ActionEvent.ALT_MASK);
        check.addActionListener((event) -> this.refreshButtonClicked(event));
        editMenu.add(check);
        JMenuItem add = createJMenuItem("Add", "Add a Product", "plus.png", KeyEvent.VK_A, ActionEvent.ALT_MASK);
        add.addActionListener((event) -> this.addButtonClicked(event));
        editMenu.add(add);
        editMenu.add(new JSeparator());
        JMenuItem search = createJMenuItem("Search", "Search for an Item", "search.png", KeyEvent.VK_S, ActionEvent.ALT_MASK);
        search.addActionListener((event) -> this.searchButtonClicked(event));
        search.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "search.png")));
        editMenu.add(search);
        JMenuItem forward = createJMenuItem("Select First", "Move to First Item", "up.png", KeyEvent.VK_UP, ActionEvent.ALT_MASK);
        forward.addActionListener((event) -> this.moveUpButtonClicked(event));
        editMenu.add(forward);
        JMenuItem backward = createJMenuItem("Select Last", "Move to Last Item", "down.png", KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
        backward.addActionListener((event) -> this.moveDownButtonClicked(event));
        backward.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "down.png")));
        editMenu.add(backward);
        JMenuItem oldest = new JRadioButtonMenuItem("Oldest");
        oldest.addActionListener((event) -> this.sortOld(event));
        sortMenu.add(oldest);
        JMenuItem newest = new JRadioButtonMenuItem("Newest");
        newest.addActionListener((event) -> this.sortNew(event));
        sortMenu.add(newest);
        sortMenu.addSeparator();
        JMenuItem ascend = new JRadioButtonMenuItem("Ascending Order");
        ascend.addActionListener((event) -> this.sortNameAscending(event));
        sortMenu.add(ascend);
        JMenuItem descend = new JRadioButtonMenuItem("Descending Order");
        descend.addActionListener((event) -> this.sortNameDescending(event));
        sortMenu.add(descend);
        sortMenu.addSeparator();
        JMenuItem low = new JRadioButtonMenuItem("Lowest Price ($)");
        low.addActionListener((event) -> this.sortLow(event));
        sortMenu.add(low);
        JMenuItem high = new JRadioButtonMenuItem("Highest Price ($)");
        high.addActionListener((event) -> this.sortHigh(event));
        sortMenu.add(high);
        JMenuItem priceChange = new JRadioButtonMenuItem("Price Change (%)");
        priceChange.addActionListener((event) -> this.sortChange(event));
        JMenu generatedNestedMenu = new JMenu("Selected");
        JMenuItem priceNested = createJMenutItem("Price", "checkmark.png", "Check Prices");
        priceNested.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        JMenuItem viewNested = createJMenutItem("View", "webbrowser.png", "View Item on Browser");
        viewNested.addActionListener((event) -> this.openWeb(event));
        JMenuItem editNested = createJMenutItem("Edit", "edit.png", "Edit selected Item");
        editNested.addActionListener((event) -> this.editButtonClicked(event));
        JMenuItem removeNested = createJMenutItem("Remove", "delete.png", "Delete selected Item");
        removeNested.addActionListener((event) -> this.deleteButtonClicked(event));
        JMenuItem cname = new JMenuItem("Copy Name");
        cname.addActionListener((event) -> this.copyToClipboard(1));
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.copyToClipboard(2));
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.copyToClipboard(3));
        generatedNestedMenu.add(priceNested);
        generatedNestedMenu.add(viewNested);
        generatedNestedMenu.add(editNested);
        generatedNestedMenu.add(removeNested);
        generatedNestedMenu.addSeparator();
        generatedNestedMenu.add(cname);
        generatedNestedMenu.add(curl);
        generatedNestedMenu.add(citem);
        editMenu.add(generatedNestedMenu);
        sortMenu.add(priceChange);
        fileMenuBar.add(appMenu);
        fileMenuBar.add(editMenu);
        fileMenuBar.add(sortMenu);
        setJMenuBar(fileMenuBar);
    }

    /**
     * Copies information inside of the {@link Jlist} to the System Clipboard.
     *
     * @param copyTo name will given name of product, product url, and whole
     * product information.
     */
    private void copyToClipboard(int copyTo) {
        if (jListRenderer.getSelectedIndex() > -1) {
            StringSelection selection = new StringSelection("");
            Clipboard clipboard;
            switch (copyTo) {
                case 1:
                    selection = new StringSelection(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductName());
                    break;
                case 2:
                    selection = new StringSelection(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL());
                    break;
                case 3:
                    Product toClipboard = defaultListModel.get(jListRenderer.getSelectedIndex());
                    selection = new StringSelection(
                            "Name:  " + toClipboard.getProductName() + "\n"
                            + "URL:  " + toClipboard.getProductURL() + "\n"
                            + "Price:  " + toClipboard.getProductPrice() + "\n"
                            + "Change:  " + toClipboard.getChange() + "\n"
                            + "Date Added:  " + toClipboard.getAddedDate() + "\n"
                    );
                    break;
                default:
                    break;
            }
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     * Creates a {@link JMenuItem} with the given label, key presses and if CTRL
     * or ALT is being pressed.
     *
     * @param label name of the item
     * @param key {@link KeyEvent}
     * @param mask modifier
     * @return
     */
    private JMenuItem createJMenuItem(String label, String tooltip, String itemName, int key, int mask) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(key, mask));
        menuItem.setMnemonic(mask);
        menuItem.setRolloverEnabled(true);
        menuItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + itemName)));
        menuItem.setToolTipText(tooltip);
        return menuItem;
    }

    /**
     * Creates a {@link JMenuItem} with the given parameter of a label. Used to
     * create a JMenuBar. It sets RollOverEnable to true.
     *
     * @param label
     * @return the JMenuItem
     */
    private JMenuItem createJMenutItem(String label, String imageName, String tooltip) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setRolloverEnabled(true);
        menuItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + imageName)));
        menuItem.setToolTipText(tooltip);
        return menuItem;

    }

    /**
     * Create a button with the given label.
     *
     * @param label name of the resource (image)
     * @param enabled
     * @return a button
     */
    private JButton createJButton(String label, String tooltip, boolean enabled) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + label)));
        button.setFocusPainted(enabled);
        button.setRolloverEnabled(true);
        button.setToolTipText(tooltip);
        return button;
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

}
