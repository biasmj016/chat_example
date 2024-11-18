package com.biasmj.manager.application;

import com.biasmj.manager.network.MessageReceiver;
import com.biasmj.manager.network.MessageSender;
import com.biasmj.manager.ui.ManagerChatGUI;
import com.biasmj.manager.ui.ManagerChatIntroGUI;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ManagerChat {

   public static Socket socket;
    public static MessageSender sender;
    public static MessageReceiver receiver;
    public static ManagerChatGUI chatGUI;
    public static List<String> participants = new ArrayList<>();
    public static List<String> chatList = new ArrayList<>();


    public ManagerChat() throws IOException {
        socket = new Socket("127.0.0.1", 9090);
        System.out.println("connect success to chat server");

        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
        receiver.start();

        SwingUtilities.invokeLater(ManagerChatIntroGUI::new);
    }
}