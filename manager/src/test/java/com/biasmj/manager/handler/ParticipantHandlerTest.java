package com.biasmj.manager.handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ParticipantHandlerTest {

    private Socket mockSocket;
    private DataInputStream mockInput;
    private DataOutputStream mockOutput;
    private ParticipantHandler participantHandler;

    @BeforeEach
    void setUp() throws IOException {
        mockSocket = Mockito.mock(Socket.class);
        mockInput = Mockito.mock(DataInputStream.class);
        mockOutput = Mockito.mock(DataOutputStream.class);

        when(mockSocket.getInputStream()).thenReturn(mockInput);
        when(mockSocket.getOutputStream()).thenReturn(mockOutput);

        participantHandler = new ParticipantHandler(mockSocket, "testID");
    }

    @AfterEach
    void tearDown() throws IOException {
        participantHandler.close();
    }

    @Test
    void close() throws IOException {
        participantHandler.close();
        verify(mockInput, times(1)).close();
        verify(mockOutput, times(1)).close();
        verify(mockSocket, times(1)).close();
    }



    @Test
    void sendMessage() {
        String message = "Test message";

        Assertions.assertDoesNotThrow(() -> participantHandler.sendMessage(message), "sendMessage should not throw IOException");
    }

}