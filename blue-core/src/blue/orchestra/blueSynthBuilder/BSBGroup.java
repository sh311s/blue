/*
 * blue - object composition environment for csound
 * Copyright (C) 2017 stevenyi
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package blue.orchestra.blueSynthBuilder;

import blue.automation.ParameterList;
import blue.mixer.Channel;
import blue.utility.ObjectUtilities;
import electric.xml.Element;
import electric.xml.Elements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/**
 *
 * @author stevenyi
 */
public class BSBGroup extends BSBObject implements Iterable<BSBObject>, UniqueNameCollection, Randomizable {

    private transient ParameterList parameterList;
    private transient ObservableSet<BSBObject> allSet;
    private StringProperty groupName = new SimpleStringProperty("Group");

    private Set<BSBObject> backingSet = new HashSet<BSBObject>() {
        @Override
        public boolean add(BSBObject bsbObj) {
            String objName = bsbObj.getObjectName();

            // guarantee unique names for objects
            if (objName != null && objName.length() != 0 && unm != null) {
                if (!unm.isUniquelyNamed(bsbObj)) {
                    unm.setUniqueName(bsbObj);
                }
            }

            if (bsbObj instanceof BSBGroup) {
                if (unm != null) {
                    UniqueNameManager tempUNM = new UniqueNameManager();
                    Set<String> additionalNames = new HashSet<>();
                    tempUNM.setUniqueNameCollection(() -> {
                        Set<String> temp = new HashSet<>();
                        temp.addAll(additionalNames);
                        if (unm.getUniqueNameCollection() != null) {
                            temp.addAll(unm.getUniqueNameCollection().getNames());
                        }
                        return temp;
                    });
                    ((BSBGroup) bsbObj).makeNamesUnique(tempUNM, additionalNames);
                }
                ((BSBGroup) bsbObj).setParameterList(parameterList);
            }

            bsbObj.setUniqueNameManager(unm);

            return super.add(bsbObj);
        }
    };

    private ObservableSet<BSBObject> interfaceItems
            = FXCollections.observableSet(backingSet);

    SetChangeListener<BSBObject> itemsListener = (change) -> {
        BSBObject bsbObj;
        if (allSet != null) {
            if (change.wasAdded()) {
                bsbObj = change.getElementAdded();
                allSet.add(bsbObj);

                if (bsbObj instanceof BSBGroup) {
                    ((BSBGroup) bsbObj).setAllSet(allSet);
                    ((BSBGroup) bsbObj).setParameterList(parameterList);
                }
                if (bsbObj instanceof AutomatableBSBObject) {
                    ((AutomatableBSBObject) bsbObj).setParameterList(
                            parameterList);
                }
                bsbObj.setUniqueNameManager(unm);
            } else if (change.wasRemoved()) {
                bsbObj = change.getElementRemoved();
                allSet.remove(bsbObj);
                if (bsbObj instanceof BSBGroup) {
                    ((BSBGroup) bsbObj).setAllSet(null);
                    ((BSBGroup) bsbObj).removeParameters();
                } else if (bsbObj instanceof AutomatableBSBObject) {
                    ((AutomatableBSBObject) bsbObj).setAutomationAllowed(false);
                }
            }
        }
    };

    public BSBGroup() {
        interfaceItems.addListener(itemsListener);
    }

    public BSBGroup(BSBGroup group) {
        super(group);
        for (BSBObject bsbObj : group.interfaceItems) {
            interfaceItems.add(bsbObj.deepCopy());
        }

        interfaceItems.addListener(itemsListener);
        setGroupName(group.getGroupName());
        // FIXME - double check that not sharing UNM is correct (UNM 
//        unm = group.unm;

    }

    public ObservableSet<BSBObject> interfaceItemsProperty() {
        return interfaceItems;
    }

    public void addBSBObject(BSBObject bsbObj) {
        if (bsbObj != null) {
            interfaceItems.add(bsbObj);
        }
    }

    @Override
    public Iterator<BSBObject> iterator() {
        return interfaceItems.iterator();
    }

    public int size() {
        return interfaceItems.size();
    }

    @Override
    public void setupForCompilation(BSBCompilationUnit compilationUnit) {
        for (BSBObject bsbObj : interfaceItems) {
            bsbObj.setupForCompilation(compilationUnit);
        }
    }

    @Override
    public Set<String> getNames() {
        Set<String> names = new HashSet<>();

        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof BSBGroup) {
                names.addAll(((BSBGroup) bsbObj).getNames());
            } else {
                String[] replacementKeys = bsbObj.getReplacementKeys();

                if (replacementKeys != null) {
                    names.addAll(Arrays.asList(replacementKeys));
                }
            }
        }

        return names;
    }

    public static BSBGroup loadFromXML(Element data) throws Exception {
        BSBGroup bsbGroup = new BSBGroup();
        initBasicFromXML(data, bsbGroup);

        String groupName = data.getAttributeValue("groupName");
        if(groupName != null) {
            bsbGroup.setGroupName(groupName);
        }

        Elements elems = data.getElements();

        while (elems.hasMoreElements()) {
            Element node = elems.next();
            String name = node.getName();

            switch (name) {
                case "bsbObject":
                    Object obj = ObjectUtilities.loadFromXML(node);
                    //FIXME
                    bsbGroup.addBSBObject((BSBObject) obj);
                    break;
            }
        }
        return bsbGroup;
    }

    @Override
    public Element saveAsXML() {
        Element retVal = super.getBasicXML(this);

        retVal.setAttribute("groupName", getGroupName());

//        retVal.setAttribute("editEnabled", Boolean.toString(isEditEnabled()));
        for (BSBObject bsbObj : interfaceItems) {
            retVal.addElement(bsbObj.saveAsXML());
        }

        return retVal;
    }

    @Override
    public String getPresetValue() {
        return null;
    }

    @Override
    public void setPresetValue(String val) {
    }

    @Override
    public BSBObject deepCopy() {
        return new BSBGroup(this);
    }

    public void randomize() {
        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof Randomizable) {
                Randomizable randomizable = (Randomizable) bsbObj;
                if (randomizable.isRandomizable()) {
                    randomizable.randomize();
                }
            }
        }
    }

    @Override
    public boolean isRandomizable() {
        return true;
    }

    @Override
    public void setRandomizable(boolean randomizable) {
        // ignore
    }

    @Override
    public void setUniqueNameManager(UniqueNameManager unm) {
        this.unm = unm;

        if (unm != null) {
            for (BSBObject bsbObj : interfaceItems) {
                bsbObj.setUniqueNameManager(unm);
            }
        }
    }

    public void resetSubChannels() {
        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof BSBSubChannelDropdown) {
                ((BSBSubChannelDropdown) bsbObj)
                        .setChannelOutput(Channel.MASTER);
            } else if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).resetSubChannels();
            }
        }
    }

    public void getStringChannels(ArrayList<StringChannel> stringChannels) {

        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).getStringChannels(stringChannels);
            } else if (bsbObj instanceof StringChannelProvider) {
                StringChannelProvider provider = (StringChannelProvider) bsbObj;
                if (provider.isStringChannelEnabled()) {
                    stringChannels.add(provider.getStringChannel());
                }
            }
        }
    }

    public void setParameterList(ParameterList paramList) {
        this.parameterList = paramList;

        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).setParameterList(paramList);
            } else if (bsbObj instanceof AutomatableBSBObject) {
                ((AutomatableBSBObject) bsbObj).setParameterList(paramList);
            }
        }

    }

//    /**
//     * Retrieves all BSBObjects from hierarchy as a flat set
//     */
//    public Set<BSBObject> getAllBSBObjects() {
//        Set<BSBObject> retVal = new HashSet<>();
//        for (BSBObject bsbObj : interfaceItems) {
//            if (bsbObj instanceof BSBGroup) {
//                retVal.addAll(((BSBGroup) bsbObj).getAllBSBObjects());
//            } else {
//                retVal.add(bsbObj);
//            }
//        }
//        return retVal;
//    }
    public void setAllSet(ObservableSet<BSBObject> allSet) {
        if (allSet == null && this.allSet != null) {
            this.allSet.remove(this);
            this.allSet.removeAll(this.interfaceItems);
        }

        this.allSet = allSet;

        if (allSet != null) {
            allSet.add(this);
            allSet.addAll(interfaceItems);
        }

        for (BSBObject bsbObj : interfaceItems) {
            if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).setAllSet(allSet);
            }
        }
    }

    protected void removeParameters() {
        for (BSBObject bsbObj : interfaceItemsProperty()) {
            if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).removeParameters();
            } else if (bsbObj instanceof AutomatableBSBObject) {
                ((AutomatableBSBObject) bsbObj).setAutomationAllowed(false);
            }
        }

    }

    public final void setGroupName(String value) {
        groupName.set(value);
    }

    public final String getGroupName() {
        return groupName.get();
    }

    public final StringProperty groupNameProperty() {
        return groupName;
    }

    /**
     * Shallow check if this group is the parent of the given bsbObj
     */
    public boolean contains(BSBObject bsbObj) {
        return interfaceItems.contains(bsbObj);
    }

    private void makeNamesUnique(UniqueNameManager unm,
            Set<String> additionalNames) {
        if (unm == null) {
            return;
        }

        for (BSBObject bsbObj : interfaceItems) {
            // guarantee unique names for objects
            if (bsbObj instanceof BSBGroup) {
                ((BSBGroup) bsbObj).makeNamesUnique(unm, additionalNames);
            } else {
                String objName = bsbObj.getObjectName();

                if (objName != null && objName.length() != 0 && unm != null) {
                    if (!unm.isUniquelyNamed(bsbObj)) {
                        unm.setUniqueName(bsbObj);
                        additionalNames.addAll(
                                Arrays.asList(bsbObj.getReplacementKeys()));
                    }
                }
            }

        }
    }

}
