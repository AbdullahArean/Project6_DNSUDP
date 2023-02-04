import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        dnsTable.put("www.google.com", "216.58.194.174");
        dnsTable.put("www.facebook.com", "31.13.77.36");
        dnsTable.put("www.amazon.com", "52.95.245.222");
        dnsTable.put("www.apple.com", "17.142.160.59");
        try {
            String domainName = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String responseipAddress = dnsTable.get(domainName);
            if (responseipAddress == null) {
                responseipAddress = "Domain not found";
            }
            System.out.println("Request Received:["+System.currentTimeMillis()+"] "+domainName + " ->Response: "+responseipAddress);
            byte[] sendData = responseipAddress.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        } catch (Exception e) {
            System.out.println("Server Failed: " + e.getMessage());
        }
    }
}