import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class DNSUDPServer {
    private static Map<String, String> dnsTable = new HashMap<>();

    public static void main(String[] args) {
        try {
            // Create a UDP socket
            DatagramSocket serverSocket = new DatagramSocket(12345);

            // Populate the DNS table
            dnsTable.put("www.google.com", "216.58.194.174");
            dnsTable.put("www.facebook.com", "31.13.77.36");
            dnsTable.put("www.amazon.com", "52.95.245.222");
            dnsTable.put("www.apple.com", "17.142.160.59");

            // Continuously receive requests from clients
            while (true) {
                // Receive the request from the client
                byte[] receiveData = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Start a new thread to handle the request
                new Thread(new ClientHandler(serverSocket, receivePacket)).start();
            }
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private DatagramSocket serverSocket;
        private DatagramPacket receivePacket;

        public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
        }

        @Override
        public void run() {
            try {
                // Get the domain name from the request
                String domainName = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Look up the IP address in the DNS table
                String ipAddress = dnsTable.get(domainName);
                if (ipAddress == null) {
                    ipAddress = "Domain not found";
                }

                // Send the response to the client
                byte[] sendData = ipAddress.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
