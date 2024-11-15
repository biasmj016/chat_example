package com.biasmj.manager;

import com.biasmj.manager.application.ManagerChat;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ManagerApplication {

    public static void main(String[] args) throws IOException {
        new ManagerChat();
    }

}
