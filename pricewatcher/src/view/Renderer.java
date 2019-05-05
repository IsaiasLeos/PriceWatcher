package view;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import model.Product;

/**
 * Visual renderer when selecting an item.
 *
 * @author Isaias Leos
 */
public class Renderer extends ItemView implements ListCellRenderer<Product> {

    private static final long serialVersionUID = 1L;

    /**
     * This will render visuals when you selected an item inside of the JList.
     *
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list,
            Product value, int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
