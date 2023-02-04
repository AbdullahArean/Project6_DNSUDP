import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
public class DNSUDPClient {

    public static void main(String[] args) {
        try {
            Scanner inputscanner = new Scanner(System.in);
            DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Give The IP Address of The DNS Server: ->");
            InetAddress ipaddressoftheserver = InetAddress.getByName(inputscanner.nextLine());
            System.out.print("Give The Port of The DNS Server: ->");
            int port = Integer.parseInt(inputscanner.nextLine());
            while (true) {
                System.out.print("Enter a domain name to resolve: (\"exit\" to stop the Client)\n[Type & Press Enter]-> ");
                String data = inputscanner.nextLine();
                if(data.equals("exit")|| data.equals("Exit")||data.equals("e")) break;
                byte[] sendData = data.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddressoftheserver, port);
                clientSocket.send(sendPacket);
                byte[] receiveData = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Client Failed: " + e.getMessage());
        }
    }
}

