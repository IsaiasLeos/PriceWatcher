package view;

import model.Product;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/**
 * A special panel to display the detail of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class ItemView extends JPanel {

    private Product product;
    private Image itemImage;
    private String theme = "Metal";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public final Dimension dim = new Dimension(0, 160);

    /**
     * Create a new instance.
     *
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ItemView() {
        setBackground(Color.WHITE);
        setPreferredSize(dim);
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
        g.drawImage(product.getProductIcon(), x, y, this);
        y += 24 + 20;
        g.drawString(textAttrManipulation("Name:      ", product.getProductName(), Font.BOLD, Color.BLACK), x, y);
        y += 20;
        g.drawString(textAttrManipulation("URL:         " + product.getProductURL(), " ", Font.PLAIN, Color.BLACK), x, y);
        y += 20;
        g.drawString(textAttrManipulation("Price:       ", product.getProductPrice() + "$", Font.PLAIN, Color.BLUE), x, y);//Green or Red
        y += 20;
        if (product.getSound() == 1) {
            if (f > 0.0) {
                priceDecreased("play.wav");
            }
            product.setSound(0);
        }
        g.drawString(textAttrManipulation("Change:  ", Math.abs(product.getChange()) + "%", Font.PLAIN, change), x, y);//Green or Red
        y += 20;
        g.drawString(textAttrManipulation("Added:     " + product.getAddedDate() + " (" + product.getInitialPrice() + "$)", " ", Font.PLAIN, Color.BLACK), x, y);
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
        try {
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
        } catch (Exception e) {
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
    private void priceDecreased(String filename) {
        try {
            URL url = new URL(getClass().getResource("/resources/"), filename);
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
