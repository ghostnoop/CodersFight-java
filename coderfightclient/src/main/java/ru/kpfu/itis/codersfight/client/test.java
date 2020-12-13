/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        final Stage popup = new Stage();
        popup.setX(primaryStage.getX() + (primaryStage.getScene().getWidth() - 200) / 2);
        popup.setY(primaryStage.getY() + (primaryStage.getScene().getHeight() - 200) / 2);
        StackPane root = new StackPane();
        popup.setScene(new Scene(root));
        popup.initOwner(primaryStage);
        popup.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                primaryStage.requestFocus();
            }
        });
        popup.initStyle(StageStyle.TRANSPARENT);
        popup.setOpacity(0.5);
        popup.show();
    }
}
