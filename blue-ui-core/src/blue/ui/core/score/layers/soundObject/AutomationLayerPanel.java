/*
 * blue - object composition environment for csound
 * Copyright (c) 2000-2006 Steven Yi (stevenyi@gmail.com)
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
package blue.ui.core.score.layers.soundObject;

import blue.SoundLayer;
import blue.automation.ParameterLinePanel;
import blue.components.AlphaMarquee;
import blue.score.TimeState;
import blue.score.layers.Layer;
import blue.score.layers.LayerGroupDataEvent;
import blue.score.layers.LayerGroupListener;
import blue.soundObject.PolyObject;
import blue.ui.core.score.MultiLineScoreSelection;
import blue.ui.core.score.MultiLineScoreSelectionListener;
import blue.ui.core.score.soundLayer.SoundLayerLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.JComponent;

public class AutomationLayerPanel extends JComponent implements
        PropertyChangeListener, LayerGroupListener,
        MultiLineScoreSelectionListener {

    SoundLayerLayout layout = new SoundLayerLayout();

    private PolyObject pObj = null;

    private TimeState timeState = null;

    private AlphaMarquee marquee;

    private int scaleLayerNum = -1;

    MultiLineScoreSelection selection = MultiLineScoreSelection.getInstance();

    public AutomationLayerPanel(AlphaMarquee marquee) {
        this.setLayout(layout);
        this.marquee = marquee;
    }

    public void setPolyObject(PolyObject pObj, TimeState timeState) {
        if (this.pObj != null && this.pObj.isRoot()) {
            this.timeState.removePropertyChangeListener(this);
            this.pObj.removeLayerGroupListener(this);
        }

        Component[] components = this.getComponents();
        this.removeAll();

        for (int i = 0; i < components.length; i++) {
            ((ParameterLinePanel) components[i]).cleanup();
        }

        layout.setPolyObject(pObj);

        this.pObj = pObj;
        this.timeState = timeState;

        if (pObj != null && pObj.isRoot()) {
            this.timeState.addPropertyChangeListener(this);
            this.pObj.addLayerGroupListener(this);
        }

        this.populate();
    }

    private void populate() {
        if (pObj == null || !pObj.isRoot()) {
            return;
        }

        for (int i = 0; i < pObj.size(); i++) {
            SoundLayer sLayer = pObj.get(i);

            ParameterLinePanel paramPanel = new ParameterLinePanel(this.marquee);
            paramPanel.setTimeState(timeState);
            paramPanel.setParameterIdList(sLayer.getAutomationParameters());

            this.add(paramPanel);

        }
        revalidate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == this.pObj) {
            if (evt.getPropertyName().equals("heightIndex")) {
                revalidate();
            }
        }
    }

//    public void addSelectionDragRegion(float startTime, float endTime, int layerNum) {
//        if(layerNum >= getComponentCount()) {
//            return;
//        }
//        
//        ParameterLinePanel paramLinePanel = (ParameterLinePanel) getComponent(layerNum);
//        paramLinePanel.addSelectionDragRegion(startTime, endTime);
//    }
    @Override
    public void removeNotify() {
        MultiLineScoreSelection.getInstance().removeListener(this);
        super.removeNotify();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        MultiLineScoreSelection.getInstance().addListener(this);
    }

    @Override
    public void multiLineSelectionUpdated(MultiLineScoreSelection.UpdateType updateType) {
        switch (updateType) {
            case SELECTION:
                setMultiLineDragStart(selection.getStartTime(),
                        selection.getEndTime(),
                        selection.getSelectedLayers());
                break;
            case TRANSLATION_START:
                setMultiLineDragStart(selection.getStartTime(),
                        selection.getEndTime(),
                        selection.getSelectedLayers());
                break;
            case TRANSLATION:
                setMultiLineTranslation(selection.getTranslationTime());
                break;
            case TRANSLATION_COMPLETE:
                commitMultiLineDrag();
                break;
            case CLEAR:
                clearMultiLineTranslation();
                break;
        }
    }

    public void setMultiLineDragStart(float startTime, float endTime,
            Collection<Layer> selectedLayers) {
        for (int i = 0; i < getComponentCount(); i++) {
            ParameterLinePanel paramLinePanel = (ParameterLinePanel) getComponent(
                    i);

            if (selectedLayers != null && selectedLayers.contains(pObj.get(i))) {
                paramLinePanel.setSelectionDragRegion(startTime, endTime);
            } else {
                paramLinePanel.clearSelectionDragRegions();
            }
        }
    }

    public void commitMultiLineDrag() {
        for (int i = 0; i < getComponentCount(); i++) {
            ParameterLinePanel paramLinePanel = (ParameterLinePanel) getComponent(
                    i);
            paramLinePanel.commitMultiLineDrag();
        }
    }

    public void setMultiLineTranslation(float timeTranslate) {
        for (int i = 0; i < getComponentCount(); i++) {
            ParameterLinePanel paramLinePanel = (ParameterLinePanel) getComponent(
                    i);
            paramLinePanel.setMultiLineMouseTranslation(timeTranslate);
        }
    }

    public void clearMultiLineTranslation() {
        for (int i = 0; i < getComponentCount(); i++) {
            ParameterLinePanel paramLinePanel = (ParameterLinePanel) getComponent(
                    i);
            paramLinePanel.clearSelectionDragRegions();
        }
    }

    /* SCORE SCALING */
    public void initiateScoreScale(float startTime, float endTime, int scaleLayerNum) {
        this.scaleLayerNum = scaleLayerNum;

        ParameterLinePanel paramLinePanel
                = ((ParameterLinePanel) this.getComponent(this.scaleLayerNum));
        paramLinePanel.initiateScoreScale(startTime, endTime);
    }

    public void setScoreScaleStart(float newSelectionStartTime) {
        ParameterLinePanel paramLinePanel
                = ((ParameterLinePanel) this.getComponent(this.scaleLayerNum));
        paramLinePanel.setScoreScaleStart(newSelectionStartTime);
    }

    public void setScoreScaleEnd(float newSelectionEndTime) {
        ParameterLinePanel paramLinePanel
                = ((ParameterLinePanel) this.getComponent(this.scaleLayerNum));
        paramLinePanel.setScoreScaleEnd(newSelectionEndTime);
    }

    public void endScoreScale() {
        ParameterLinePanel paramLinePanel
                = ((ParameterLinePanel) this.getComponent(this.scaleLayerNum));
        paramLinePanel.commitScoreScale();
        this.scaleLayerNum = -1;
    }

    @Override
    public void layerGroupChanged(LayerGroupDataEvent event) {
        switch (event.getType()) {
            case LayerGroupDataEvent.DATA_ADDED:
                layersAdded(event);
                break;
            case LayerGroupDataEvent.DATA_REMOVED:
                layersRemoved(event);
                break;
            case LayerGroupDataEvent.DATA_CHANGED:
                contentsChanged(event);
                break;
        }
    }

    public void contentsChanged(LayerGroupDataEvent e) {
        int start = e.getStartIndex();
        int end = e.getEndIndex();

        // This is a hack to determine what direction the layers were
        // pushed
        boolean isUp = ((start >= 0) && (end >= 0));

        if (isUp) {
            Component c = getComponent(start);
            remove(start);
            add(c, end);

        } else {
            // have to flip because listDataEvent stores as min and max
            Component c = getComponent(-start);
            remove(-start);
            add(c, -end);
        }

        revalidate();
    }

    public void layersAdded(LayerGroupDataEvent e) {
        int index = e.getStartIndex();
        SoundLayer sLayer = pObj.get(index);

        ParameterLinePanel paramPanel = new ParameterLinePanel(this.marquee);
        paramPanel.setTimeState(timeState);
        paramPanel.setParameterIdList(sLayer.getAutomationParameters());

        // this.add(paramPanel);
        this.add(paramPanel, index);

        revalidate(); // is this necessary?
    }

    public void layersRemoved(LayerGroupDataEvent e) {
        int start = e.getStartIndex();
        int end = e.getEndIndex();

        for (int i = end; i >= start; i--) {
            Component c = getComponent(i);
            remove(i);
            ((ParameterLinePanel) c).cleanup();
        }

        revalidate(); // is this necessary?
    }

}
