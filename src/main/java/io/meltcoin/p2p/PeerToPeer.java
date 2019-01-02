package io.meltcoin.p2p;

import io.meltcoin.p2p.actions.ReceiveAction;
import io.meltcoin.p2p.actions.SendAction;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class PeerToPeer {

    public Integer listenPort;
    public String splitString;
    public Integer packetSize;

    public ArrayList<MessageListener> messageListeners;
    public DatagramSocket datagramSocket = null;

    public ReceiveAction receiveAction = null;
    public SendAction sendAction = null;

    public PeerToPeer(Integer listenPort) {
        this(listenPort, "@#$&");
    }

    public PeerToPeer(Integer listenPort, String splitString) {
        this(listenPort, splitString, 1024);
    }

    public PeerToPeer(Integer listenPort, String splitString, Integer packetSize) {
        if (splitString == null) {
            splitString = "@#$&";
        }
        this.listenPort = listenPort;
        this.splitString = splitString;
        this.packetSize = packetSize;
        this.messageListeners = new ArrayList<>();
    }

    public void start() {
        try {
            this.datagramSocket = new DatagramSocket(listenPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        receiveAction = new ReceiveAction(this);
        receiveAction.start();

        sendAction = new SendAction(this);
    }

    public void registerMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }
}
