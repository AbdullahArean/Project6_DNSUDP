package Servers;

import java.net.*;
import java.util.Enumeration;

public class DNSUDPServer {

    public static void main(String[] args) {
        try {
            //Select a Random Port Number
            //int port = ThreadLocalRandom.current().nextInt(1000, 9999);
            int port = 8182;
            DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("DNS Resolution Server started....\nServer IP: "+getIP()+ " Server Port: "+port);
            System.out.println("Waiting for Continuously receive requests from clients ...");

            while (true) {
                byte[] receiveData = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                new Thread(new DNSUDPHandler(serverSocket, receivePacket)).start();
            }
        } catch (Exception e) {
            System.out.println("Server Failed : " + e.getMessage());
        }
    }
    private static String getIP(){
        String ip=null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) continue;
                    ip = addr.getHostAddress();
                }
            }
        } catch (Exception e) {
            System.out.println("Server Failed: " + e.getMessage());
        }
        return ip;
    }




}
