/*
 * BoardEvent.java
 *
 * Created on Pondelok, 2007, marec 26, 9:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package eu.easyedu.jnetwalk;

/**
 *
 * @author hlavki
 */
public class BoardEvent {

    private final Score score;

    /** Creates a new instance of BoardEvent */
    public BoardEvent(Score score) {
	this.score = score;
    }

    public Score getScore() {
	return score;
    }
}
