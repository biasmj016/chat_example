package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;

public interface FindChat {
    Chat find(String chatName);

    class FindChatImpl implements FindChat {
        private final ChatDao chatDao;

        public FindChatImpl() {
            this.chatDao = new ChatDao.ChatDaoImpl();
        }
        public FindChatImpl(ChatDao chatDao) {this.chatDao = chatDao;}

        @Override
        public Chat find(String chatName) {
            return chatDao.findChat(chatName);
        }
    }

}