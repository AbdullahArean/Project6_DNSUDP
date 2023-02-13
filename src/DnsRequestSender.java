//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.nio.ByteBuffer;
//
//public class DnsRequestSender {
//    public static void main(String[] args) throws Exception {
//        DatagramSocket socket = new DatagramSocket();
//        InetAddress address = InetAddress.getByName("8.8.8.8");
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//
//        // Encode the header
//        byteBuffer.putShort((short) 12345); // Transaction ID
//        byteBuffer.putShort((short) 0x0100); // Flags
//        byteBuffer.putShort((short) 1); // Question count
//        byteBuffer.putShort((short) 0); // Answer count
//        byteBuffer.putShort((short) 0); // Authority count
//        byteBuffer.putShort((short) 0); // Additional count
//
//        // Encode the question
//        String[] parts = "www.google.com".split("\\.");
//        for (String part : parts) {
//            byteBuffer.put((byte) part.length());
//            byteBuffer.put(part.getBytes());
//        }
//        byteBuffer.put((byte) 0);
//        byteBuffer.putShort((short) 1); // Type: A (IPv4 address)
//        byteBuffer.putShort((short) 1); // Class: IN (Internet)
//
//        byte[] buf = byteBuffer.array();
//        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 53);
//        socket.send(packet);
//
//        // Receive the response
//        buf = new byte[1024];
//        packet = new DatagramPacket(buf, buf.length);
//        socket.receive(packet);
//
//        // Decode and display the response
//        ByteBuffer responseBuffer = ByteBuffer.wrap(buf);
//        // ... (use the code from the previous example to decode the response)
//        // Decode the header
//        int id = responseBuffer.getShort() & 0xFFFF;
//        int flags = responseBuffer.getShort() & 0xFFFF;
//        int qdCount = responseBuffer.getShort() & 0xFFFF;
//        int anCount = responseBuffer.getShort() & 0xFFFF;
//        int nsCount = responseBuffer.getShort() & 0xFFFF;
//        int arCount = responseBuffer.getShort() & 0xFFFF;
//
//        // Display the header information
//        System.out.println("DNS Response Message Format");
//        System.out.println("Transaction ID: " + id);
//        System.out.println("Flags: " + flags);
//        System.out.println("Question Count: " + qdCount);
//        System.out.println("Answer Count: " + anCount);
//        System.out.println("Authority Count: " + nsCount);
//        System.out.println("Additional Count: " + arCount);
//
//        // Decode the questions
//        for (int i = 0; i < qdCount; i++) {
//            StringBuilder nameBuilder = new StringBuilder();
//            int length;
//            while ((length = responseBuffer.get() & 0xFF) > 0) {
//                for (int j = 0; j < length; j++) {
//                    nameBuilder.append((char) responseBuffer.get());
//                }
//                nameBuilder.append(".");
//            }
//            String name = nameBuilder.substring(0, nameBuilder.length() - 1);
//            int type = responseBuffer.getShort() & 0xFFFF;
//            int qclass = responseBuffer.getShort() & 0xFFFF;
//
//            System.out.println("Question " + (i + 1));
//            System.out.println("Name: " + name);
//            System.out.println("Type: " + type);
//            System.out.println("Class: " + qclass);
//        }
//
//        // Decode the answers
//        for (int i = 0; i < anCount; i++) {
//            StringBuilder nameBuilder = new StringBuilder();
//            int length;
//            while ((length = responseBuffer.get() & 0xFF) > 0) {
//                for (int j = 0; j < length; j++) {
//                    nameBuilder.append((char) responseBuffer.get());
//                }
//                nameBuilder.append(".");
//            }
//            String name = nameBuilder.substring(0, nameBuilder.length() - 1);
//            int type = responseBuffer.getShort() & 0xFFFF;
//            int qclass = responseBuffer.getShort() & 0xFFFF;
//            int ttl = responseBuffer.getInt();
//            int rdlen = responseBuffer.getShort() & 0xFFFF;
//            byte[] data = new byte[rdlen];
//            responseBuffer.get(data);
//            System.out.println("Answer " + (i + 1));
//            System.out.println("Name: " + name);
//            System.out.println("Type: " + type);
//            System.out.println("Class: " + qclass);
//            System.out.println("TTL: " + ttl);
//            System.out.println("Data Length: " + rdlen);
//            System.out.println("Data: " + new String(data));
//        }
//
//        socket.close();
//    }
//}
