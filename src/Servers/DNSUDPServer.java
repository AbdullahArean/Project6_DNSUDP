package Servers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

public class DNSUDPServer {

    public static void main(String[] args) {
        try {
            //Select a Random Port Number
            int port = ThreadLocalRandom.current().nextInt(1000, 9999);
            DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("DNS Resolution Server started....\nServer IP: " + getIP() + " Server Port: " + port);
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

    private static String getIP() {
        String ip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
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


    public static class DNSUDPHandler implements Runnable {
        private final DatagramSocket serverSocket;
        private final DatagramPacket receivePacket;
        //private static DnsRecord[] localstorage;


        public DNSUDPHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
        }

        @Override
        public void run() {
            try {
//                String domainName = new String(receivePacket.getData(), 0, receivePacket.getLength());
//                System.out.println("Request Received:[" + System.currentTimeMillis() + "] " + domainName);
                byte[] sendData = handleDNSRequest(receivePacket.getData());
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } catch (Exception e) {
                System.out.println("Server Failed: (handler) " + e.getMessage());
            }
        }

        public static byte[] handleDNSRequest(byte[] request) throws Exception {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(request));
            System.out.println("\n\nStart response decode");
            System.out.println("Transaction ID: " + dataInputStream.readShort()); // ID
            short flags = dataInputStream.readByte();
            int QR = (flags & 0b10000000) >>> 7;
            int opCode = (flags & 0b01111000) >>> 3;
            int AA = (flags & 0b00000100) >>> 2;
            int TC = (flags & 0b00000010) >>> 1;
            int RD = flags & 0b00000001;
            System.out.println("QR " + QR);
            System.out.println("Opcode " + opCode);
            System.out.println("AA " + AA);
            System.out.println("TC " + TC);
            System.out.println("RD " + RD);
            flags = dataInputStream.readByte();
            int RA = (flags & 0b10000000) >>> 7;
            int Z = (flags & 0b01110000) >>> 4;
            int RCODE = flags & 0b00001111;
            System.out.println("RA " + RA);
            System.out.println("Z " + Z);
            System.out.println("RCODE " + RCODE);

            short QDCOUNT = dataInputStream.readShort();
            short ANCOUNT = dataInputStream.readShort();
            short NSCOUNT = dataInputStream.readShort();
            short ARCOUNT = dataInputStream.readShort();

            System.out.println("Questions: " + String.format("%s", QDCOUNT));
            System.out.println("Answers RRs: " + String.format("%s", ANCOUNT));
            System.out.println("Authority RRs: " + String.format("%s", NSCOUNT));
            System.out.println("Additional RRs: " + String.format("%s", ARCOUNT));

            String QNAME = "";
            int recLen;
            byte[] record = new byte[0];
            while ((recLen = dataInputStream.readByte()) > 0) {
                record = new byte[recLen];
                for (int i = 0; i < recLen; i++) {
                    record[i] = dataInputStream.readByte();
                }
                QNAME = new String(record, StandardCharsets.UTF_8);
            }
            StringBuilder ip = new StringBuilder();
            StringBuilder domainSb = new StringBuilder();
            for(byte ipPart:record) {
                ip.append(ipPart).append(".");
            }
            short QTYPE = dataInputStream.readShort();
            short QCLASS = dataInputStream.readShort();
            System.out.println("Record: " + QNAME);
            System.out.println("Record Type: " + String.format("%s", QTYPE));
            System.out.println("Class: " + String.format("%s", QCLASS));


//            ByteBuffer requestBuffer = ByteBuffer.wrap(request);
//            short transactionID = requestBuffer.getShort();
//            short flags = requestBuffer.get();
//            short questions = 1;
//            short answers = requestBuffer.getShort();
//            short authorities = requestBuffer.getShort();
//            short additionals = requestBuffer.getShort();
//
//
//            ByteBuffer responseBuffer = null;
//            for (int i = 0; i < questions; i++) {
//                StringBuilder hostname = new StringBuilder();
//                int length = requestBuffer.getInt() & 0xff;
//                while (length != 0) {
//                    for (int j = 0; j < length; j++) {
//                        hostname.append(requestBuffer.getChar());
//                    }
//                    hostname.append(".");
//                    length = requestBuffer.getInt() & 0xff;
//                }
//                System.out.println(hostname+"hohoo");
//                System.out.println("adfaldfasl");
//                short type = requestBuffer.getShort();
//                short questionClass = requestBuffer.getShort();
//
//                responseBuffer = ByteBuffer.allocate(512);
//                responseBuffer.putShort(transactionID);
//                responseBuffer.putShort((short) 0x8180);
//                responseBuffer.putShort((short) 1);
//                responseBuffer.putShort((short) 1);
//                responseBuffer.putShort((short) 0);
//                responseBuffer.putShort((short) 0);
//
//                String[] domainParts = hostname.toString().split("\\.");
//                for (String domainPart : domainParts) {
//                    responseBuffer.put((byte) domainPart.length());
//                    responseBuffer.put(domainPart.getBytes());
//                }
//
//                responseBuffer.put((byte) 0x00);
//                responseBuffer.putShort(type);
//                responseBuffer.putShort(questionClass);
//                responseBuffer.putShort((short) 60);
//                responseBuffer.putShort((short) 4);
//                responseBuffer.put(InetAddress.getByName("192.12.12.12").getAddress());
//
//
//            }
//            return responseBuffer.array();
//        }
            return request;
        }
    }
}
