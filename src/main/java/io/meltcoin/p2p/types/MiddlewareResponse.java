package io.meltcoin.p2p.types;

public class MiddlewareResponse {

    Peer peer;
    String message;

    public MiddlewareResponse(Peer peer, String message) {
        this.peer = peer;
        this.message = message;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
