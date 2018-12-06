package io.meltcoin.p2p.listeners;

public interface MessageListener {

    void onMessage(String type, String message);
}
