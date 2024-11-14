package com.biasmj.server.chat.domain;

import com.biasmj.server.chat.domain.type.RequestType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatServerTest {

    private ChatServer chatServer;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        chatServer = new ChatServer();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> chatServer.run());
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    void testChatServerReceivesAndResponds() throws Exception {
        try (Socket clientSocket = new Socket("localhost", 9090);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("CONNECT:Test");

            String response = in.readLine();
            System.out.println("Response from server: " + response);

            assertTrue(response != null && !response.isEmpty());
        }
    }

    @Test
    void testCreateAndJoinChat() throws Exception {
        Socket clientSocket = new Socket("localhost", 9090);
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // CREATE 요청을 보냅니다.
            String createMessage = String.format("%s:%s", RequestType.CREATE, "ChatRoom1,User1,5");
            out.println(createMessage);

            // 서버 응답을 수신합니다.
            String response = in.readLine();
            System.out.println("Response for CREATE: " + response);
        } finally {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        }
    }
}