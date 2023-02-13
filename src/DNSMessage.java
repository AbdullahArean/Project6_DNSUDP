//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.nio.ByteBuffer;
//
//public class DNSMessage {
//
//
//    public static byte[] createDnsMessage(String domain) throws Exception {
//        ByteBuffer dnsBuffer = ByteBuffer.allocate(512);
//
//        // Write header
//        dnsBuffer.putShort((short) 0x1234); // Transaction ID
//        dnsBuffer.putShort((short) 0x0100); // Flags
//        dnsBuffer.putShort((short) 0x0001); // Number of questions
//        dnsBuffer.putShort((short) 0x0000); // Number of answers
//        dnsBuffer.putShort((short) 0x0000); // Number of authorities
//        dnsBuffer.putShort((short) 0x0000); // Number of additionals
//
//        // Write question section
//        String[] domainParts = domain.split("\\.");
//        for (String domainPart : domainParts) {
//            dnsBuffer.put((byte) domainPart.length());
//            dnsBuffer.put(domainPart.getBytes());
//        }
//        dnsBuffer.put((byte) 0x00);
//        dnsBuffer.putShort((short) 0x0001); // Type A (IPv4 address)
//        dnsBuffer.putShort((short) 0x0001); // Class IN (Internet)
//
//        return dnsBuffer.array();
//    }
//
//    public static byte[] sendDnsRequest(byte[] dnsMessage, String ipaddressoftheserver,int port) throws IOException {
//        DatagramSocket socket = new DatagramSocket();
//        InetAddress address = InetAddress.getByName("8.8.8.8");
//       DatagramPacket request = new DatagramPacket(dnsMessage, dnsMessage.length, InetAddress.getByName(ipaddressoftheserver), port);
//        //DatagramPacket request = new DatagramPacket(dnsMessage, dnsMessage.length, address, port);
//        socket.send(request);
//
//        byte[] responseBuffer = new byte[512];
//        DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
//        socket.receive(response);
//        socket.close();
//
//        return response.getData();
//    }
//
//    public static void main(String[] args) throws Exception {
//        parseDnsResponse(sendDnsRequest(createDnsMessage("www.google.com"),"8.8.8.8",53));
//    }
//
//    public static void parseDnsResponse(byte[] response) throws UnknownHostException {
//        ByteBuffer responseBuffer = ByteBuffer.wrap(response);
//
//        // Read header
//        short transactionID = responseBuffer.getShort();
//        short flags = responseBuffer.getShort();
//        short questions = responseBuffer.getShort();
//        short answers = responseBuffer.getShort();
//        short authorities = responseBuffer.getShort();
//        short additionals = responseBuffer.getShort();
//
//        System.out.println("Transaction ID: " + transactionID);
//        System.out.println("Flags: " + flags);
//        System.out.println("Questions: " + questions);
//        System.out.println("Answers: " + answers);
//        System.out.println("Authorities: " + authorities);
//        System.out.println("Additionals: " + additionals);
//
////        for (int i = 0; i < questions; i++) {
////            // Read name
////            int length = responseBuffer.get() & 0xff;
////            StringBuilder hostname = new StringBuilder();
////            while (length != 0) {
////                for (int j = 0; j < length; j++) {
////                    hostname.append((char) responseBuffer.get());
////                }
////                hostname.append(".");
////                length = responseBuffer.get() & 0xff;
////            }
////            System.out.println(hostname);
////            // Read type and class
////            short type = responseBuffer.getShort();
////            short questionClass = responseBuffer.getShort();
////            System.out.println(type + " " + questionClass);
////
////            System.out.println(responseBuffer);
////        }
//        // Decode the questions
//        for (int i = 0; i < questions; i++) {
////            StringBuilder nameBuilder = new StringBuilder();
////            int length;
////            while ((length = responseBuffer.get() & 0xFF) > 0) {
////                for (int j = 0; j < length; j++) {
////                    nameBuilder.append((char) responseBuffer.get());
////                }
////                nameBuilder.append(".");
////            }
////            String name = nameBuilder.substring(0, nameBuilder.length() - 1);
//            int length = responseBuffer.get() & 0xff;
//            StringBuilder name = new StringBuilder();
//            while (length != 0) {
//                for (int j = 0; j < length; j++) {
//                    name.append((char) responseBuffer.get());
//                }
//                name.append(".");
//                length = responseBuffer.get() & 0xff;
//            }
//            int type = responseBuffer.getShort() & 0xFFFF;
//            int qclass = responseBuffer.getShort() & 0xFFFF;
//
//            System.out.println("Question " + (i + 1));
//            System.out.println("Name: " + name);
//            System.out.println("Type: " + type);
//            System.out.println("Class: " + qclass);
//        }
//
////        // Read answers
////        for (int i = 0; i < answers; i++) {
////            // Read name
////            StringBuilder st = new StringBuilder();
////            int length = responseBuffer.get() & 0xff;
////            while (length != 0) {
////                for (int j = 0; j < length; j++) {
////                    st.append((char) responseBuffer.get());
////                }
////                st.append(".");
////                length = responseBuffer.get() & 0xff;
////            }
////            System.out.println(st);
////            short type = responseBuffer.getShort();
////            short answerClass = responseBuffer.getShort();
////            short ttl = responseBuffer.getShort();
////            System.out.println("Type: " + type + ", Class: " + answerClass + ", TTL: " + ttl);
////
////            // Read data
////            short dataLength = responseBuffer.getShort();
////            byte[] data = new byte[dataLength];
////            responseBuffer.get(data);
////
////            if (type == 1) { // A record
////                System.out.println("IPv4 address: " + InetAddress.getByAddress(data).getHostAddress());
////            } else if (type == 11) { // AAAA record
////                System.out.println("IPv6 address: " + InetAddress.getByAddress(data).getHostAddress());
////            } else if (type == 15) { // MX record
////                System.out.println("Mail exchange: " + InetAddress.getByAddress(data).getHostAddress());
////            } else {
////                System.out.println("Unknown record type");
////            }
//
//    // Decode the answers
//        for (int i = 0; i < answers; i++) {
////        StringBuilder nameBuilder = new StringBuilder();
////        int length;
////        while ((length = responseBuffer.get() & 0xFF) > 0) {
////            for (int j = 0; j < length; j++) {
////                nameBuilder.append((char) responseBuffer.get());
////            }
////            nameBuilder.append(".");
////        }
////        String name = nameBuilder.substring(0, nameBuilder.length() - 1);
////        int type = responseBuffer.getShort() & 0xFFFF;
////        int qclass = responseBuffer.getShort() & 0xFFFF;
//            int length = responseBuffer.get() & 0xff;
//            StringBuilder name = new StringBuilder();
//            while (length != 0) {
//                for (int j = 0; j < length; j++) {
//                    name.append((char) responseBuffer.get());
//                }
//                name.append(".");
//                length = responseBuffer.get() & 0xff;
//            }
//            short type = responseBuffer.getShort();
//            short answerClass = responseBuffer.getShort();
////            short ttl = responseBuffer.getShort();
////            System.out.println("Type: " + type + ", Class: " + answerClass + ", TTL: " + ttl);
////
////            int type = responseBuffer.getShort() & 0xFFFF;
////            int qclass = responseBuffer.getShort() & 0xFFFF;
//        int ttl = responseBuffer.getShort() & 0xFFFF ;
//        int rdlen = responseBuffer.getShort() & 0xFFFF;
//        byte[] data = new byte[rdlen];
//        responseBuffer.get(data);
//
//        System.out.println("Answer " + (i + 1));
//        System.out.println("Name: " + name);
//        System.out.println("Type: " + type);
//        System.out.println("Class: " + answerClass);
//        System.out.println("TTL: " + ttl);
//        System.out.println("Data Length: " + rdlen);
//        System.out.println("Data: " + new String(data));
//    }
//    }
//}