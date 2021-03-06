/*
 * blue - object composition environment for csound Copyright (c) 2000-2009
 * Steven Yi (stevenyi@gmail.com)
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.LIB. If not, write to the Free
 * Software Foundation Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307
 * USA
 */
package blue.settings;

import blue.ui.utilities.SimpleDocumentListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final class GeneralPanel extends javax.swing.JPanel {

    private final GeneralOptionsPanelController controller;

    private boolean loading = false;

    GeneralPanel(GeneralOptionsPanelController controller) {
        this.controller = controller;
        initComponents();

        DocumentListener changeListener = new SimpleDocumentListener() {

            @Override
            public void documentChanged(DocumentEvent e) {
                if (!loading) {
                    GeneralPanel.this.controller.changed();
                }
            }
        };

        defaultDirectoryField.getDocument().addDocumentListener(changeListener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        csoundErrorWarningEnabled = new javax.swing.JCheckBox();
        messageColorsEnabled = new javax.swing.JCheckBox();
        alphaEnabled = new javax.swing.JCheckBox();
        newUserDefaultsEnabled = new javax.swing.JCheckBox();
        defaultDirectoryField = new javax.swing.JTextField();
        directoryOpenButton = new javax.swing.JButton();

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabel2.text_1")); // NOI18N
        jLabel2.setAlignmentX(1.0F);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabel4.text_1")); // NOI18N
        jLabel4.setAlignmentX(1.0F);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabel6.text_1")); // NOI18N
        jLabel6.setAlignmentX(1.0F);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabel8.text_1")); // NOI18N
        jLabel8.setAlignmentX(1.0F);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabel9.text_1")); // NOI18N
        jLabel9.setAlignmentX(1.0F);

        csoundErrorWarningEnabled.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        csoundErrorWarningEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        csoundErrorWarningEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csoundErrorWarningEnabledActionPerformed(evt);
            }
        });

        messageColorsEnabled.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        messageColorsEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        messageColorsEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageColorsEnabledActionPerformed(evt);
            }
        });

        alphaEnabled.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        alphaEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        alphaEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaEnabledActionPerformed(evt);
            }
        });

        newUserDefaultsEnabled.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        newUserDefaultsEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        newUserDefaultsEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserDefaultsEnabledActionPerformed(evt);
            }
        });

        defaultDirectoryField.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.defaultDirectoryField.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(directoryOpenButton, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.directoryOpenButton.text_1")); // NOI18N
        directoryOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryOpenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defaultDirectoryField, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(alphaEnabled, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(messageColorsEnabled, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(csoundErrorWarningEnabled, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(newUserDefaultsEnabled, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(directoryOpenButton)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel4, jLabel6, jLabel8, jLabel9});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(defaultDirectoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(directoryOpenButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(newUserDefaultsEnabled))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(alphaEnabled)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(messageColorsEnabled)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csoundErrorWarningEnabled)
                    .addComponent(jLabel9))
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void csoundErrorWarningEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csoundErrorWarningEnabledActionPerformed
        controller.changed();
    }//GEN-LAST:event_csoundErrorWarningEnabledActionPerformed

    private void messageColorsEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageColorsEnabledActionPerformed
        controller.changed();
    }//GEN-LAST:event_messageColorsEnabledActionPerformed

    private void alphaEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaEnabledActionPerformed
        controller.changed();
    }//GEN-LAST:event_alphaEnabledActionPerformed

    private void newUserDefaultsEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserDefaultsEnabledActionPerformed
        controller.changed();
    }//GEN-LAST:event_newUserDefaultsEnabledActionPerformed

    private void directoryOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directoryOpenButtonActionPerformed
        JFileChooser jfc = SettingsFileChooser.getFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setCurrentDirectory(GeneralSettings.getInstance().getDefaultDirectory());

        int rValue = jfc.showOpenDialog(this);
        if (rValue == JFileChooser.APPROVE_OPTION) {
            defaultDirectoryField.setText(jfc.getSelectedFile()
                    .getAbsolutePath());

            GeneralSettings.getInstance().setDefaultDirectory(jfc.getSelectedFile());
            controller.changed();
        }
    }//GEN-LAST:event_directoryOpenButtonActionPerformed


    void load() {

        loading = true;

        GeneralSettings settings = GeneralSettings.getInstance();

        defaultDirectoryField.setText(settings.getDefaultDirectory().getAbsolutePath());
        newUserDefaultsEnabled.setSelected(settings.isNewUserDefaultsEnabled());
        alphaEnabled.setSelected(settings.isAlphaEnabled());
        messageColorsEnabled.setSelected(settings.isMessageColorsEnabled());
        csoundErrorWarningEnabled.setSelected(settings.isCsoundErrorWarningEnabled());

        loading = false;
    }

    void store() {

        GeneralSettings settings = GeneralSettings.getInstance();

        settings.setDefaultDirectory(new File(defaultDirectoryField.getText()));
        settings.setNewUserDefaultsEnabled(newUserDefaultsEnabled.isSelected());
        settings.setAlphaEnabled(alphaEnabled.isSelected());
        settings.setMessageColorsEnabled(messageColorsEnabled.isSelected());
        settings.setCsoundErrorWarningEnabled(csoundErrorWarningEnabled.isSelected());

        settings.save();
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox alphaEnabled;
    private javax.swing.JCheckBox csoundErrorWarningEnabled;
    private javax.swing.JTextField defaultDirectoryField;
    private javax.swing.JButton directoryOpenButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JCheckBox messageColorsEnabled;
    private javax.swing.JCheckBox newUserDefaultsEnabled;
    // End of variables declaration//GEN-END:variables
}
