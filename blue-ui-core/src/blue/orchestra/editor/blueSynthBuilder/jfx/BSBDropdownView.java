/*
 * blue - object composition environment for csound
 * Copyright (C) 2016
 * Steven Yi <stevenyi@gmail.com>
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
package blue.orchestra.editor.blueSynthBuilder.jfx;

import blue.orchestra.blueSynthBuilder.BSBDropdown;
import blue.orchestra.blueSynthBuilder.BSBDropdownItem;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.openide.util.Exceptions;

/**
 *
 * @author stevenyi
 */
public class BSBDropdownView extends ComboBox<BSBDropdownItem> {

    private final BSBDropdown dropdown;
    boolean mutating = false;

    public BSBDropdownView(BSBDropdown dropdown) {
        super(dropdown.dropdownItemsProperty());
        setUserData(dropdown);

        getStylesheets().add(getClass().getResource("bsbDropdown.css").toExternalForm());
//        ListCell<BSBDropdownItem> cell = new ListCell<BSBDropdownItem>() {
//             public void updateItem(BSBDropdownItem item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (!empty) {
//                        setText(item.getName());
//                    } else {
//                        setText(null);
//                    }
//                }
//        };
//        cell.setStyle("-fx-padding:4 1 4 1");
//        setCellFactory(lv -> cell);
        this.dropdown = dropdown;
//        setPrefWidth(USE_COMPUTED_SIZE);

        ChangeListener<? super Number> cl = (obs, old, newVal) -> {
            if (!mutating) {
                mutating = true;

                if (!Platform.isFxApplicationThread()) {
                    CountDownLatch latch = new CountDownLatch(1);
                    Platform.runLater(() -> {
                        try {
                            getSelectionModel().select(newVal.intValue());
                        } finally {
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    getSelectionModel().select(newVal.intValue());
                }

                mutating = false;
            }
        };

        getSelectionModel().selectedIndexProperty().addListener((obs, o, newVal)
                -> {
            if (!mutating) {
                mutating = true;
                dropdown.setSelectedIndex(newVal.intValue());
                mutating = false;
            }
        });

        ChangeListener<? super Number> fontListener = (obs, old, newVal) -> {
            updateFont(dropdown.getFontSize());
        };

        InvalidationListener listListener = o -> {
            updateWidth();
        };

        sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal == null) {
                dropdown.selectedIndexProperty().removeListener(cl);
                dropdown.fontSizeProperty().removeListener(fontListener);
                dropdown.getBSBDropdownItemList().removeListener(listListener);
            } else {
                getSelectionModel().select(dropdown.getSelectedIndex());
                dropdown.selectedIndexProperty().addListener(cl);
                dropdown.fontSizeProperty().addListener(fontListener);
                dropdown.getBSBDropdownItemList().addListener(listListener);
            }
        });

        updateFont(dropdown.getFontSize());
        updateWidth();
    }

    protected void updateFont(int fontSize) {
        setStyle(String.format("-fx-font-size: %dpx;", fontSize));

    }

    protected void updateWidth() {
        Font f = Font.font("System Regular", dropdown.getFontSize());
        Text t = new Text();
        t.setFont(f);
        double w = 0;

        for (BSBDropdownItem item : dropdown.getBSBDropdownItemList()) {
            t.setText(item.getName());
            w = Math.max(w, t.getLayoutBounds().getWidth());
        }

        w += 24;

        setPrefWidth(w);
        setWidth(w);
    }
}
