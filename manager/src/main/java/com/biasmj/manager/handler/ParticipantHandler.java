package com.biasmj.manager.handler;

import com.biasmj.manager.domain.ChatObserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipantHandler extends Thread implements ChatObserver, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ParticipantHandler.class.getName());
    private static final String MESSAGE_FORMAT = "%s : %s";

    private final DataInputStream in;
    private final DataOutputStream out;
    private final String participantID;
    private final Socket socket;

    public ParticipantHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.participantID = in.readUTF();
    }

    public ParticipantHandler(Socket socket, String participantID) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.participantID = participantID;
    }

    @Override
    public void run() {
        try (this) {
            while (!Thread.currentThread().isInterrupted()) {
                String message = in.readUTF();
                sendMessage(String.format(MESSAGE_FORMAT, participantID, message));
            }
        } catch (IOException e) {
            logger.info(participantID + " left the chat.");
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending message to " + participantID, e);
        }
    }

    @Override
    public void update(String message) {
        sendMessage(message);
    }

    @Override
    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) socket.close();
    }
}