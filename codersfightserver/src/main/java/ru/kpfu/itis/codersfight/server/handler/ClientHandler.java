package ru.kpfu.itis.codersfight.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
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
        }
        else if (command[0].equals("/getmap")) {
            String result = GameService.gameMapToString(
                    gameMap
            );
            sendMsg(result);
        }
        else if (command[0].equals("/whom")) {

            sendMsg(isYourStage + "");
        }
        else if (command[0].equals("/attack")) {
            try {
                final StringWriter sw = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(sw, getQuestion());

                System.out.println(sw.toString());

                enemyClient.sendMsg(sw.toString());

                sendMsg(sw.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private Question getQuestion() {
        Question question = new Question(
                "test",
                new String[]{"1", "2", "3", "3"},
                "2"
        );
        return question;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
