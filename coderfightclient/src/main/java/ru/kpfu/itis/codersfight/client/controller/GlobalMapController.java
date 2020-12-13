/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ru.kpfu.itis.codersfight.client.Main;
import ru.kpfu.itis.codersfight.client.model.Question;
import ru.kpfu.itis.codersfight.client.service.SocketService;
import ru.kpfu.itis.codersfight.client.window.WindowManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GlobalMapController implements Initializable {
    public Label moveLabel;

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
    public Label textLabel;
    public Button ans1;
    public Button ans2;
    public Button ans3;
    public Button ans4;
    public AnchorPane waitingLoader;
    public ProgressIndicator pb;

    private String[] enumStatus = new String[]{
            "default", "own", "enemy"
    };
    private Button[] imageViews;
    private Integer[] terrainArray;
    // 0 - neutral, 1 - my own, 2 - enemy

    private boolean isYourStage;
    private boolean isAnswerListenerInit = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                terrainArray = socketService.getStartGameMap();
                updateUI();


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

    private void waitAnswer() {
        new Thread(() -> {
            SocketService socketService = SocketService.getInstance();
            try {
                int stateMove = socketService.waitBackFromServer();
                terrainArray = socketService.getGameMap();
                updateUI(stateMove);


                Platform.runLater(() -> {
                    backToMap();
                    waitingLoader.setVisible(false);
                    updateMap();
                    if (!isYourStage)
                        waitStage();
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();
    }

    private void backToMap() {
        questionLayout.setVisible(false);

    }

    private void updateUI() throws IOException {
        SocketService socketService = SocketService.getInstance();
        isYourStage = socketService.whoseStage();
        System.out.println("stage: " + isYourStage);
        Platform.runLater(() -> {


            if (isYourStage)
                moveLabel.setText("Your move");
            else
                moveLabel.setText("Enemy move");
        });
    }

    private void updateUI(int stateMove) throws IOException {
        SocketService socketService = SocketService.getInstance();
        isYourStage = socketService.whoseStage();
        System.out.println("stage: " + isYourStage);
        Platform.runLater(() -> {


            stateOfMove(stateMove);


            if (isYourStage)
                moveLabel.setText("Your move");
            else
                moveLabel.setText("Enemy move");
        });
    }

    private void stateOfMove(int stateMove) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (stateMove == 1) {
            alert.setTitle("Вы захватили территорию!");
            alert.setHeaderText("У вас " +
                    Arrays.stream(terrainArray).filter(terrainArray -> terrainArray == 1).count()
                    + " территорий");
        }
        else if (stateMove == 0) {
            alert.setTitle("Вы не смогли захватить территорию!");
            alert.setHeaderText("У вас " +
                    Arrays.stream(terrainArray).filter(terrainArray -> terrainArray == 1).count()
                    + " территорий");

        }
        else if (stateMove==2){
            alert.setTitle("Вы выиграли!");
            alert.setHeaderText("У вас большинство доступных территорий");
        }
        else if (stateMove==3){
            alert.setTitle("Вы проиграли!");
            alert.setHeaderText("У вас меньше территорий");
        }
        else if (stateMove==4){
            alert.setTitle("Ничья!");
            alert.setHeaderText("У вас столько же территорий");
        }
        alert.showAndWait();
        if (stateMove==2 || stateMove==3 || stateMove==4){
            SocketService socketService = SocketService.getInstance();
            socketService.disconnect();
            try {
                WindowManager.renderLoginWindow(Main.primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            int finalK = k;
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (isYourStage) {
                    Platform.runLater(() -> {
                        try {
                            System.out.println("click");
                            socketService.sendMessage("/attack " + finalK);
                            String msg = socketService.waitQuestion();
                            getQuestion(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


                }


                imageView.setDisable(true);
                event.consume();
            });
            k++;
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
        Button[] answersButtons = new Button[]{
                ans1, ans2, ans3, ans4
        };

        questionLayout.setVisible(true);
        titleLabel.setText(question.getTitle());
        textLabel.setText(question.getText());
        String[] answers = question.getAnswers();
        for (int i = 0; i < answersButtons.length; i++) {
            answersButtons[i].setText(answers[i]);
        }
        if (!isAnswerListenerInit)
            setAnswerListener();


    }

    private void setAnswerListener() {
        Button[] answersButtons = new Button[]{
                ans1, ans2, ans3, ans4
        };

        isAnswerListenerInit = true;
        int index = 0;
        SocketService socketService = SocketService.getInstance();
        for (Button answerButton : answersButtons) {

            int finalIndex = index;
            answerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                System.out.println("index key :" + finalIndex);
                try {
                    socketService.sendMessage("/answer " + finalIndex);
                    waitAnswer();
//                    todo set progress bar menu
                    Platform.runLater(() -> {
                        pb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                        waitingLoader.setVisible(true);
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.consume();
            });
            index++;
        }
    }
}
