package ru.kpfu.itis.codersfight.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketService {

    private static SocketService socketService = new SocketService();
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public SocketService() {
        try {
            this.socket = new Socket("localhost", 8080);
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SocketService getInstance() {
        return socketService;
    }

    public void sendMessage(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public Integer[] getGameMap() throws IOException {
        sendMessage("/getmap");
        String msg = in.readUTF();
        System.out.println("/getmap");
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(msg, Integer[].class);
    }

    public Integer[] getStartGameMap() throws IOException {
        sendMessage("/map");
        String msg = in.readUTF();
        System.out.println("/map");
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(msg, Integer[].class);
    }



    public boolean whoseStage() throws IOException {
        sendMessage("/whom");
        String msg = in.readUTF();
        return msg.startsWith("true");
    }

    public String waitQuestion() throws IOException {
        System.out.println("tut");
        while (true) {
            System.out.println("tut2");
            String response = in.readUTF();
            System.out.println("mess: "+response);
            return response;
        }
    }

    public boolean auth(String name, String pass) throws IOException {
        out.writeUTF("/auth " + name + " " + pass);

        String response = in.readUTF();
        System.out.println(response);
        return response.startsWith("/auth_success");
    }

    public int getCommands() throws IOException {
        while (true) {
            String msg = in.readUTF();
            System.out.println(msg);
            if (msg.startsWith("/foundgame")) {
                return 1;
            } else if (msg.startsWith("/defend")) {
                return Integer.parseInt(msg.split(" ")[1]);
            } else
                System.out.println(msg);
        }
    }

    public void chat(ObservableList<String> observableList) throws IOException {
        while (true) {
            String msg = in.readUTF();
            System.out.println(msg);
            Platform.runLater(() -> {
                observableList.add(msg);
            });
        }
    }
}
