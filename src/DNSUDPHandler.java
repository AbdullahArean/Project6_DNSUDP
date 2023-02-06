
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DNSUDPHandler implements Runnable {
    private final DatagramSocket serverSocket;
    private final DatagramPacket receivePacket;
    private static DnsRecord[] localstorage;


    public DNSUDPHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }
    @Override
    public void run() {
        try {
            String domainName = new String(receivePacket.getData(), 0, receivePacket.getLength());
            localstorage = DnsRecord.readRecordsFromFile("dns_records_auth.txt");
            System.out.println("Request Received:["+System.currentTimeMillis()+"] "+domainName);
            byte[] sendData = handleDnsRequest(domainName.getBytes());
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        } catch (Exception e) {
            System.out.println("Server Failed: (handler) " + e.getMessage());
        }
    }
    private static byte[] handleDnsRequest(byte[] request) throws Exception {
    // Parse the DNS request

    ByteBuffer requestBuffer = ByteBuffer.wrap(request);
    short transactionID = requestBuffer.getShort();
    short flags = requestBuffer.getShort();
    short questions = requestBuffer.getShort();
    short answers = requestBuffer.getShort();
    short authorities = requestBuffer.getShort();
    short additionals = requestBuffer.getShort();

    // Read questions
    for (int i = 0; i < questions; i++) {
        // Read name
        int length = requestBuffer.get() & 0xff;
        StringBuilder hostname = new StringBuilder();
        while (length != 0) {
            for (int j = 0; j < length; j++) {
                hostname.append((char) requestBuffer.get());
            }
            hostname.append(".");
            length = requestBuffer.get() & 0xff;
        }
        System.out.println(hostname);
        short type_st = 1;
        short ttl_st = 60;
        String ipaddress_st = "192.12.12.12";
//        for (int index = 0; index < localstorage.length; i++) {
//            if (hostname.equals(localstorage[index].getName())) {
//                ipaddress_st = localstorage[index].getValue();
//                type_st = localstorage[index].getType();
//                ttl_st = localstorage[index].getTtl();
//                System.out.println("here");
//            }
//        }

        // Read type and class
        short type = requestBuffer.getShort();
        short questionClass = requestBuffer.getShort();
        System.out.println(type + " " + questionClass);


        // Build the DNS response
        ByteBuffer responseBuffer = ByteBuffer.allocate(512);
        responseBuffer.putShort(transactionID);
        responseBuffer.putShort((short) 0x8180); // Standard query response, No error
        responseBuffer.putShort((short) 1); // 1 question
        responseBuffer.putShort((short) 1); // 1 answer
        responseBuffer.putShort((short) 0); // 0 authorities
        responseBuffer.putShort((short) 0); // 0 additionals
        System.out.println(responseBuffer);

        // Write question section
        String[] domainParts = hostname.toString().split("\\.");
        for (String domainPart : domainParts) {
            responseBuffer.put((byte) domainPart.length());
            responseBuffer.put(domainPart.getBytes());
        }
        responseBuffer.put((byte) 0x00);
        responseBuffer.putShort(type_st);responseBuffer.putShort(type_st);responseBuffer.putShort(type_st);


        responseBuffer.putShort(type_st); // Type A record
        responseBuffer.putShort(questionClass); // Class IN
        responseBuffer.putShort(ttl_st); // TTL 60 seconds

        responseBuffer.putShort((short) 4); // Data length 4 bytes
        responseBuffer.put(InetAddress.getByName(ipaddress_st).getAddress()); // IP address

        System.out.println(responseBuffer);

        return responseBuffer.array();
    }

    return null;
}
}