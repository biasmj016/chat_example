package com.biasmj.server.chat.infrastructure;

import com.biasmj.server.chat.domain.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public interface ChatDao {
    Chat addChat(Chat chat);
    Chat updateChat(Chat chat);
    void removeChat(Chat chat);
    Chat findChat(String chatName);
    List<Chat> findAllChats();

    class ChatDaoImpl implements ChatDao {
        private final List<Chat> chats = new ArrayList<>();

        @Override
        public Chat addChat(Chat chat) {
            chats.add(chat);
            return chat;
        }

        @Override
        public Chat updateChat(Chat chat) {
            removeChat(chat);
            chats.add(chat);
            return chat;
        }

        @Override
        public void removeChat(Chat chat) {
            chats.removeIf(c -> c.name().equals(chat.name()));
        }

        @Override
        public Chat findChat(String chatName) {
            return chats.stream()
                    .filter(chat -> chat.name().equals(chatName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Chat not found"));
        }

        @Override
        public List<Chat> findAllChats() {
            return new ArrayList<>(chats);
        }
    }
}