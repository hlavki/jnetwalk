/*
 * Cell.java
 *
 * Created on Nede\u013Ea, 2007, marec 18, 17:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk;

import eu.easyedu.jnetwalk.utils.ImageHelper;
import eu.easyedu.jnetwalk.utils.ImageRotator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author hlavki
 */
public class Cell extends JComponent implements MouseListener {
    
    private final static Map<Integer, String> directionNames  = initDirNames();
    
    BufferedImage image = null;
    
    public final static int U = 1;
    public final static int D = 4;
    public final static int R = 2;
    public final static int L = 8;
    public final static int FREE = 0;
    public final static int NONE = 16;
    
    private int angle;
    private int light;
    private int index;
    private boolean connected;
    private boolean changed;
    private boolean root;
    private boolean locked;
    private int directions;
    private Board board;
    
    /** Creates a new instance of Cell */
    public Cell(Board board, final int index) {
        this(index);
        this.board = board;
    }
    
    /** Creates a new instance of Cell */
    public Cell(final int index) {
        this.angle = 0;
        this.light = 0;
        this.index = index;
        this.directions = FREE;
        this.changed = true;
        this.connected = false;
        this.root = false;
        this.locked = false;
        image = new BufferedImage(32, 32, BufferedImage.TYPE_3BYTE_BGR);
        this.addMouseListener(this);
    }
    
    public void rotate(int angle) {
        this.angle += angle;
        changed = true;
        while(this.angle >= 45) {
            this.angle -= 90;
            int newdirs = FREE;
            if ((directions & U) > 0) newdirs |= R;
            if((directions & R) > 0) newdirs |= D;
            if((directions & D) > 0) newdirs |= L;
            if((directions & L) > 0) newdirs |= U;
            setDirections(newdirs);
        }
        while(this.angle < -45) {
            this.angle += 90;
            int newdirs = FREE;
            if((directions & U) >0) newdirs |= L;
            if((directions & R) >0) newdirs |= U;
            if((directions & D) >0) newdirs |= R;
            if((directions & L) >0) newdirs |= D;
            setDirections(newdirs);
        }
        repaint();
    }
    
    public void setDirections(int directions) {
        this.directions = directions;
        changed = true;
        repaint();
    }
    
    public void setRoot(boolean root) {
        this.root = root;
        changed = true;
        repaint();
    }
    
    public void setLight(int light) {
        this.light = light;
        changed = true;
        repaint();
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
        changed = true;
        repaint();
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
        changed = true;
        repaint();
    }
    
    public int getDirections() {
        return directions;
    }
    
    public int getIndex() {
        return index;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public boolean isRotated() {
        return angle > 0;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    private final static Map<Integer, String> initDirNames() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        result.put(L,     "0001");
        result.put(D,     "0010");
        result.put(D|L,   "0011");
        result.put(R,     "0100");
        result.put(R|L,   "0101");
        result.put(R|D,   "0110");
        result.put(R|D|L, "0111");
        result.put(U,     "1000");
        result.put(U|L,   "1001");
        result.put(U|D,   "1010");
        result.put(U|D|L, "1011");
        result.put(U|R,   "1100");
        result.put(U|R|L, "1101");
        result.put(U|R|D, "1110");
        return result;
    }
    
    private void drawImage(Graphics2D g2, String name) {
        ImageIcon icon = ImageHelper.getInstance().getIcon(name);
        g2.drawImage(icon.getImage(), g2.getTransform(), icon.getImageObserver());
    }
    
    protected void paintEvent() {
        if(changed) {
            changed = false;
            Graphics2D g2 = (Graphics2D)image.getGraphics();
            if (locked) {
                drawImage(g2, "/eu/easyedu/jnetwalk/images/background_locked.png");
            } else {
                drawImage(g2, "/eu/easyedu/jnetwalk/images/background.png");
            }
            
            if (light > 0) {
                Line2D line = new Line2D.Double(0, getWidth() - light, getWidth(), 2 * getWidth() - light);
                g2.setColor(Color.white);
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, 1));
                g2.draw(line);
            }
            
            if ((directions != FREE) && (directions != NONE)) {
                double offset = 0;
                if (angle > 0) {
                    image = ImageRotator.rotateDegrees(image, image.getWidth(), image.getHeight(),
                            angle, ImageRotator.EXACT_BOUNDING_BOX, null);
                    //                    offset = getWidth() / 2;
                    //                    g2.translate(offset, offset);
                    //                    System.out.println("Rotating angle: " + angle);
                    //                    g2.rotate(angle);
                }
                
                if (connected) {
                    drawImage(g2, "/eu/easyedu/jnetwalk/images/cable" + directionNames.get(directions) + ".png");
                    //                    paint.drawPixmap(int(-offset), int(-offset), *connectedpixmap[ddirs]);
                } else {
                    drawImage(g2, "/eu/easyedu/jnetwalk/images/cable" + directionNames.get(directions) + ".png");
                    //                    paint.drawPixmap(int(-offset), int(-offset), *disconnectedpixmap[ddirs]);
                }
                
                if (root) {
                    drawImage(g2, "/eu/easyedu/jnetwalk/images/server.png");
                } else if(directions == U || directions == L || directions == D || directions == R) {
                    if(connected) {
                        drawImage(g2, "/eu/easyedu/jnetwalk/images/computer2.png");
                    } else {
                        drawImage(g2, "/eu/easyedu/jnetwalk/images/computer1.png");
                    }
                }
            }
        }
        
    }
    
    public void clear() {
        setDirections(Cell.NONE);
        setConnected(false);
        setRoot(false);
        setLocked(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintEvent();
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image, null, 0, 0);
        g2.dispose();
    }
    
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            board.rotate(index, RotateDirection.RIGHT);
            break;
        case MouseEvent.BUTTON3:
            board.rotate(index, RotateDirection.LEFT);
            break;
        }
    }
    
    public void mouseClicked(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    
}