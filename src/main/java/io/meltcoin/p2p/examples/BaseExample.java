package io.meltcoin.p2p.examples;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;

public class BaseExample implements MessageListener {

    public static void main(String[] args) {
        PeerToPeer peerToPeer = new PeerToPeer(8342);
        peerToPeer.start();
        peerToPeer.registerMessageListener(new BaseExample());

        Peer peer = new Peer(peerToPeer, "127.0.0.1", 8342);
        System.out.println("Sending test message...");
        peer.sendMessage(new Message("test", "Hello world!"));
    }

    @Override
    public void onMessage(Peer peer, String type, String message) {
        if (!type.equalsIgnoreCase("test")) return;
        System.out.println("Test message received!");
        System.out.println(message);
    }
}
