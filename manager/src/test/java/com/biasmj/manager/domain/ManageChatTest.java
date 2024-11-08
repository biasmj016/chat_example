package com.biasmj.manager.domain;

import com.biasmj.manager.service.ManageChat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ManageChatTest {
    private ManageChat.ManageChatService manageChatService;

    @BeforeEach
    void setUp() {
        manageChatService = new ManageChat.ManageChatService();
    }

    @AfterEach
    void tearDown() throws IOException {
        manageChatService.stopServer();
    }

    @Test
    void startServer() {
        assertDoesNotThrow(() -> manageChatService.startServer(8080));
        assertNotNull(manageChatService);
    }

    @Test
    void addObserver() {
        ChatObserver observer = Mockito.mock(ChatObserver.class);
        manageChatService.addObserver(observer);

        assertTrue(manageChatService.getChatObservers().contains(observer));
    }

    @Test
    void removeObserver() {
        ChatObserver observer = Mockito.mock(ChatObserver.class);
        manageChatService.addObserver(observer);
        manageChatService.removeObserver(observer);

        assertFalse(manageChatService.getChatObservers().contains(observer));
    }

    @Test
    void broadcastMessage() {
        ChatObserver observer1 = Mockito.mock(ChatObserver.class);
        ChatObserver observer2 = Mockito.mock(ChatObserver.class);

        manageChatService.addObserver(observer1);
        manageChatService.addObserver(observer2);

        String message = "Test message";
        manageChatService.broadcast(message);

        verify(observer1, times(1)).update(message);
        verify(observer2, times(1)).update(message);
    }

    @Test
    void stopServer() throws IOException {
        manageChatService.startServer(12345);
        manageChatService.stopServer();

        ServerSocket serverSocket = manageChatService.getServerSocket();
        assertTrue(serverSocket.isClosed(), "Server socket should be closed after stopServer() is called");
    }
}
