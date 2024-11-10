package com.biasmj.participant.service;

import com.biasmj.participant.domain.ChatObserver;
import com.biasmj.participant.handler.ParticipantHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ManageChat {
    void startServer(int port) throws IOException;

    void stopServer() throws IOException;

    void broadcast(String message);

    void addObserver(ChatObserver observer);

    void removeObserver(ChatObserver observer);

    void connectToServer(int port, String participantID) throws IOException;

    public final class ManageChatService implements ManageChat {
        private static final Logger logger = Logger.getLogger(ManageChat.class.getName());
        private ServerSocket serverSocket;
        private final List<ChatObserver> chatObservers = new CopyOnWriteArrayList<>();

        @Override
        public void startServer(int port) throws IOException {
            InetAddress localIpAddress = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(port, 50, localIpAddress);
            logger.info("Chat started on " + localIpAddress + " : " + port);

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && !serverSocket.isClosed()) {
                    try {
                        Socket socket = serverSocket.accept();
                        logger.info("Participant connected from " + socket.getRemoteSocketAddress());
                        ParticipantHandler participantHandler = new ParticipantHandler(socket, "participantID");
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

        @Override
        public void stopServer() throws IOException {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        }

        @Override
        public void broadcast(String message) {
            chatObservers.forEach(observer -> observer.update(message));
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
        public void connectToServer(int port, String participantID) throws IOException {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            Socket socket = new Socket(ipAddress, port);
            ParticipantHandler participantHandler = new ParticipantHandler(socket, participantID);
            addObserver(participantHandler);
            participantHandler.start();
        }

        public ServerSocket getServerSocket() {
            return serverSocket;
        }

        public List<ChatObserver> getChatObservers() {
            return chatObservers;
        }
    }
}