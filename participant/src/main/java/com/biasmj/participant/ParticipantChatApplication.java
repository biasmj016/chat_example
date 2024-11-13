package com.biasmj.participant;

import com.biasmj.participant.domain.ChatParticipant;
import com.biasmj.participant.ui.ParticipantChatIntroGUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.io.IOException;

@SpringBootApplication
public class ParticipantChatApplication {
    public static void main(String[] args) throws IOException {
        new ChatParticipant();
        SwingUtilities.invokeLater(ParticipantChatIntroGUI::new);
    }
}
