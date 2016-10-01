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

import blue.orchestra.blueSynthBuilder.BSBXYController;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author stevenyi
 */
public class BSBXYControllerView extends BorderPane {

    private final BSBXYController bsbXYController;

    public BSBXYControllerView(BSBXYController bsbXYController) {
        setUserData(bsbXYController);

        this.bsbXYController = bsbXYController;

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black");

        Label label = new Label();

        label.setFont(new Font(10));
        label.setTextFill(Color.WHITE);

        setCenter(pane);
        setBottom(label);

        Rectangle yLine = new Rectangle();
        yLine.setX(0);
        yLine.setHeight(1.0);
        yLine.setFill(Color.WHITE);

        Rectangle xLine = new Rectangle();
        xLine.setY(0);
        xLine.setWidth(1.0);

        xLine.setFill(Color.WHITE);

        Rectangle rect = new Rectangle(3, 3);
        rect.setFill(Color.color(0, 1.0, 0));

        pane.getChildren().addAll(xLine, yLine, rect);

        sceneProperty().addListener((obs, old, newVal) -> {

        });

        pane.setOnMousePressed(me -> {
            bsbXYController.xValueProperty().setNormalizedValue(
                    me.getX() / pane.getWidth()
            );
            bsbXYController.yValueProperty().setNormalizedValue(
                    1.0 - (me.getY() / pane.getHeight())
            );
        });
        pane.setOnMouseDragged(me -> {
            if (me.getSource() == pane) {
                bsbXYController.xValueProperty().setNormalizedValue(
                        me.getX() / pane.getWidth()
                );
                bsbXYController.yValueProperty().setNormalizedValue(
                        1.0 - (me.getY() / pane.getHeight())
                );
            }
        });

        sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal == null) {
                pane.prefWidthProperty().unbind();
                pane.prefHeightProperty().unbind();
                label.textProperty().unbind();
                label.prefWidthProperty().unbind();
                yLine.widthProperty().unbind();
                yLine.yProperty().unbind();
                xLine.heightProperty().unbind();
                xLine.xProperty().unbind();
                rect.yProperty().unbind();
                rect.xProperty().unbind();
            } else {
                pane.prefWidthProperty().bind(bsbXYController.widthProperty());
                pane.prefHeightProperty().bind(bsbXYController.heightProperty());
                label.textProperty().bind(
                        Bindings.format("x: %.4g y: %.4g",
                                bsbXYController.xValueProperty().valueProperty(),
                                bsbXYController.yValueProperty().valueProperty(),
                                bsbXYController.xValueProperty().maxProperty(),
                                bsbXYController.xValueProperty().minProperty(),
                                bsbXYController.yValueProperty().maxProperty(),
                                bsbXYController.yValueProperty().minProperty()));
                label.prefWidthProperty().bind(pane.prefWidthProperty());
                yLine.widthProperty().bind(pane.widthProperty());
                yLine.yProperty().bind(Bindings.createDoubleBinding(() -> {
                    double percent = bsbXYController.yValueProperty().getNormalizedValue();
                    return Math.floor(pane.getPrefHeight() * (1 - percent));
                },
                        bsbXYController.yValueProperty().valueProperty(),
                        bsbXYController.yValueProperty().minProperty(),
                        bsbXYController.yValueProperty().maxProperty()
                ));
                xLine.heightProperty().bind(pane.heightProperty());
                xLine.xProperty().bind(Bindings.createDoubleBinding(() -> {
                    double percent = bsbXYController.xValueProperty().getNormalizedValue();
                    return Math.floor(pane.getPrefWidth() * percent);
                },
                        bsbXYController.xValueProperty().valueProperty(),
                        bsbXYController.xValueProperty().minProperty(),
                        bsbXYController.xValueProperty().maxProperty()
                ));
                rect.xProperty().bind(Bindings.createDoubleBinding(()
                        -> (int) (bsbXYController.xValueProperty().getNormalizedValue()
                        * pane.getPrefWidth()) - 1.0,
                        bsbXYController.xValueProperty().valueProperty(),
                        bsbXYController.xValueProperty().maxProperty(),
                        bsbXYController.xValueProperty().minProperty()));

                rect.yProperty().bind(Bindings.createDoubleBinding(()
                        -> (int) ((1.0 - bsbXYController.yValueProperty().getNormalizedValue())
                        * pane.getPrefHeight()) - 1.0,
                        bsbXYController.yValueProperty().valueProperty(),
                        bsbXYController.yValueProperty().maxProperty(),
                        bsbXYController.yValueProperty().minProperty()));
            }
        });
    }
}
