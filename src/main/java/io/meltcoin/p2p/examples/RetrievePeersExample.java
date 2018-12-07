package io.meltcoin.p2p.examples;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;

import java.util.HashMap;

public class RetrievePeersExample implements MessageListener {

    public static PeerToPeer peerToPeer = null;
    public static HashMap<String, Peer> peers = new HashMap<>();

    public static void main(String[] args) {
        peerToPeer = new PeerToPeer(8342);
        peerToPeer.start();
        peerToPeer.registerMessageListener(new RetrievePeersExample());

        Peer peer = new Peer(peerToPeer, "127.0.0.1", 8342);
        peers.put("127.0.0.1:8342", peer);
        System.out.println("Sending peer request message...");
        peer.sendMessage(new Message("requestpeers", ""));
        System.out.println("Sending test message...");
        peer.sendMessage(new Message("test", "Hello world!"));
    }

    @Override
    public void onMessage(Peer peer, String type, String message) {
        if (type.equalsIgnoreCase("requestpeers")) {
            System.out.println("Request for peers received.");
            for (Peer peer1 : peers.values()) {
                peer.sendMessage(new Message("addpeer", peer1.getHost() + ":" + peer1.getPort()));
            }
        } else if (type.equalsIgnoreCase("addpeer")) {
            System.out.println("Add peer received from " + peer.getHost() + ":" + peer.getPort());
            // Add peer that sent message
            if (!peers.containsKey(peer.getHost() + ":" + peer.getPort())) {
                peers.put(peer.getHost() + ":" + peer.getPort(), peer);
                System.out.println("Added the peer " + peer.getHost() + ":" + peer.getPort());
            }
            // Add specified peer
            if (message.contains(":")) {
                String[] splitMessage = message.split(":");
                if (splitMessage.length == 2) {
                    if (!peers.containsKey(splitMessage[0] + ":" + splitMessage[1])) {
                        peers.put(splitMessage[0] + ":" + splitMessage[1], new Peer(peerToPeer, splitMessage[0], Integer.getInteger(splitMessage[1])));
                        System.out.println("Added the peer " + splitMessage[0] + ":" + splitMessage[1]);
                    }
                }
            }
        } else if (type.equalsIgnoreCase("test")) {
            System.out.println("Test message received from " + peer.getHost() + ":" + peer.getPort());
            System.out.println(message);
        }
    }
}
