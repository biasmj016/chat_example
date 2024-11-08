package com.biasmj.manager;

import com.biasmj.manager.ui.ManagerChatIntroGUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class ManagerChatApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagerChatIntroGUI::new);
    }
}
