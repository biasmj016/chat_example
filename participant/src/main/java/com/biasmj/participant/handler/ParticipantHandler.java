package com.biasmj.participant.handler;

import com.biasmj.participant.domain.ChatObserver;
import com.biasmj.participant.service.ManageChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipantHandler extends Thread implements ChatObserver, AutoCloseable{
    private static final String MESSAGE_FORMAT = "%s : %s";

    private final DataInputStream in;
    private final DataOutputStream out;
    private final String participantID;
    private final Socket socket;

    public ParticipantHandler(Socket socket, String participantID) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.participantID = participantID;
    }

    @Override
    public void run() {
        try (this) {
            out.writeUTF(participantID);
            while (!Thread.currentThread().isInterrupted()) {
                String message = in.readUTF();
                update(String.format(MESSAGE_FORMAT, participantID, message));
            }
        } catch (IOException e) {
            Logger.getLogger(ParticipantHandler.class.getName()).info(participantID + " left the chat.");
        }
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            Logger.getLogger(ParticipantHandler.class.getName()).log(Level.SEVERE, "Error sending message to " + participantID, e);
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