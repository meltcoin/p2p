package io.meltcoin.p2p.actions;

import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.types.Middleware;
import io.meltcoin.p2p.types.MiddlewareResponse;
import io.meltcoin.p2p.types.Peer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendAction {

    private PeerToPeer peerToPeer;
    private DatagramSocket datagramSocket;

    public SendAction(PeerToPeer peerToPeer) {
        this.peerToPeer = peerToPeer;
        this.datagramSocket = peerToPeer.datagramSocket;
    }

    public boolean sendMessage(String type, String message, String host, Integer port) {
        // Run middleware
        MiddlewareResponse middlewareResponse = new MiddlewareResponse(new Peer(peerToPeer, host, port), type + peerToPeer.splitString + message);
        for (Middleware middleware : peerToPeer.sendMiddlewares) {
            middlewareResponse = middleware.onMessage(middlewareResponse);
        }
        // Generate message to send
        byte finalMessage[] = (middlewareResponse).getMessage().getBytes();
        try {
            // Send packet after creation
            DatagramPacket packet = new DatagramPacket(finalMessage, finalMessage.length, InetAddress.getByName(host), port);
            //System.out.println("Sending data: " + new String(packet.getData()));
            datagramSocket.send(packet);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean sendMessageDebug(String type, String message, String host, Integer port) {
        // Run middleware
        MiddlewareResponse middlewareResponse = new MiddlewareResponse(new Peer(peerToPeer, host, port), type + peerToPeer.splitString + message);
        for (Middleware middleware : peerToPeer.sendMiddlewares) {
            middlewareResponse = middleware.onMessage(middlewareResponse);
        }
        // Generate message to send
        byte finalMessage[] = (middlewareResponse).getMessage().getBytes();
        try {
            // Send packet after creation
            DatagramPacket packet = new DatagramPacket(finalMessage, finalMessage.length, InetAddress.getByName(host), port);
            System.out.println("Sending data: " + new String(packet.getData()));
            datagramSocket.send(packet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
