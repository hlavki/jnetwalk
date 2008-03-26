/*
 * Settings.java
 *
 * Created on Sobota, 2007, marec 24, 17:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hlavki
 */
public class Settings implements Serializable {
    
    private final static Logger log = Logger.getLogger(Settings.class.getName());
    private final static String FILE_NAME = System.getProperty("user.home") + "/.jnetwalk.dat";
    
    static final long serialVersionUID = -1506210641967751745L;
    
    private Skill skill;
    private Map<Skill, SortedSet<Score>> scores;
    private transient final static int MAX_SCORE_SIZE = 10;
    private boolean playSound;
    
    /** Creates a new instance of Settings */
    public Settings() {
        skill = Skill.NOVICE;
        scores = new EnumMap<Skill, SortedSet<Score>>(Skill.class);
        playSound = true;
    }
    
    public final Skill getSkill() {
        return skill;
    }
    
    public final void setSkill(Skill skill) {
        this.skill = skill;
    }
    
    public final void addNewScore(Score newScore) {
	log.info("Adding new score with hash " + newScore.hashCode());
        SortedSet<Score> skillScores = getSkillScores();
        skillScores.add(newScore);
        while (skillScores.size() > MAX_SCORE_SIZE) {
            skillScores.remove(skillScores.last());
        }
    }
    
    public final SortedSet<Score> getSkillScores(Skill skill) {
        SortedSet<Score> skillScores = scores.get(skill);
        if (skillScores == null) {
            skillScores = new TreeSet<Score>();
            scores.put(skill, skillScores);
        }
        return skillScores;
    }
    
    public final SortedSet<Score> getSkillScores() {
        return getSkillScores(getSkill());
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
    
    @Override
    public final String toString() {
        return getSkillScores().toString();
    }
    
    public final static Settings load() {
        log.info("Loading settings...");
        Settings settings;
        try {
            FileInputStream fin = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fin);
            settings = (Settings) ois.readObject();
            ois.close();
            log.info("Settings from " + FILE_NAME + " loaded successfully...");
        } catch (Exception e) {
            log.log(Level.WARNING, "Settings file (" + FILE_NAME + 
                    ") not found or corrupted. Creating new...", e);
            settings = new Settings();
        }
        return settings;
    }
    
    public final void save() {
        log.info("Saving settings to " + FILE_NAME);
        try {
            FileOutputStream fout = new FileOutputStream(FILE_NAME);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public int getScoreIndex(Score newScore) {
	int idx = 0, scoreIndex = -1;
	SortedSet<Score> skillScores = getSkillScores();
	Iterator<Score> it = skillScores.iterator();
	int size = skillScores.size();
	while (newScore != null && idx < size && scoreIndex == -1) {
	    Score score = it.next();
	    if (newScore.equals(score)) {
		scoreIndex = idx;
	    }
	    idx++;
	}
	return scoreIndex;
    }
}
