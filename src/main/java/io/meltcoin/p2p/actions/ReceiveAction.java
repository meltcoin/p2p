package io.meltcoin.p2p.actions;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Peer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveAction extends Thread {

    private PeerToPeer peerToPeer;
    private DatagramSocket datagramSocket;

    public ReceiveAction(PeerToPeer peerToPeer) {
        this.peerToPeer = peerToPeer;
        this.datagramSocket = peerToPeer.datagramSocket;
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
                if (receivedData.contains(peerToPeer.splitString)) {
                    String[] splitMessage = StringUtils.split(receivedData, peerToPeer.splitString);
                    if (splitMessage.length == 2) {
                        String address = packet.getAddress().toString().replaceAll("/", "");
                        Peer fromPeer = new Peer(peerToPeer, address, packet.getPort());
                        for (MessageListener messageListener : peerToPeer.messageListeners) {
                            messageListener.onMessage(fromPeer, splitMessage[0], splitMessage[1]);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
