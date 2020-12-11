package ru.kpfu.itis.codersfight.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.kpfu.itis.codersfight.client.Main;
import ru.kpfu.itis.codersfight.client.service.SocketService;
import ru.kpfu.itis.codersfight.client.window.WindowManager;

import java.io.IOException;

public class LoginController {
    public PasswordField passwordField;
    public TextField loginField;
    public Button loginButton;


    public void login(ActionEvent actionEvent) throws IOException {
        Platform.runLater(() -> {
            SocketService socketService = SocketService.getInstance();
            boolean auth = false;
            try {
                auth = socketService.auth(loginField.getText(), passwordField.getText());
                if (auth) {
                    WindowManager.renderQueueWindow(Main.primaryStage);
                } else {
                    WindowManager.alert("Ошибка", "Неверный логин или пароль");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
