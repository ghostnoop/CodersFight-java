/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ru.kpfu.itis.codersfight.client.model.Question;
import ru.kpfu.itis.codersfight.client.service.SocketService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GlobalMapController implements Initializable {
    public Button img00;
    public Button img10;
    public Button img20;

    public Button img01;
    public Button img11;
    public Button img21;

    public Button img02;
    public Button img12;
    public Button img22;


    public AnchorPane questionLayout;
    public Label titleLabel;
    public Button ans1;
    public Button ans2;
    public Button ans3;
    public Button ans4;


    private String[] enumStatus = new String[]{
            "default", "own", "enemy"
    };
    private Button[] imageViews;
    private Integer[] terrainArray;
    // 0 - neutral, 1 - my own, 2 - enemy

    private boolean isYourStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                terrainArray = socketService.getStartGameMap();
                isYourStage = socketService.whoseStage();
                System.out.println("stage: "+isYourStage);

                Platform.runLater(() -> {
                    updateMap();
                    if (!isYourStage)
                        waitStage();
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        imageViews = new Button[]{
                img00, img10, img20, img01, img11, img21, img02, img12, img22
        };
        imagesClickHandlers();


    }

    private void waitStage() {
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                String msg = socketService.waitQuestion();
                System.out.println("message: " + msg);

                Platform.runLater(() -> {
                    getQuestion(msg);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();
    }

    private void updateMap() {
        int k = 1;
        for (int i = 0; i < terrainArray.length; i++) {
            String status = enumStatus[terrainArray[i]];
            String path = "/" + status + "/__0" + k + ".png";
            imageViews[i].setStyle("-fx-background-image: url(" + path + ")");
            if (terrainArray[i] == 0) {
                imageViews[i].setDisable(false);
            } else imageViews[i].setDisable(true);
            k++;
        }

    }




    private void imagesClickHandlers() {
        int k = 0;
        SocketService socketService = SocketService.getInstance();

        for (Button imageView : imageViews) {
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    if (isYourStage) {
                        socketService.sendMessage("/attack " + imageView.getId());
                        String msg = socketService.waitQuestion();
                        getQuestion(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setDisable(true);
                event.consume();
            });
        }
    }

    private void getQuestion(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Question question = mapper.readValue(msg, Question.class);
            setGameQuestion(question);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void setGameQuestion(Question question) {
        questionLayout.setVisible(true);
        titleLabel.setText(question.getTitle());
        String[] answers = question.getAnswers();
        ans1.setText(answers[0]);
        ans2.setText(answers[1]);
        ans3.setText(answers[2]);
        ans4.setText(answers[3]);
    }
}
