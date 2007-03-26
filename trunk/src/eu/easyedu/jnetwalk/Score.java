/*
 * Score.java
 *
 * Created on Nedeľa, 2007, marec 25, 21:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hlavki
 */
public class Score implements Comparable<Score>, Serializable {
    
    private Integer score;
    private Date date;
    private String name;
    
    /** Creates a new instance of Score */
    public Score(Integer score, Date date, String name) {
        this.score = score;
        this.date = date;
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int compareTo(Score otherScore) {
        return getScore().compareTo(otherScore.getScore());
    }
    
}
