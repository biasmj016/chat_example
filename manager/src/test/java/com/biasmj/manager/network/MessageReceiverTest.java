package com.biasmj.manager.network;

import com.biasmj.manager.application.ManagerChat;
import com.biasmj.manager.network.request.ChatCreateRequest;
import com.biasmj.manager.network.request.ChatLeaveRequest;
import com.biasmj.manager.network.MessageReceiver;
import com.biasmj.manager.network.MessageSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.biasmj.manager.network.request.ChatMessageRequest;

import static org.junit.jupiter.api.Assertions.*;

class MessageReceiverTest {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Socket serverSideSocket;
    private MessageReceiver messageReceiver;
    private MessageSender messageSender;

    @BeforeEach
    public void setUp() throws IOException {
        ManagerChat.chatGUI = null; // GUI 초기화
        ManagerChat.chatList.clear(); // 이전 테스트 데이터 초기화
        ManagerChat.participants.clear(); // 이전 참여자 데이터 초기화
        serverSocket = new ServerSocket(12345);
        clientSocket = new Socket("127.0.0.1", 12345);
        serverSideSocket = serverSocket.accept();

        messageReceiver = new MessageReceiver(serverSideSocket);
        messageSender = new MessageSender(clientSocket);

        new Thread(messageReceiver).start();
    }

    @AfterEach
    public void tearDown() throws IOException {
        clientSocket.close();
        serverSideSocket.close();
        serverSocket.close();
    }

    @Test
    public void testMessageReception() throws InterruptedException {
        String testMessage = "Hello!,24-12-2021 12:00";
        messageSender.sendMessage(new ChatMessageRequest("chatting", "manager", testMessage));
        Thread.sleep(1000);
        System.out.println(ManagerChat.chatList);
        assertTrue(ManagerChat.chatList.get(0).contains("Hello!"));
    }
}