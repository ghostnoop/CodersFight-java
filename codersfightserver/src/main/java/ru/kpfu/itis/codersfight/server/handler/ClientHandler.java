package ru.kpfu.itis.codersfight.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.itis.codersfight.server.model.Answer;
import ru.kpfu.itis.codersfight.server.model.Question;
import ru.kpfu.itis.codersfight.server.server.Server;
import ru.kpfu.itis.codersfight.server.service.GameService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    @Getter
    private Boolean isActive = Boolean.TRUE;
    @Setter
    @Getter
    private Boolean isGame = Boolean.FALSE;
    @Setter
    @Getter
    private ClientHandler enemyClient;
    @Setter
    @Getter
    private int positionOnMap;
    @Getter
    @Setter
    private boolean isYourStage;
    @Getter
    @Setter
    private Integer[] gameMap;
    private Question currentQuestion;
    @Getter
    private int answerIndex = -1;
    @Setter
    private Answer answer=null;

    private int terrains = 1;

    private int attackPosition = -1;
    private long currentTime= 0L;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            isActive = Boolean.FALSE;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = in.readUTF();
                System.out.println(msg);
                gameCommands(msg);
            } catch (IOException e) {
                e.printStackTrace();
                isActive = Boolean.FALSE;
                break;
            }
        }
    }

    private void gameCommands(String msg) {
        String[] command = msg.trim().split(" ");
        if (command[0].equals("/map")) {
            gameMap = GameService.getGameMap(positionOnMap);
            sendMsg(GameService.gameMapToString(gameMap)
            );
        } else if (command[0].equals("/getmap")) {
            sendMsg(GameService.gameMapToString(gameMap));
        } else if (command[0].equals("/whom")) {

            sendMsg(isYourStage + "");
        } else if (command[0].equals("/attack")) {
            try {
                final StringWriter sw = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(sw, getQuestion());
                attackPosition = Integer.parseInt(command[1]);
                enemyClient.attackPosition = attackPosition;
                System.out.println(sw.toString());

                enemyClient.sendMsg(sw.toString());

                sendMsg(sw.toString());

                currentTime = System.currentTimeMillis();
//                answer = new Answer(currentTime);
//                enemyClient.setAnswer(new Answer(currentTime));
//                System.out.println(enemyClient.answer.isGameEnd);
//                System.out.println(answer.getStartTime());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command[0].equals("/answer")) {
            try {
                if (answer==null){
                    answer=new Answer(currentTime);

                }
                if (enemyClient.answer==null){
                    enemyClient.setAnswer(new Answer(currentTime));
                }

                System.out.println(answer);
                if (answer.isGameEnd)
                    return;
                answer.gameEnd();
                answerIndex = Integer.parseInt(command[1]);
                answer.setRight(currentQuestion.checkAnswer(Integer.parseInt(command[1])));
                if (enemyClient.answer.isGameEnd && answer.isGameEnd) {
                    System.out.println("zdez");
                    checkWinner();
                }
            } catch (NullPointerException e) {
                System.out.println(e);
                e.printStackTrace();
            }

        }


    }

    private void checkWinner() {
        System.out.println("qeqweqewqewqeqweqwe");
//        todo get global win if terraing >5 smt
        if (answer.getEndTime() > enemyClient.answer.getEndTime()) {
            if (answer.isRight()) {
                terrains++;
                gameMap[attackPosition] = 1;
                enemyClient.gameMap[attackPosition] = 2;
            } else if (enemyClient.answer.isRight()) {
                enemyClient.terrains++;
                gameMap[attackPosition] = 2;
                enemyClient.gameMap[attackPosition] = 1;
            }
        } else {
            if (enemyClient.answer.isRight()) {
                enemyClient.terrains++;
                gameMap[attackPosition] = 2;
                enemyClient.gameMap[attackPosition] = 1;
            } else if (answer.isRight()) {
                terrains++;
                gameMap[attackPosition] = 1;
                enemyClient.gameMap[attackPosition] = 2;
            }
        }
        forNextGame();
        enemyClient.forNextGame();
        sendMsg("/backmap");
        enemyClient.sendMsg("/backmap");

//        reset
//        back to map
    }

    private void forNextGame() {
        attackPosition = -1;
        currentQuestion = null;
        answerIndex = -1;
        isYourStage = !isYourStage;
    }

    private Question getQuestion() {
        currentQuestion = new Question(
                "test",
                "text test",
                new String[]{"1", "2", "3", "3"},
                1
        );
        enemyClient.currentQuestion = currentQuestion;

        return currentQuestion;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
