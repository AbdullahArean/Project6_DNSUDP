import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DNSUDPHandler implements Runnable {
    private final DatagramSocket serverSocket;
    private final DatagramPacket receivePacket;
    private static final Map<String, String> dnsTable = new HashMap<>();

    public DNSUDPHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        // Populate the DNS table
        dnsTable.put("www.google.com", "216.58.194.174");
        dnsTable.put("www.facebook.com", "31.13.77.36");
        dnsTable.put("www.amazon.com", "52.95.245.222");
        dnsTable.put("www.apple.com", "17.142.160.59");
        try {
            // Get the domain name from the request
            String domainName = new String(receivePacket.getData(), 0, receivePacket.getLength());

            // Look up the IP address in the DNS table
            String ipAddress = dnsTable.get(domainName);
            if (ipAddress == null) {
                ipAddress = "Domain not found";
            }
            System.out.println("Request Received:["+System.currentTimeMillis()+"] "+domainName + " ->Response: "+ipAddress);
            // Send the response to the client
            byte[] sendData = ipAddress.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}