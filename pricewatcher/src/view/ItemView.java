package view;

import model.Product;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * A special panel to display the detail of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class ItemView extends JPanel implements ListCellRenderer<Product> {

    private Image itemImage;
    private Product product;

    /**
     * View-page clicking listener.
     */
    private ClickListener listener;

    /**
     * Directory for image files: src/image.
     */
    private final static String RESOURCE_DIR = "/resources/";

    /**
     * Create a new instance.
     *
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ItemView() {
        setItemImage(getImage("webbrowser.png"));
        Dimension dim = getSize();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(dim.width, 160));//Learn what this does.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

    public interface ClickListener {

        /**
         * Callback to be invoked when the view page icon is clicked.
         */
        void clicked();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list, Product value, int index, boolean isSelected, boolean cellHasFocus) {
        setProduct(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    private void openWeb() {
        System.out.println("Test");
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(getProduct().getProductURL()));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(Main.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Set the view-page click listener.
     *
     * @param listener
     */
    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /**
     * Display the details of an item list within the ItemView Panel. Overridden
     * here to display the details of the item.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        float f = (float) product.getChange();
        Color change = f == 0.0 ? Color.BLACK : f > 0.0 ? Color.GREEN : Color.RED;
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int x = 20, y = 10;
        g.drawImage(itemImage, x, y, this);
        y += 24 + 20;
        g.drawString(textAttrManipulation("Name:      ", product.getProductName(), Font.BOLD, Color.BLACK), x, y);
        y += 20;
        g.drawString("URL:         " + product.getProductURL(), x, y);
        y += 20;
        g.drawString(textAttrManipulation("Price:       ", product.getProductPrice() + "$", Font.PLAIN, Color.BLUE), x, y);//Green or Red
        y += 20;
        if (product.getSound() == 1) {
            if (f > 0.0) {
                priceDropSound("play.wav");
            }
            product.setSound(0);
        }
        g.drawString(textAttrManipulation("Change:  ", Math.abs(product.getChange()) + "%", Font.PLAIN, change), x, y);//Green or Red
        y += 20;
        g.drawString("Added:     " + product.getAddedDate() + " (" + product.getInitialPrice() + "$)", x, y);
        y += 80;
        g.dispose();
    }

    /**
     * Manipulates the given two strings. Changes the first string to make the
     * same font style, but ignores color and fond type. Changes the color and
     * font type of the second string given the font style and color.
     *
     * @param productPrefix first part of the string
     * @param productPostfix second part of the string
     * @param font the font type e.g. bold or plain
     * @param color the color of the font
     * @return the re-formated string
     */
    private AttributedCharacterIterator textAttrManipulation(String productPrefix, String productPostfix, int font, Color color) {
        AttributedString text = new AttributedString(productPrefix + productPostfix);
        text.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN, 12),
                0, productPrefix.length() + productPostfix.length());
        text.addAttribute(TextAttribute.FONT, new Font("Arial", font, 12),
                productPrefix.length(),
                productPrefix.length() + productPostfix.length());
        text.addAttribute(TextAttribute.FOREGROUND, color,
                productPrefix.length(),
                productPrefix.length() + productPostfix.length());
        return text.getIterator();
    }

    /**
     *
     * @param x x-coordinate of the mouse pointer
     * @param y y-coordinate of the mouse pointer
     * @return true if the given screen coordinate is inside the viewPage icon.
     */
    private boolean isImageClicked(int x, int y) {
        return new Rectangle(20, 10, 24, 24).contains(x, y);
    }

    /**
     *
     * @param x x-coordinate of the mouse pointer
     * @param y y-coordinate of the mouse pointer
     * @return true if the given screen coordinate is inside the viewPage icon.
     */
    public boolean imageClicked(int x, int y) {
        return isImageClicked(x, y);
    }

    /**
     * Return the image stored in the given file.
     *
     * @param file
     * @return
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Image getImage(String file) {
        try {
            URL url = new URL(getClass().getResource(RESOURCE_DIR), file);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Play the audio clip (wav) specified by a URL. This method has no effect
     * if the audio clip cannot be found.
     *
     * @param file
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private void priceDropSound(String filename) {
        try {
            URL url = new URL(getClass().getResource(RESOURCE_DIR), filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Product getProduct() {
        return product;
    }

    /**
     *
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     *
     * @return
     */
    public Image getItemImage() {
        return itemImage;
    }

    /**
     *
     * @param itemIcon
     */
    public void setItemImage(Image itemIcon) {
        this.itemImage = itemIcon;
    }
}
