package io.meltcoin.p2p.actions;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveAction extends Thread {

    private PeerToPeer peerToPeer;
    private DatagramSocket datagramSocket;

    public ReceiveAction(PeerToPeer peerToPeer, Integer listenPort) {
        this.peerToPeer = peerToPeer;
        try {
            this.datagramSocket = new DatagramSocket(listenPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        String receivedData;
        while (true) {
            try {
                datagramSocket.receive(packet);
                receivedData = new String(packet.getData());
                //System.out.println("Received data: " + receivedData);
                if (receivedData.contains(":")) {
                    String[] splitMessage = receivedData.split(":");
                    if (splitMessage.length == 2) {
                        for (MessageListener messageListener : peerToPeer.messageListeners) {
                            messageListener.onMessage(splitMessage[0], splitMessage[1]);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
