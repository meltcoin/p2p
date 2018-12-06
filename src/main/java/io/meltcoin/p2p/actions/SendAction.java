package io.meltcoin.p2p.actions;

import io.meltcoin.p2p.PeerToPeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendAction {

    private PeerToPeer peerToPeer;
    private DatagramSocket datagramSocket = null;

    public SendAction(PeerToPeer peerToPeer) {
        this.peerToPeer = peerToPeer;
        try {
            this.datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String type, String message, String host, Integer port) {
        // Generate message to send
        byte finalMessage[] = (type + ":" + message).getBytes();
        try {
            // Send packet after creation
            DatagramPacket packet = new DatagramPacket(finalMessage, finalMessage.length, InetAddress.getByName(host), port);
            //System.out.println("Sending data: " + new String(packet.getData()));
            datagramSocket.send(packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
