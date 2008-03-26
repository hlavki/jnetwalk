/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.easyedu.jnetwalk.utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author hlavki
 */
public class ColorTextCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private Integer highlightRow;

    public ColorTextCellRenderer() {
	this(null);
    }

    public ColorTextCellRenderer(Integer highlightRow) {
	this.highlightRow = highlightRow;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	    boolean hasFocus, int row, int column) {
	Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	if (this.highlightRow != null && highlightRow.intValue() == row) {
	    c.setForeground(Color.RED);
	} else {
	    c.setForeground(Color.BLACK);
	}
	return c;
    }

    public Integer getHighlightRow() {
	return highlightRow;
    }

    public void setHighlightRow(Integer highlightRow) {
	this.highlightRow = highlightRow;
    }
}
