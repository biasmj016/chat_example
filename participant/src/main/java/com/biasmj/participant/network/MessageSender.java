package com.biasmj.participant.network;

import com.biasmj.participant.domain.MessageType;
import com.biasmj.participant.domain.type.RequestType;
import com.biasmj.participant.network.request.ChatJoinRequest;
import com.biasmj.participant.network.request.ChatLeaveRequest;
import com.biasmj.participant.network.request.ChatMessageRequest;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender {
    private static final Logger logger = Logger.getLogger(MessageSender.class.getName());
    private final Socket socket;

    public MessageSender(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(MessageType messageType) {
        RequestType type = messageType.getRequestType();
        logger.info("Send message: " + messageType);
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            switch (type) {
                case JOIN -> writer.println((ChatJoinRequest) messageType);
                case LEAVE -> writer.println((ChatLeaveRequest) messageType);
                case MESSAGE -> writer.println((ChatMessageRequest) messageType);
            }
            writer.flush();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error sending message", e);
        }

    }
}