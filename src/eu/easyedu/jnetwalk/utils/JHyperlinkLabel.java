/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.easyedu.jnetwalk.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 *
 * @author hlavki
 */
public class JHyperlinkLabel extends JLabel {

    private String url;
    private static MouseListener linker = new MouseAdapter() {

	@Override
	public void mouseClicked(MouseEvent e) {
	    JHyperlinkLabel self = (JHyperlinkLabel) e.getSource();
	    if (self.url == null) {
		return;
	    }
//	    try {
	    System.out.println(self.url.toString());
//	    } catch (IOException e1) {
//		e1.printStackTrace();
//	    }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	    e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
    };

    public JHyperlinkLabel() {
	this(null);
    }

    public JHyperlinkLabel(String label) {
	super(label);
	setForeground(Color.BLUE);
	addMouseListener(linker);
    }

    public JHyperlinkLabel(String label, String url) {
	this(label);
	this.url = url;
    }

    public void setURL(String url) {
	this.url = url;
    }

    public String getURL() {
	return url;
    }
}
