package view;

import model.Product;
import controller.PriceFinder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    private Product product;
    private PriceFinder webContent;

    /**
     * Directory for image files: src/image.
     */
    private final static String RESOURCE_DIR = "/resources/";

    /**
     * Default dimension of the dialog.
     */
    private final static Dimension DEFAULT_SIZE = new Dimension(800, 600);

    /**
     * Special panel to display the watched item.
     */
    private ItemView itemView;

    /**
     * Message bar to display various messages.
     */
    private final JLabel msgBar = new JLabel(" ");

    /**
     * Create a new dialog.
     */
    public Main() {
        this(DEFAULT_SIZE);
    }

    /**
     * Create a new dialog of the given screen dimension.
     *
     * @param dim
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Main(Dimension dim) {
        super("Price Watcher");
        String itemURL = "https://www.amazon.com/Nintendo-Console-Resolution-Surround-Customize/dp/B07M5ZQSKV";
        String itemName = "Nintendo Switch";
        double itemPrice = 359.99;
        String itemDateAdded = "1/30/2019";
        this.product = new Product(itemName, itemURL, itemPrice, itemDateAdded);
        this.webContent = new PriceFinder();
        setDefaultLookAndFeelDecorated(true);
        setLayout(new FlowLayout());
        setSize(dim);
        configureUI();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        showMessage("Welcome!");
    }

    /**
     * Callback to be invoked when the refresh button is clicked. Find the
     * current price of the watched item and display it along with a percentage
     * price change.
     *
     * @param event
     */
    private void refreshButtonClicked(ActionEvent event) {
//        System.out.println(event.toString());
//        productList.forEach((iter) -> {
//            iter.checkPrice(webContent.getSimulatedPrice());
//        });
//        super.repaint();
//        showMessage(product.getProductPrice() + "$");
    }

    private void singleRefreshButtonClicked(ActionEvent event) {
//        System.out.println(event.toString());
//        productList.forEach((iter) -> {
//            iter.checkPrice(webContent.getSimulatedPrice());
//        });
//        super.repaint();
//        showMessage(product.getProductPrice() + "$");
    }

    private void addButtonClicked(ActionEvent event) {

    }

    private void searchButtonClicked(ActionEvent event) {

    }

    private void forwardButtonClicked(ActionEvent event) {

    }

    private void backwardButtonClicked(ActionEvent event) {

    }

    private void delete(ActionEvent event) {

    }

    private void edit(ActionEvent event) {

    }

    private void openWeb(ActionEvent event) {

    }

    /**
     * Callback to be invoked when the view-page icon is clicked. Launch a
     * (default) web browser by supplying the URL of the item.
     */
    private void viewPageClicked() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(product.getProductURL()));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        showMessage("Opening Webpage");
    }

    /**
     * Configure UI.
     */
    private void configureUI() {
        setLayout(new BorderLayout());
        JPanel control = makeControlPanel();
        control.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        add(control, BorderLayout.NORTH);
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 16, 0, 16),
                BorderFactory.createLineBorder(Color.GRAY)));
        board.setLayout(new GridLayout(1, 1));
        itemView = new ItemView(product);
        itemView.setClickListener(this::viewPageClicked);
        board.add(itemView);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /**
     * Create a control panel consisting of a refresh button.
     */
    private JPanel makeControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JToolBar toolBar = new JToolBar("Toolbar");
        panel.add(toolBar, BorderLayout.PAGE_END);
        JButton checkmark = createButton("checkmark.png", false);
        checkmark.addActionListener(this::refreshButtonClicked);
        toolBar.add(checkmark);
        JButton add = createButton("plus.png", false);
        add.addActionListener(this::addButtonClicked);
        toolBar.add(add);
        JButton search = createButton("search.png", false);
        search.addActionListener(this::searchButtonClicked);
        toolBar.add(search);
        JButton forward = createButton("forward.png", false);
        forward.addActionListener(this::forwardButtonClicked);
        toolBar.add(forward);
        JButton backward = createButton("back.png", false);
        backward.addActionListener(this::backwardButtonClicked);
        toolBar.add(backward);
        toolBar.addSeparator();
        JButton singleRefresh = createButton("refresh.png", false);
        singleRefresh.addActionListener(this::singleRefreshButtonClicked);
        toolBar.add(singleRefresh);
        JButton openLink = createButton("link.png", false);
        openLink.addActionListener(this::openWeb);
        toolBar.add(openLink);
        JButton delete = createButton("delete.png", false);
        delete.addActionListener(this::delete);
        toolBar.add(delete);
        JButton edit = createButton("edit.png", false);
        edit.addActionListener(this::edit);
        toolBar.add(edit);
        return panel;
    }

    /**
     * Create a button with the given label.
     *
     * @param label name of the resource (image)
     * @param enabled
     * @return a button
     */
    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton(new ImageIcon(getClass().getClassLoader()
                .getResource("resources/" + label)));
        button.setFocusPainted(enabled);
        return button;
    }

    /**
     * Show briefly the given string in the message bar.
     *
     * @param msg
     */
    private void showMessage(String msg) {
        msgBar.setText(msg);
        new Thread(() -> {
            try {
                Thread.sleep(3 * 1000); // 3 seconds
            } catch (InterruptedException e) {
            }
            if (msg.equals(msgBar.getText())) {
                SwingUtilities.invokeLater(() -> msgBar.setText(" "));
            }
        }).start();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
    }
}
