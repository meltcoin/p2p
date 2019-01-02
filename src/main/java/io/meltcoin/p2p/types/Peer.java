package io.meltcoin.p2p.types;

import io.meltcoin.p2p.PeerToPeer;

public class Peer {

    private PeerToPeer peerToPeer;
    private String host;
    private Integer port;

    public Peer(PeerToPeer peerToPeer, String host, Integer port) {
        this.peerToPeer = peerToPeer;
        this.host = host;
        this.port = port;
    }

    public void sendMessage(Message message) {
        peerToPeer.sendAction.sendMessage(message.getType(), message.getMessage(), host, port);
    }

    public void sendMessageDebug(Message message) {
        peerToPeer.sendAction.sendMessageDebug(message.getType(), message.getMessage(), host, port);
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
