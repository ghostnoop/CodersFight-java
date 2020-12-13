package ru.kpfu.itis.codersfight.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.itis.codersfight.server.model.Answer;
import ru.kpfu.itis.codersfight.server.server.Server;
import ru.kpfu.itis.codersfight.server.service.GameService;
import ru.kpfu.itis.codersfight.server.task.GameTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

@Setter
@Getter
public class ClientHandler extends Thread {
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;

    private Boolean isActive = Boolean.TRUE;
    private Boolean isGame = Boolean.FALSE;

    private int positionOnMap;
    private GameTask gameTask;


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
            sendMsg(GameService.gameMapToString(gameTask.getMapByPos(positionOnMap)));

        } else if (command[0].equals("/getmap")) {
            sendMsg(GameService.gameMapToString(gameTask.getMapByPos(positionOnMap)));

        } else if (command[0].equals("/whom")) {
            sendMsg(String.valueOf((gameTask.getWhoseMove() == positionOnMap)));

        } else if (command[0].equals("/attack")) {
            try {
                final StringWriter sw = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(sw, gameTask.getCurrentQuestion());

                gameTask.setAttackPosition(Integer.parseInt(command[1]));
                gameTask.setAnswerStart();

                gameTask.sendAttackToPlayers(sw.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command[0].equals("/answer")) {
            System.out.println("answer thread" + this.getThreadGroup().getName());
            gameTask.getAnswers()[positionOnMap].gameEnd();
            gameTask.getAnswers()[positionOnMap].setRight(gameTask.getCurrentQuestion()
                    .checkAnswer(Integer.parseInt(command[1])));
            gameTask.checkEndGame();

        }


    }


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
