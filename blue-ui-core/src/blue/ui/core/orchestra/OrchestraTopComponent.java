/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blue.ui.core.orchestra;

import blue.event.SelectionEvent;
import blue.event.SelectionListener;
import blue.orchestra.Instrument;
import blue.projects.BlueProject;
import blue.projects.BlueProjectManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//blue.ui.core.orchestra//Orchestra//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "OrchestraTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false,
        position = 200)
@ActionID(category = "Window", id = "blue.ui.core.midi.OrchestraTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 1100),
    @ActionReference(path = "Shortcuts", name = "D-2")
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_OrchestraAction",
        preferredID = "OrchestraTopComponent"
)
@NbBundle.Messages({
    "CTL_OrchestraAction=Orchestra",
    "CTL_OrchestraTopComponent=Orchestra",
    "HINT_OrchestraTopComponent=This is an Orchestra window"
})
public final class OrchestraTopComponent extends TopComponent {

    private static OrchestraTopComponent instance;

    private final ArrangementEditPanel arrangementPanel = new ArrangementEditPanel();

    private final UserInstrumentLibrary userInstrumentLibrary = new UserInstrumentLibrary();

    boolean isChanging = false;

    private OrchestraTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(OrchestraTopComponent.class,
                "CTL_OrchestraTopComponent"));
        setToolTipText(NbBundle.getMessage(OrchestraTopComponent.class,
                "HINT_OrchestraTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

        jSplitPane2.setLeftComponent(arrangementPanel);
        jSplitPane2.setRightComponent(userInstrumentLibrary);

        BlueProjectManager.getInstance().addPropertyChangeListener(
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (BlueProjectManager.CURRENT_PROJECT.equals(
                                evt.getPropertyName())) {
                            reinitialize();
                        }
                    }
                });

        userInstrumentLibrary.addSelectionListener(new SelectionListener() {

            @Override
            public void selectionPerformed(SelectionEvent e) {
                Object obj = e.getSelectedItem();

                if (obj instanceof Instrument) {
                    isChanging = true;

                    instrumentEditPanel1.setEditingLibraryObject(true);
                    editInstrument((Instrument) obj);

                    arrangementPanel.deselect();
                    isChanging = false;
                } else {
                    isChanging = true;

                    editInstrument(null);
                    instrumentEditPanel1.setEditingLibraryObject(false);

                    arrangementPanel.deselect();
                    isChanging = false;
                }
            }

        });

        arrangementPanel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting() || isChanging) {
                    return;
                }

                Instrument instr = arrangementPanel.getSelectedInstrument();

                editInstrument(instr);
                instrumentEditPanel1.setEditingLibraryObject(false);
                userInstrumentLibrary.deselect();

            }
        });

        reinitialize();
    }

    private void reinitialize() {
        BlueProject project = BlueProjectManager.getInstance().getCurrentProject();
        if (project == null) {
            instrumentEditPanel1.editInstrument(null);
            arrangementPanel.setMixer(null);
            arrangementPanel.setArrangement(null);
        } else {
            instrumentEditPanel1.editInstrument(null);
            arrangementPanel.setMixer(project.getData().getMixer());
            arrangementPanel.setArrangement(project.getData().getArrangement());
        }
    }

    public void editInstrument(Instrument instrument) {
        instrumentEditPanel1.editInstrument(instrument);
    }

    public Instrument getInstrumentFromLibrary() {
        return userInstrumentLibrary.getSelectedInstrument();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        instrumentEditPanel1 = new blue.ui.core.orchestra.InstrumentEditPanel();

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(0, 0));

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(0, 0));
        jSplitPane1.setLeftComponent(jSplitPane2);
        jSplitPane1.setRightComponent(instrumentEditPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private blue.ui.core.orchestra.InstrumentEditPanel instrumentEditPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    // End of variables declaration//GEN-END:variables

    public static synchronized OrchestraTopComponent getDefault() {
        if (instance == null) {
            instance = new OrchestraTopComponent();
        }
        return instance;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    /**
     * replaces this in object stream
     */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return OrchestraTopComponent.getDefault();
        }
    }
}
