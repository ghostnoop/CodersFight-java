/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.task;


import ru.kpfu.itis.codersfight.server.server.Server;
import ru.kpfu.itis.codersfight.server.service.GameService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameTask extends Thread {

    private Socket socket;
    private Server server;
    private GameService gameService;

    private DataInputStream in;
    private DataOutputStream out;
    private int[] gameMap;



    public GameTask(Socket socket, Server server, GameService gameService) {
        this.socket = socket;
        this.server = server;
        this.gameService = gameService;
    }
    private void initialiseGameMap(){
        gameMap=new int[]{
                0,0,0,
                0,0,0,
                0,0,0
        };
    }

    @Override
    public void run() {

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String msg = in.readUTF();
                System.out.println(msg);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
