package com.biasmj.manager.service;

import com.biasmj.manager.domain.ChatObserver;
import com.biasmj.manager.handler.ParticipantHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public sealed interface ManageChat permits ManageChat.ManageChatService {
    void startServer(int port) throws IOException;
    void broadcast(String message);
    void addObserver(ChatObserver observer);
    void removeObserver(ChatObserver observer);

    final class ManageChatService implements ManageChat {
        private static final Logger logger = Logger.getLogger(ManageChatService.class.getName());
        private ServerSocket serverSocket;
        private final List<ChatObserver> chatObservers = new CopyOnWriteArrayList<>();

        @Override
        public void startServer(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            logger.info("Chat started on " + InetAddress.getLocalHost() + " : " + port);

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && !serverSocket.isClosed()) {
                    try {
                        Socket socket = serverSocket.accept();
                        logger.info("Participant connected from " + socket.getRemoteSocketAddress());
                        ParticipantHandler participantHandler = new ParticipantHandler(socket);
                        addObserver(participantHandler);
                        participantHandler.start();
                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) {
                            logger.log(Level.SEVERE, "Error accepting participant", e);
                        }
                    }
                }
            }).start();
        }

        public void stopServer() throws IOException {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        }

        @Override
        public void addObserver(ChatObserver observer) {
            chatObservers.add(observer);
        }

        @Override
        public void removeObserver(ChatObserver observer) {
            chatObservers.remove(observer);
        }

        @Override
        public void broadcast(String message) {
            chatObservers.forEach(observer -> observer.update(message));
        }
        public ServerSocket getServerSocket() {
            return serverSocket;
        }

        public List<ChatObserver> getChatObservers() {
            return chatObservers;
        }
    }
}