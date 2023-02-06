import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;
public class DNSUDPClient {
    private static String ipaddressoftheserver;
    private static int port;

    public static void main(String[] args) {
        try {
            Scanner inputscanner = new Scanner(System.in);
           // DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Give The IP Address of The DNS Server: ->");
            //InetAddress ipaddressoftheserver = InetAddress.getByName(inputscanner.nextLine());
            ipaddressoftheserver = "localhost";
            System.out.print("Give The Port of The DNS Server: ->");
            //int port = Integer.parseInt(inputscanner.nextLine());
            port = 8181;
            while (true) {
                System.out.print("Enter a domain name to resolve: (\"exit\" to stop the Client)\n[Type & Press Enter]-> ");
                String data = inputscanner.nextLine();
                if(data.equals("exit")|| data.equals("Exit")||data.equals("e")) break;

//                byte[] sendData = createDnsMessage(data);
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipaddressoftheserver), port);
//                clientSocket.send(sendPacket);
//                byte[] receiveData = new byte[4096];
//                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                clientSocket.receive(receivePacket);
//                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                parseDnsResponse(sendDnsRequest(createDnsMessage(data)));
               // System.out.println("Response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Client Failed: " + e.getMessage());
        }
    }
    private static byte[] createDnsMessage(String domain) throws Exception {
        ByteBuffer dnsBuffer = ByteBuffer.allocate(512);

        // Write header
        dnsBuffer.putShort((short) 0x1234); // Transaction ID
        dnsBuffer.putShort((short) 0x0100); // Flags
        dnsBuffer.putShort((short) 0x0001); // Number of questions
        dnsBuffer.putShort((short) 0x0000); // Number of answers
        dnsBuffer.putShort((short) 0x0000); // Number of authorities
        dnsBuffer.putShort((short) 0x0000); // Number of additionals

        // Write question section
        String[] domainParts = domain.split("\\.");
        for (String domainPart : domainParts) {
            dnsBuffer.put((byte) domainPart.length());
            dnsBuffer.put(domainPart.getBytes());
        }
        dnsBuffer.put((byte) 0x00);
        dnsBuffer.putShort((short) 0x0001); // Type A (IPv4 address)
        dnsBuffer.putShort((short) 0x0001); // Class IN (Internet)

        return dnsBuffer.array();
    }

    private static byte[] sendDnsRequest(byte[] dnsMessage) throws IOException {
        DatagramSocket socket = new DatagramSocket();
       // InetAddress address = InetAddress.getByName("8.8.8.8");
        DatagramPacket request = new DatagramPacket(dnsMessage, dnsMessage.length, InetAddress.getByName(ipaddressoftheserver), port);
        socket.send(request);

        byte[] responseBuffer = new byte[512];
        DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
        socket.receive(response);
        socket.close();

        return response.getData();
    }
    private static void parseDnsResponse(byte[] response) throws UnknownHostException {
        ByteBuffer responseBuffer = ByteBuffer.wrap(response);

        // Read header
        short transactionID = responseBuffer.getShort();
        short flags = responseBuffer.getShort();
        short questions = responseBuffer.getShort();
        short answers = responseBuffer.getShort();
        short authorities = responseBuffer.getShort();
        short additionals = responseBuffer.getShort();

        System.out.println("Transaction ID: " + transactionID);
        System.out.println("Flags: " + flags);
        System.out.println("Questions: " + questions);
        System.out.println("Answers: " + answers);
        System.out.println("Authorities: " + authorities);
        System.out.println("Additionals: " + additionals);

        // Read answers
        for (int i = 0; i < answers; i++) {
            // Read name
            StringBuilder st = new StringBuilder();
            int length = responseBuffer.get() & 0xff;
            while (length != 0) {
                for (int j = 0; j < length; j++) {
                    st.append((char) responseBuffer.get());
                }
                st.append(".");
                length = responseBuffer.get() & 0xff;
            }
            System.out.println(st);

            // Read type, class, and ttl
            //responseBuffer.getShort();
            //responseBuffer.getShort();
            short type = responseBuffer.getShort();
            short answerClass = responseBuffer.getShort();
            short ttl = responseBuffer.getShort();
            System.out.println("Type: " + type + ", Class: " + answerClass + ", TTL: " + ttl);

            // Read data
            short dataLength = responseBuffer.getShort();
            byte[] data = new byte[dataLength];
            responseBuffer.get(data);

            if (type == 1) { // A record
                System.out.println("IPv4 address: " + InetAddress.getByAddress(data).getHostAddress());
            } else if (type == 11) { // AAAA record
                System.out.println("IPv6 address: " + InetAddress.getByAddress(data).getHostAddress());
            } else if (type == 15) { // MX record
                System.out.println("Mail exchange: " + InetAddress.getByAddress(data).getHostAddress());
            } else {
                System.out.println("Unknown record type");
            }
        }
    }


}

