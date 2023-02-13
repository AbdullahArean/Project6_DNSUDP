package Clients;

import Utils.DNSMessageCreateSendParse;

import java.util.Scanner;
public class DNSUDPClient {

    public static void main(String[] args) {
        try {
            Scanner inputscanner = new Scanner(System.in);
           // DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Give The IP Address of The DNS Server: ->");
            String ipaddressoftheserver = inputscanner.nextLine();
            //String ipaddressoftheserver = "localhost";
            System.out.print("Give The Port of The DNS Server: ->");
            int port = Integer.parseInt(inputscanner.nextLine());
            while (true) {
                System.out.print("Enter a domain name to resolve: (\"exit\" to stop the Client)\n[Type & Press Enter]-> ");
                String data = inputscanner.nextLine();
                if(data.equals("exit")|| data.equals("Exit")||data.equals("e")) break;
               // DNSMessageCreateSendParse.parseDnsResponse(DNSMessageCreateSendParse.sendDnsRequest(DNSMessageCreateSendParse.createDnsMessage(data), String.valueOf(ipaddressoftheserver), port));
            DNSMessageCreateSendParse.SendDnsRequest(data, ipaddressoftheserver,port);
            }
        } catch (Exception e) {
            System.out.println("Client Failed: " );
                    e.printStackTrace();
        }
    }



}

