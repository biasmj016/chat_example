package com.biasmj.server.chat.application.handler;

import com.biasmj.server.chat.application.service.ChatServer;
import com.biasmj.server.chat.application.handler.request.ChatMessageRequest;
import com.biasmj.server.chat.application.handler.response.ChatCreateResponse;
import com.biasmj.server.chat.application.handler.response.ChatInitDataResponse;
import com.biasmj.server.chat.application.handler.response.ChatMessageResponse;
import com.biasmj.server.chat.application.handler.response.ParticipantInitDataResponse;
import com.biasmj.server.chat.application.usecase.*;
import com.biasmj.server.chat.application.usecase.CreateChat.CreateChatRequest;
import com.biasmj.server.chat.application.usecase.JoinChat.JoinChatRequest;
import com.biasmj.server.chat.application.usecase.LeaveChat.LeaveChatRequest;
import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.application.usecase.FindParticipant;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ThreadHandler.class);
    private static final String WELCOME_MESSAGE = " joined the chat.";
    private static final String BYE_MESSAGE = " left the chat.";
    private final Socket socket;
    private final InitChatData initChatData;
    private final FindChat findChat;
    private final FindParticipant findParticipant;
    private final CreateChat createChat;
    private final JoinChat joinChat;
    private final LeaveChat leaveChat;

    public ThreadHandler(Socket socket, ChatDao chatDao, ParticipantDao participantDao) {
        this.socket = socket;
        this.initChatData = new InitChatData.InitChatDataImpl(chatDao);
        this.findChat = new FindChat.FindChatImpl(chatDao);
        this.findParticipant = new FindParticipant.FindParticipantImpl(participantDao);
        this.createChat = new CreateChat.CreateChatImpl(chatDao, participantDao);
        this.joinChat = new JoinChat.JoinChatImpl(chatDao, participantDao);
        this.leaveChat = new LeaveChat.LeaveChatImpl(chatDao, participantDao);
    }


    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String str;
            while ((str = reader.readLine()) != null) {
                String[] token = str.split("#");
                RequestType type = RequestType.valueOf(token[0]);
                String message = token[1];
                processReceiveMessage(type, message);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            logger.error("Error in ThreadHandler run method", e);
        }
    }

    public void processReceiveMessage(RequestType type, String message) {
        Chat chat;
        Participant participant;
        switch (type) {
            case JOIN -> {
                JoinChatRequest request = new JoinChatRequest(socket, message);
                chat = joinChat.execute(new JoinChatRequest(socket, message));
                sendMessage(new ChatMessageResponse(chat.name(), request.participantID(), request.participantID()+WELCOME_MESSAGE));
                sendMessage(new ParticipantInitDataResponse(chat.participants()));
            }
            case CREATE -> {
                chat = createChat.execute(new CreateChatRequest(socket, message));
                sendMessage(new ChatCreateResponse(chat.name(), chat.managerID()));
                sendMessage(new ChatMessageResponse(chat.name(), chat.managerID(), chat.managerID()+WELCOME_MESSAGE));
                sendMessage(new ParticipantInitDataResponse(chat.participants()));
            }
            case LEAVE -> {
                LeaveChatRequest chatLeaveRequest = new LeaveChatRequest(message);
                chat = findChat.find(chatLeaveRequest.chatName());
                participant = findParticipant.find(chatLeaveRequest.participantID());

                sendMessage(new ChatMessageResponse(chatLeaveRequest.chatName(), participant.participantID(), participant.participantID() + BYE_MESSAGE));
                sendMessage(new ParticipantInitDataResponse(chat.participants()));

                leaveChat.execute(chatLeaveRequest);

                sendMessage(new ChatInitDataResponse(initChatData.finds()));
            }
            case MESSAGE -> {
                ChatMessageRequest messageRequest = new ChatMessageRequest(message);
                sendMessage(new ChatMessageResponse(messageRequest.getChatName(), messageRequest.getParticipantID(), messageRequest.getMessage()));
            }
        }
    }

    public void sendMessage(MessageType message) {
        if (socket == null) {
            logger.error("Socket is null, cannot send message");
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            switch (message.getRequestType()) {
                case CONNECT -> {
                    ChatInitDataResponse connectInitData = (ChatInitDataResponse) message;
                    writer.println(connectInitData);
                }
                case JOIN, LEAVE -> {
                    ParticipantInitDataResponse participantInitData = (ParticipantInitDataResponse) message;
                    for (Participant participant : participantInitData.getParticipantList()) {
                        PrintWriter participantWriter = new PrintWriter(participant.socket().getOutputStream(), true);
                        participantWriter.println(participantInitData);
                    }
                }
                case CREATE -> {
                    ChatCreateResponse chatCreateResponse = (ChatCreateResponse) message;
                    for (Socket s : ChatServer.sockets) {
                        PrintWriter socketWriter = new PrintWriter(s.getOutputStream(), true);
                        socketWriter.println(chatCreateResponse);
                    }
                }
                case MESSAGE -> {
                    ChatMessageResponse messageResponse = (ChatMessageResponse) message;
                    Chat chat = findChat.find(messageResponse.getChatName());
                    for (Participant participant : chat.participants()) {
                        PrintWriter participantWriter = new PrintWriter(participant.socket().getOutputStream(), true);
                        participantWriter.println(messageResponse);
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            logger.error("Error in sendMessage method", e);
        }
    }

    private void closeSocket() {
        try {
            socket.close();
            logger.info("Socket closed.");
            ChatServer.sockets.remove(socket);
        } catch (IOException e) {
            logger.error("Error closing socket", e);
        }
    }
}