package com.biasmj.server;

import com.biasmj.server.chat.application.service.ChatServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {new ChatServer().run();}
}
