package ru.kpfu.itis.codersfight.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import ru.kpfu.itis.codersfight.client.renders.CellRender;
import ru.kpfu.itis.codersfight.client.service.SocketService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<String> listView;
    public TextField messageField;
    public Button sendButton;
    public ScrollPane scrollPane;
    private ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());

    private String name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setEditable(true);
        listView.setItems(observableList);
        listView.setCellFactory(new CellRender());
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                socketService.chat(observableList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void sendMessage(ActionEvent actionEvent) throws IOException {
        SocketService.getInstance().sendMessage(messageField.getText());
//        observableList.add(messageField.getText());
//        messageField.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
