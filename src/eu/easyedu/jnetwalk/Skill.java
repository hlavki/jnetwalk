/*
 * Skill.java
 *
 * Created on Streda, 2007, marec 21, 0:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk;

/**
 *
 * @author hlavki
 */
public enum Skill {
    NOVICE(5),
    NORMAL(7),
    MASTER(9),
    EXPERT(9);
    
    private int boardSize;
    
    private Skill(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public String toString() {
        String name = super.toString().toLowerCase();
        return java.util.ResourceBundle.getBundle("eu/easyedu/jnetwalk/Bundle").getString(name + ".level.menu.item");
    }

    public String toDefaultString() {
        return super.toString();
    }
}
