package io.meltcoin.p2p.listeners;

import io.meltcoin.p2p.types.Peer;

public interface MessageListener {

    void onMessage(Peer peer, String type, String message);
}
