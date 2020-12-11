package ru.kpfu.itis.codersfight.server.task;


import ru.kpfu.itis.codersfight.server.handler.ClientHandler;
import ru.kpfu.itis.codersfight.server.model.Client;
import ru.kpfu.itis.codersfight.server.server.Server;
import ru.kpfu.itis.codersfight.server.service.AuthenticationService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthenticationTask extends Thread {

    private Socket socket;
    private Server server;
    private AuthenticationService authenticationService;

    private DataInputStream in;
    private DataOutputStream out;

    public AuthenticationTask(Socket socket, Server server , AuthenticationService authenticationService) {
        this.socket = socket;
        this.server = server;
        this.authenticationService = authenticationService;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String msg = in.readUTF();
                System.out.println(msg);

                if (msg.startsWith("/auth")){
                    String[] creds = msg.split(" ");
                    Client client = authenticationService.authenticate(creds[1], creds[2]);
                    if (client != null) {
                        out.writeUTF("/auth_success");
                        server.subscribe(new ClientHandler(server, socket));
                        break;
                    } else {
                        out.writeUTF("/auth_failed");
                    }

                } else {
                    out.writeUTF("/unauthorized");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
