/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.task;


import lombok.Getter;
import lombok.Setter;
import ru.kpfu.itis.codersfight.server.handler.ClientHandler;
import ru.kpfu.itis.codersfight.server.model.Answer;
import ru.kpfu.itis.codersfight.server.model.Question;
import ru.kpfu.itis.codersfight.server.service.GameService;
import ru.kpfu.itis.codersfight.server.service.QuestionsCreator;

import java.util.Arrays;

@Getter
@Setter
public class GameTask {
    private ClientHandler player1;
    private ClientHandler player2;

    private int[] terrains = new int[]{1, 1};
    private Integer[] gameMapPlayer1;
    private Integer[] gameMapPlayer2;
    private Question currentQuestion = null;
    private int attackPosition = -1;
    private long currentTime = 0L;
    private int whoseMove = 0;
    private Answer[] answers = new Answer[]{null, null};
    private boolean gameIsEndInThisThread = false;
    private int whoseWin = -1;

    public GameTask(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;

        player1.setPositionOnMap(0);
        player2.setPositionOnMap(1);

        gameMapPlayer1 = GameService.getGameMap(0);
        gameMapPlayer2 = GameService.getGameMap(1);
    }

    private void forNextGame() {
        currentQuestion = null;
        gameIsEndInThisThread = false;
        for (int i = 0; i < 2; i++) {
            answers[i] = null;
        }
    }

    public void checkEndGame() {
        if (gameIsEndInThisThread) {
            return;
        }

        boolean isEnd = true;
        for (int i = 0; i < 2; i++) {
            if (!answers[i].isGameEnd) {
                isEnd = false;
                break;
            }
        }
        if (isEnd) {
            gameIsEndInThisThread = true;
            checkAnswers();
        }
    }

    private void checkAnswers() {
        if (answers[0].getEndTime() > answers[1].getEndTime()) {
            if (answers[0].isRight()) {
                terrains[0]++;
                gameMapPlayer1[attackPosition] = 1;
                gameMapPlayer2[attackPosition] = 2;
                whoseWin = 0;
            } else if (answers[1].isRight()) {
                terrains[1]++;
                gameMapPlayer1[attackPosition] = 2;
                gameMapPlayer2[attackPosition] = 1;
                whoseWin = 1;
            }
        } else {
            if (answers[1].isRight()) {
                terrains[1]++;
                gameMapPlayer1[attackPosition] = 2;
                gameMapPlayer2[attackPosition] = 1;
                whoseWin = 1;
            } else if (answers[0].isRight()) {
                terrains[0]++;
                gameMapPlayer1[attackPosition] = 1;
                gameMapPlayer2[attackPosition] = 2;
                whoseWin = 0;
            }
        }
        getBack();
    }

    private void getBack() {
        if (whoseMove == 0)
            whoseMove = 1;
        else
            whoseMove = 0;
        System.out.println("terrains");
        System.out.println(Arrays.toString(terrains));
        if (terrains[0]+terrains[1]==9){
            if (terrains[0]>terrains[1]){
                player1.sendMsg("/backmap 2");
                player2.sendMsg("/backmap 3");
            }
            else if (terrains[0]<terrains[1]){
                player1.sendMsg("/backmap 3");
                player2.sendMsg("/backmap 2");
            }
            else {
                player1.sendMsg("/backmap 4");
                player2.sendMsg("/backmap 4");
            }
            return;
        }

        if (whoseWin == 0) {
            player1.sendMsg("/backmap 1");
        } else {
            player1.sendMsg("/backmap 0");
        }

        if (whoseWin == 1) {
            player2.sendMsg("/backmap 1");
        } else {
            player2.sendMsg("/backmap 0");
        }


        forNextGame();
    }

    public void sendAttackToPlayers(String msg) {
        player1.sendMsg(msg);
        player2.sendMsg(msg);
    }

    public void setAnswerStart() {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            answers[i] = new Answer(currentTime);
        }
    }

    public Integer[] getMapByPos(int pos) {
        if (pos == 0) return gameMapPlayer1;
        if (pos == 1) return gameMapPlayer2;
        return null;
    }

    public Question getCurrentQuestion() {
        if (currentQuestion != null) {
            return currentQuestion;
        } else {
            currentQuestion = QuestionsCreator.getNewQuestion();
            return currentQuestion;
        }
    }


}
