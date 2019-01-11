package io.meltcoin.p2p.actions;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.listeners.MessageListener;
import io.meltcoin.p2p.types.Middleware;
import io.meltcoin.p2p.types.MiddlewareResponse;
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
        DatagramPacket packet = new DatagramPacket(new byte[peerToPeer.packetSize], peerToPeer.packetSize);
        String receivedData;
        while (true) {
            try {
                datagramSocket.receive(packet);
                receivedData = new String(packet.getData());
                //System.out.println("Received data: " + receivedData);
                if (packet.getAddress().toString() == null) {
                    return;
                }
                Peer middlewarePeer = new Peer(peerToPeer, packet.getAddress().toString().replaceAll("/", ""), packet.getPort());
                MiddlewareResponse middlewareResponse = new MiddlewareResponse(middlewarePeer, receivedData);
                for (Middleware middleware : peerToPeer.recieveMiddlewares) {
                    middlewareResponse = middleware.onMessage(middlewareResponse);
                }
                if (middlewareResponse.getMessage().contains(peerToPeer.splitString)) {
                    String[] splitMessage = StringUtils.split(middlewareResponse.getMessage(), peerToPeer.splitString);
                    if (splitMessage.length == 2) {
                        for (MessageListener messageListener : peerToPeer.messageListeners) {
                            messageListener.onMessage(middlewareResponse.getPeer(), splitMessage[0], splitMessage[1]);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
