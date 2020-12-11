package ru.kpfu.itis.codersfight.server.service;


import ru.kpfu.itis.codersfight.server.model.Client;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private List<Client> clients = new ArrayList<Client>(){
        {add(new Client("test", "123"));}
        {add(new Client("test2", "123"));}
    };

    public Client authenticate(String name, String pass){
        return clients.stream()
                .filter(client -> client.getName().equals(name) && client.getPassword().equals(pass))
                .findFirst()
                .orElse(null);
    }
}
