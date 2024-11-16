package com.biasmj.participant;

import com.biasmj.participant.application.ParticipantChat;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ParticipantApplication {

    public static void main(String[] args) throws IOException {
        new ParticipantChat();
    }

}
