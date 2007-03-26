/*
 * StatisticsDialog.java
 *
 * Created on Nedeľa, 2007, marec 25, 19:43
 */

package eu.easyedu.jnetwalk;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author  hlavki
 */
public class StatisticsDialog extends javax.swing.JDialog {
    
    private final static Logger log = Logger.getLogger(StatisticsDialog.class.getName());
    private final static String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    private DefaultListModel levelModel;
    private DefaultTableModel statisticsTableModel;
    private ResourceBundle bundle;
    private Settings settings;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    
    /** Creates new form StatisticsDialog */
    public StatisticsDialog(Settings settings, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.settings = settings;
        bundle = ResourceBundle.getBundle("eu/easyedu/jnetwalk/Bundle");
        initComponents();
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(170);
        
        TableCellRenderer tcr = scoreTable.getDefaultRenderer(String.class);
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tcr;
        dtcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
        skillList.setSelectedValue(settings.getSkill(), true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        skillListScrollPane = new javax.swing.JScrollPane();
        skillList = new javax.swing.JList();
        scoreTableScrollPane = new javax.swing.JScrollPane();
        scoreTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        CloseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new java.awt.BorderLayout());

        skillList.setModel(getLevelModel());
        skillList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        skillList.setPreferredSize(new java.awt.Dimension(100, 85));
        skillList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                skillListValueChanged(evt);
            }
        });
        skillListScrollPane.setViewportView(skillList);

        mainPanel.add(skillListScrollPane, java.awt.BorderLayout.WEST);

        scoreTable.setModel(getStatisticsTableModel());
        scoreTable.setAutoCreateColumnsFromModel(false);
        scoreTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        scoreTableScrollPane.setViewportView(scoreTable);

        mainPanel.add(scoreTableScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setPreferredSize(new java.awt.Dimension(100, 50));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("eu/easyedu/jnetwalk/Bundle"); // NOI18N
        CloseButton.setText(bundle.getString("statistics.dialog.close.button")); // NOI18N
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(CloseButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-410)/2, (screenSize.height-330)/2, 410, 330);
    }// </editor-fold>//GEN-END:initComponents
    
private void skillListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_skillListValueChanged
    if (!evt.getValueIsAdjusting()) {
        showData();
    }
}//GEN-LAST:event_skillListValueChanged

private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
    dispose();
}//GEN-LAST:event_CloseButtonActionPerformed

protected ListModel getLevelModel() {
    if (levelModel == null) {
        levelModel = new DefaultListModel();
        levelModel.addElement(Skill.NOVICE);
        levelModel.addElement(Skill.NORMAL);
        levelModel.addElement(Skill.MASTER);
        levelModel.addElement(Skill.EXPERT);
    }
    return levelModel;
}

protected TableModel getStatisticsTableModel() {
    if (statisticsTableModel == null) {
        statisticsTableModel = new DefaultTableModel();
        statisticsTableModel.addColumn(bundle.getString("statistics.dialog.order.title"));
        statisticsTableModel.addColumn(bundle.getString("statistics.dialog.score.title"));
        statisticsTableModel.addColumn(bundle.getString("statistics.dialog.date.title"));
    }
    return statisticsTableModel;
}

protected void showData() {
    log.info("Showing data...");
    statisticsTableModel.setRowCount(0);
    Skill skill = (Skill)skillList.getSelectedValue();
    if (skill == null) {
        skill = settings.getSkill();
        skillList.setSelectedValue(skill, true);
        
    }
    Set<Score> scores = settings.getSkillScores(skill);
    int idx = 1;
    for (Score score : scores) {
        statisticsTableModel.addRow(new Object[] { new Integer(idx++),
        score.getScore(), dateFormat.format(score.getDate()) });
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTable scoreTable;
    private javax.swing.JScrollPane scoreTableScrollPane;
    private javax.swing.JList skillList;
    private javax.swing.JScrollPane skillListScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
