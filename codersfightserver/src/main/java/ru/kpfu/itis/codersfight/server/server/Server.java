package ru.kpfu.itis.codersfight.server.server;

import lombok.SneakyThrows;
import ru.kpfu.itis.codersfight.server.handler.ClientHandler;
import ru.kpfu.itis.codersfight.server.service.AuthenticationService;
import ru.kpfu.itis.codersfight.server.task.AuthenticationTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;
    private List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
    private ConcurrentLinkedQueue<ClientHandler> queueClients = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                socket = serverSocket.accept();
                System.out.println("client connected");
                new AuthenticationTask(socket, this, new AuthenticationService()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    todo after auth set search loop for 2 players

    @SneakyThrows
    public void subscribe(ClientHandler clientHandler) {

        if (queueClients.size() > 0) {
            System.out.println("tut");
            ClientHandler oldClientHandler = queueClients.poll();

            clientHandler.setEnemyClient(oldClientHandler);
            oldClientHandler.setEnemyClient(clientHandler);

            oldClientHandler.start();
            clientHandlers.add(oldClientHandler);
            oldClientHandler.setPositionOnMap(0);
            oldClientHandler.setYourStage(false);

            clientHandler.start();
            clientHandlers.add(clientHandler);
            clientHandler.setPositionOnMap(1);
            clientHandler.setYourStage(true);

            oldClientHandler.sendMsg("/foundgame");
            clientHandler.sendMsg("/foundgame");
        }
        else
            queueClients.add(clientHandler);


    }

    public void broadcast(String msg) {
        clientHandlers.stream().filter(ClientHandler::getIsActive).forEach(clientHandlers -> {
            System.out.println("/test");
            System.out.println(msg);
            clientHandlers.sendMsg(msg);
        });
    }
}
