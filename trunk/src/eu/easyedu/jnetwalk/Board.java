/*
 * Board.java
 *
 * Created on Sobota, 2007, marec 24, 14:54
 */

package eu.easyedu.jnetwalk;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingworker.SwingWorker;

/**
 *
 * @author  hlavki
 */
public class Board extends javax.swing.JPanel {

    private static final Logger log = Logger.getLogger(Board.class.getName());
    private static final Map<Integer, Integer> contrDirs = initContrDirs();
    private static final int MINIMUM_CELLS = 20;
    private final Random random = new Random();
    protected final PropertyChangeSupport propertyChangeSupport;
    private Cell[] board;
    private Cell rootCell;
    private int boardSize = 9;
    private boolean wrapped = false;
    private int clickCount = -1;
    private Settings settings = null;
    private List<BoardEventListener> gameOverListeners;

    public Board() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        initComponents();
        initBoard();
        setMinimumSize(new Dimension(boardSize * boardSize + 3, boardSize * boardSize + 3));
        setPreferredSize(getComponentSize());
        this.gameOverListeners = new ArrayList<BoardEventListener>();
    }

    /** Creates new form Board */
    public Board(PropertyChangeListener clickCountListener) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        addPropertyChangeListener("clickCount", clickCountListener);
        initComponents();
        initBoard();
        setMinimumSize(new Dimension(boardSize * boardSize + 3, boardSize * boardSize + 3));
        setSize(getMinimumSize());
        setPreferredSize(getComponentSize());
        this.gameOverListeners = new ArrayList<BoardEventListener>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(getBoardSize(), getBoardSize()));
    }// </editor-fold>//GEN-END:initComponents

    public int getBoardSize() {
        return boardSize;
    }

    public Cell getRootCell() {
        return rootCell;
    }

    protected void initBoard() {
        log.info("Initializing board...");
        board = new Cell[boardSize * boardSize];
        Cell cell;
        for (int i = 0; i < (boardSize * boardSize); i++) {
            cell = new Cell(this, i);
            board[i] = cell;
            add(cell);
        }
        resetClickCounter();
    }

    public void newGame() {
        log.info("Starting new game with skill " + getSettings().getSkill().toDefaultString());
        for (int i = 0; i < boardSize * boardSize; i++) {
            board[i].clear();
        }
        
        Skill skill = getSettings().getSkill();
        final int size = skill.getBoardSize();

        wrapped = (skill == Skill.EXPERT);
        
        final int start = (getBoardSize() - size) / 2;
        final int rootrow = random.nextInt(size);
        final int rootcol = random.nextInt(size);

        rootCell = board[(start + rootrow) * getBoardSize() + start + rootcol];
        rootCell.setConnected(true);
        rootCell.setRoot(true);

        while (true) {
            for (int row = start; row < start + size; row++) {
                for (int col = start; col < start + size; col++) {
                    board[row * getBoardSize() + col].setDirections(Cell.FREE);
                }
            }
            LinkedList<Cell> cells = new LinkedList<Cell>();
            cells.add(rootCell);
            if (random.nextInt(2) == 1) {
                addRandomDir(cells);
            }

            while (!cells.isEmpty()) {
                if (random.nextInt(2) == 1) {
                    addRandomDir(cells);
                    if (random.nextInt(2) == 1) {
                        addRandomDir(cells);
                    }
                    cells.removeFirst();
                } else {
                    cells.add(cells.getFirst());
                    cells.removeFirst();
                }
            }

            int cellCount = 0;
            for (int i = 0; i < getBoardSize() * getBoardSize(); i++) {
                int d = board[i].getDirections();
                if ((d != Cell.FREE) && (d != Cell.NONE)) {
                    cellCount++;
                }
            }
            if (cellCount >= MINIMUM_CELLS) {
                break;
            }
        }

        for (int i = 0; i < getBoardSize() * getBoardSize(); i++) {
            board[i].rotate((random.nextInt(4)) * 90);
        }
        updateConnections();
        resetClickCounter();
    }

    private final Cell uCell(Cell cell) {
        if (cell.getIndex() >= getBoardSize()) {
            return board[cell.getIndex() - getBoardSize()];
        } else if (wrapped) {
            return board[getBoardSize() * (getBoardSize() - 1) + cell.getIndex()];
        } else {
            return null;
        }
    }

    private final Cell dCell(Cell cell) {
        if (cell.getIndex() < getBoardSize() * (getBoardSize() - 1)) {
            return board[cell.getIndex() + getBoardSize()];
        } else if (wrapped) {
            return board[cell.getIndex() - getBoardSize() * (getBoardSize() - 1)];
        } else {
            return null;
        }
    }

    private final Cell lCell(Cell cell) {
        if (cell.getIndex() % getBoardSize() > 0) {
            return board[cell.getIndex() - 1];
        } else if (wrapped) {
            return board[cell.getIndex() - 1 + getBoardSize()];
        } else {
            return null;
        }
    }

    private final Cell rCell(Cell cell) {
        if (cell.getIndex() % getBoardSize() < getBoardSize() - 1) {
            return board[cell.getIndex() + 1];
        } else if (wrapped) {
            return board[cell.getIndex() + 1 - getBoardSize()];
        } else {
            return null;
        }
    }

    protected boolean updateConnections() {
        boolean[] newConnection = new boolean[getBoardSize() * getBoardSize()];
        for (int i = 0; i < getBoardSize() * getBoardSize(); i++) {
            newConnection[i] = false;
        }

        LinkedList<Cell> cellList = new LinkedList<Cell>();
        if (!rootCell.isRotated()) {
            newConnection[rootCell.getIndex()] = true;
            cellList.add(rootCell);
        }
        while (!cellList.isEmpty()) {
            Cell cell = cellList.getFirst();
            Cell uCell = uCell(cell);
            Cell rCell = rCell(cell);
            Cell dCell = dCell(cell);
            Cell lCell = lCell(cell);

            if (((cell.getDirections() & Cell.U) > 0) && (uCell != null) && ((uCell.getDirections() & Cell.D) > 0) &&
                    !newConnection[uCell.getIndex()] && !uCell.isRotated()) {
                newConnection[uCell.getIndex()] = true;
                cellList.add(uCell);
            }

            if (((cell.getDirections() & Cell.R) > 0) && (rCell != null) && ((rCell.getDirections() & Cell.L) > 0) &&
                    !newConnection[rCell.getIndex()] && !rCell.isRotated()) {
                newConnection[rCell.getIndex()] = true;
                cellList.add(rCell);
            }
            if (((cell.getDirections() & Cell.D) > 0) && (dCell != null) && ((dCell.getDirections() & Cell.U) > 0) &&
                    !newConnection[dCell.getIndex()] && !dCell.isRotated()) {
                newConnection[dCell.getIndex()] = true;
                cellList.add(dCell);
            }
            if (((cell.getDirections() & Cell.L) > 0) && (lCell != null) && ((lCell.getDirections() & Cell.R) > 0) &&
                    !newConnection[lCell.getIndex()] && !lCell.isRotated()) {
                newConnection[lCell.getIndex()] = true;
                cellList.add(lCell);
            }
            cellList.removeFirst();
        }

        boolean isNewConnection = false;
        for (int i = 0; i < getBoardSize() * getBoardSize(); i++) {
            if (!board[i].isConnected() && newConnection[i]) {
                isNewConnection = true;
            }
            board[i].setConnected(newConnection[i]);
        }
        return isNewConnection;
    }

    protected void addRandomDir(LinkedList<Cell> cellList) {
        Cell cell = cellList.getFirst();
        Cell uCell = uCell(cell);
        Cell rCell = rCell(cell);
        Cell dCell = dCell(cell);
        Cell lCell = lCell(cell);

        Map<Integer, Cell> freeCells = new HashMap<Integer, Cell>();

        if ((uCell != null) && uCell.getDirections() == Cell.FREE)
            freeCells.put(new Integer(Cell.U), uCell);
        if ((rCell != null) && rCell.getDirections() == Cell.FREE)
            freeCells.put(new Integer(Cell.R), rCell);
        if ((dCell != null) && dCell.getDirections() == Cell.FREE)
            freeCells.put(new Integer(Cell.D), dCell);
        if ((lCell != null) && lCell.getDirections() == Cell.FREE)
            freeCells.put(new Integer(Cell.L), lCell);
        if (freeCells.isEmpty()) {
            return;
        }
        Iterator<Entry<Integer, Cell>> it = freeCells.entrySet().iterator();
        Entry<Integer, Cell> entry = it.next();
        for (int i = random.nextInt(freeCells.size()); i > 0; --i) {
            entry = it.next();
        }

        cell.setDirections(cell.getDirections() | entry.getKey());
        entry.getValue().setDirections(contrDirs.get(entry.getKey()));
        cellList.add(entry.getValue());
    }

    private static final Map<Integer, Integer> initContrDirs() {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        result.put(Cell.U, Cell.D);
        result.put(Cell.R, Cell.L);
        result.put(Cell.D, Cell.U);
        result.put(Cell.L, Cell.R);
        return result;
    }

    protected void rotate(final int index, final RotateDirection direction) {
        final Cell cell = board[index];
        final int d = cell.getDirections();
        if ((d == Cell.FREE) || (d == Cell.NONE) || isGameOver() || cell.isLocked()) {
            blink(index);
        } else {



            cell.rotate(direction.equals(RotateDirection.LEFT) ? -6 : 6);
            updateConnections();

            SwingWorker<Void, Object> anim = new SwingWorker<Void, Object>() {

                protected Void doInBackground() throws Exception {
                    log.fine("Starting animation...");
                    for (int i = 0; i < 14; i++) {
                        int inc = direction.equals(RotateDirection.LEFT) ? -6 : 6;
                        cell.rotate(inc);
                        try {
                            if (i < 13)
                                Thread.sleep(5);
                        } catch (InterruptedException e) {
                        }
                    }
                    log.fine("Animation stopped");
                    return null;
                }

                @Override
                public void done() {
                    if (updateConnections()) {
                    }
                    if (isGameOver()) {
                        blink(index);
                        endGame();
                    }
                }
            };
            anim.execute();

//            cell.rotate(direction.equals(RotateDirection.RIGHT) ? -90 : 90);
//            addClick();
        }
    }

    protected void endGame() {
        log.info(" Game with skill " + getSettings().getSkill().toDefaultString() + " was successfully ended in " +
                getClickCount() + " clicks");
        getSettings().addNewScore(new Score(getClickCount(), new Date(), System.getProperty("user.name")));
        fireGameOverEvent();
//        int n = JOptionPane.showConfirmDialog(this, "Cool. Do you wish to start new game?",
//                "Game Over", JOptionPane.YES_NO_OPTION);
//        if (n == JOptionPane.YES_OPTION) {
//            newGame();
//        }
    }

    protected void blink(int index) {
        for (int i = 0; i < board[index].getWidth() * 2; i += 2) {
            board[index].setLight(i);
        }
        board[index].setLight(0);
    }

    protected boolean isGameOver() {
        for (int i = 0; i < getBoardSize() * getBoardSize(); i++) {
            final int d = board[i].getDirections();
            if ((d != Cell.FREE) && (d != Cell.NONE) && !board[i].isConnected()) {
                return false;
            }
        }
        return true;
    }

    public void setSkill(Skill skill) {
        getSettings().setSkill(skill);
    }

    protected void resetClickCounter() {
        setClickCount(0);
    }

    protected int getClickCount() {
        return clickCount;
    }

    public Settings getSettings() {
        if (settings == null) {
            settings = Settings.load();
        }
        return settings;
    }

    protected void addClick() {
        setClickCount(getClickCount() + 1);
    }

    public void setClickCount(int clickCount) {
        int oldClickCount = this.clickCount;
        this.clickCount = clickCount;
        propertyChangeSupport.firePropertyChange("clickCount", new Integer(oldClickCount), new Integer(clickCount));
    }

    /**Add a property change listener for a specific property.
     * @param propertyName The name of the property to listen on.
     * @param listener The <code>PropertyChangeListener</code>
     * to be added.
     */
    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**Remove a property change listener for a specific property.
     * @param propertyName The name of the property that was listened on.
     * @param listener The <code>PropertyChangeListener</code>
     * to be removed
     */
    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    protected Dimension getComponentSize() {
        return new Dimension(getBoardSize() * 32, getBoardSize() * 32);
    }

    private void fireGameOverEvent() {
        for (BoardEventListener robotEventListener : gameOverListeners) {
            robotEventListener.gameOverEvent(new BoardEvent());
        }
    }

    /**
     * Add listener that listen on robot moves.
     * @param robotEventListener
     */
    public void addGameOverListener(BoardEventListener gameOverListener) {
        gameOverListeners.add(gameOverListener);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}