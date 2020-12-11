package ru.kpfu.itis.codersfight.client.renders;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class CellRender implements Callback<ListView<String>, ListCell<String>> {

    @Override
    public ListCell<String> call(ListView<String> param) {
        return new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(null);
                if (!empty) {
                    HBox hBox = new HBox();
                    hBox.setFillHeight(true);
                    Label label = new Label(item);
                    CornerRadii cr = new CornerRadii(10);
                    Color color;
                    if (getIndex() % 2 == 0) {
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        color = Color.web("#000051");
                    } else {
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                        color = Color.web("#534bae");
                    }

                    label.setBackground(new Background(new BackgroundFill(color, cr, Insets.EMPTY)));
                    label.setWrapText(true);
                    label.setPadding(new Insets(15,15,15,15));
                    label.setTextFill(Paint.valueOf("#fcfcfc"));

                    VBox vBox = new VBox();
                    vBox.setMaxWidth(300);
                    vBox.getChildren().add(label);
                    hBox.getChildren().add(vBox);
                    setGraphic(hBox);
                }
            }
        };
    }
}
