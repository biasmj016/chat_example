package com.biasmj.participant.application;

import com.biasmj.participant.network.MessageReceiver;
import com.biasmj.participant.network.MessageSender;
import com.biasmj.participant.ui.ParticipantChatGUI;
import com.biasmj.participant.ui.ParticipantChatIntroGUI;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ParticipantChat {

   public static Socket socket;
    public static MessageSender sender;
    public static MessageReceiver receiver;
    public static ParticipantChatIntroGUI introGUI;
    public static ParticipantChatGUI chatGUI;
    public static List<String> chatRoomList = new ArrayList<>();
    public static List<String> participants = new ArrayList<>();
    public static List<String> chatList = new ArrayList<>();


    public ParticipantChat() throws IOException {
        socket = new Socket("127.0.0.1", 9090);
        System.out.println("connect success to chat server");

        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
        receiver.start();

        introGUI = new ParticipantChatIntroGUI();
    }
}