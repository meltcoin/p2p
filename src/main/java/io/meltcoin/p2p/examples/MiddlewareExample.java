package io.meltcoin.p2p.examples;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Middleware;
import io.meltcoin.p2p.types.MiddlewareResponse;
import io.meltcoin.p2p.types.Peer;

import java.util.Base64;

public class MiddlewareExample implements MessageListener {

    public static void main(String[] args) {
        PeerToPeer peerToPeer = new PeerToPeer(8342);
        peerToPeer.start();

        peerToPeer.sendMiddlewares.add(middlewareResponse -> {
            middlewareResponse.setMessage(Base64.getEncoder().encodeToString(middlewareResponse.getMessage().getBytes()));
            return middlewareResponse;
        });

        peerToPeer.sendMiddlewares.add(middlewareResponse -> {
            middlewareResponse.setMessage(new String(Base64.getDecoder().decode(middlewareResponse.getMessage().getBytes())));
            return middlewareResponse;
        });

        peerToPeer.registerMessageListener(new MiddlewareExample());

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
