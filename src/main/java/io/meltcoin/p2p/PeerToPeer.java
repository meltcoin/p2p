package io.meltcoin.p2p;

import io.meltcoin.p2p.actions.ReceiveAction;
import io.meltcoin.p2p.actions.SendAction;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;

import java.util.ArrayList;

public class PeerToPeer {

    public Integer port;
    public ArrayList<MessageListener> messageListeners;

    public ReceiveAction receiveAction = null;
    public SendAction sendAction = null;

    public PeerToPeer(Integer port) {
        this.port = port;
        this.messageListeners = new ArrayList<>();
    }

    public void start() {
        receiveAction = new ReceiveAction(this, this.port);
        receiveAction.start();

        sendAction = new SendAction(this);
    }

    public void registerMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }
}
