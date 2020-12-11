/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import ru.kpfu.itis.codersfight.client.Main;
import ru.kpfu.itis.codersfight.client.service.SocketService;
import ru.kpfu.itis.codersfight.client.window.WindowManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QueueController implements Initializable {
    public ProgressIndicator pb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                int command = socketService.getCommands();
                if (command==1){
                    Platform.runLater(() -> {
                        try {
                            WindowManager.renderGameWindow(Main.primaryStage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
