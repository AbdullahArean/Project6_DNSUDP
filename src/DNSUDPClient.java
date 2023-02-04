import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
public class DNSUDPClient {
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            // Continuously receive input from the user
            scanner = new Scanner(System.in);
            // Create a UDP socket
            DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Give The IP Address of The DNS Server: ->");
            // Get the server's address and port
            InetAddress address = InetAddress.getByName(scanner.nextLine());
            System.out.print("Give The Port of The DNS Server: ->");
            int port = Integer.parseInt(scanner.nextLine());


            while (true) {
                System.out.print("Enter a domain name to resolve: ");
                String data = scanner.nextLine();

                // Send the request to the server
                byte[] sendData = data.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                clientSocket.send(sendPacket);

                // Receive the response from the server
                byte[] receiveData = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                // Display the response
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Response: " + response);
            }
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

