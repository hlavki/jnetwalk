/*
 * BoardEventListener.java
 *
 * Created on Pondelok, 2007, marec 26, 9:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk;

import java.util.EventListener;

/**
 *
 * @author hlavki
 */
public interface BoardEventListener extends EventListener {
    
    public void gameOverEvent(BoardEvent evt);
    
}
