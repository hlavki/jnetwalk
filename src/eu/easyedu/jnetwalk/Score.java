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

    static final long serialVersionUID = -6268548982168878799L;
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

    @Override
    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof Score)) {
	    return false;
	}
	final Score other = (Score) obj;
	if (this.score != other.score && (this.score == null || !this.score.equals(other.score))) {
	    return false;
	}
	if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
	    return false;
	}
	if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 23 * hash + (this.score != null ? this.score.hashCode() : 0);
	hash = 23 * hash + (this.date != null ? this.date.hashCode() : 0);
	hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
	return hash;
    }
}
