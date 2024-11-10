package com.biasmj.participant;

import com.biasmj.participant.ui.ParticipantChatIntroGUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class ParticipantChatApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParticipantChatIntroGUI::new);
    }
}
