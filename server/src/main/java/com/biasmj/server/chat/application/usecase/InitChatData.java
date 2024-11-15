package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;

import java.util.List;

public interface InitChatData {
    List<String> finds();

    class InitChatDataImpl implements InitChatData {
        private final ChatDao chatDao;

        public InitChatDataImpl(ChatDao chatDao) {this.chatDao = chatDao;}

        @Override
        public List<String> finds() {
            return chatDao.findAllChats().stream().map(Chat::name).toList();
        }
    }
}