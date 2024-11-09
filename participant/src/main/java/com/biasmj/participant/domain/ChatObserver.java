package com.biasmj.participant.domain;

@FunctionalInterface
public interface ChatObserver {
    void update(String message);
}
