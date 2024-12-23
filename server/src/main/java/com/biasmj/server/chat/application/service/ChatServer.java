package com.biasmj.server.chat.application.service;

import com.biasmj.server.chat.application.usecase.InitChatData;
import com.biasmj.server.chat.application.handler.ThreadHandler;
import com.biasmj.server.chat.application.handler.response.ChatInitDataResponse;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.infrastructure.ParticipantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);
    public static final List<Socket> sockets = new ArrayList<>();
    private final ChatDao chatDao;
    private final ParticipantDao participantDao;

    public ChatServer() {
        this.chatDao = new ChatDao.ChatDaoImpl();
        this.participantDao = new ParticipantDao.ParticipantDaoImpl();
    }

    public void run() {
        try (ServerSocket serverSocket = createServerSocket()) {
            while (!serverSocket.isClosed()) {
                logger.info("Waiting for connection...");
                try {
                    Socket socket = serverSocket.accept();
                    logger.info("Participant connected: IP = {}, Port = {}", socket.getInetAddress(), socket.getPort());

                    sockets.add(socket);

                    ThreadHandler thread = new ThreadHandler(socket, chatDao, participantDao);
                    thread.start();

                    InitChatData initChatData = new InitChatData.InitChatDataImpl(chatDao);
                    thread.sendMessage(new ChatInitDataResponse(initChatData.finds()));
                } catch (IOException e) {
                    logger.error("Error accepting participant connection", e);
                }
            }
        } catch (IOException e) {
            logger.error("Error starting server", e);
        }
    }

    protected ServerSocket createServerSocket() throws IOException {
        return new ServerSocket(9090);
    }
}