package com.biasmj.manager.domain;

@FunctionalInterface
public interface ChatObserver {
    void update(String message);
}
