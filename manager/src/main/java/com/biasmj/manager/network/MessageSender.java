package com.biasmj.manager.network;

import com.biasmj.manager.domain.MessageType;
import com.biasmj.manager.domain.type.RequestType;
import com.biasmj.manager.network.request.ChatCreateRequest;
import com.biasmj.manager.network.request.ChatLeaveRequest;
import com.biasmj.manager.network.request.ChatMessageRequest;

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

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            switch (type) {
                case CREATE -> writer.println((ChatCreateRequest) messageType);
                case LEAVE -> writer.println((ChatLeaveRequest) messageType);
                case MESSAGE -> writer.println((ChatMessageRequest) messageType);
            }
            writer.flush();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error sending message", e);
        }

    }
}