import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;
public class DNSUDPClient {

    public static void main(String[] args) {
        try {
            Scanner inputscanner = new Scanner(System.in);
           // DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Give The IP Address of The DNS Server: ->");
            //InetAddress ipaddressoftheserver = InetAddress.getByName(inputscanner.nextLine());
            String ipaddressoftheserver = "localhost";
            System.out.print("Give The Port of The DNS Server: ->");
            //int port = Integer.parseInt(inputscanner.nextLine());
            int port = 8182;
            while (true) {
                System.out.print("Enter a domain name to resolve: (\"exit\" to stop the Client)\n[Type & Press Enter]-> ");
                String data = inputscanner.nextLine();
                if(data.equals("exit")|| data.equals("Exit")||data.equals("e")) break;
                DNSMessage.parseDnsResponse(DNSMessage.sendDnsRequest(DNSMessage.createDnsMessage(data), ipaddressoftheserver, port));
                //DNSMessage.parseDnsResponse(DNSMessage.createDnsMessage(data));
            }
        } catch (Exception e) {
            System.out.println("Client Failed: " );
                    e.printStackTrace();
        }
    }



}

