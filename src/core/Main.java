package core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import util.AddressValidator;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Bitte IP des Zugangsservers eingeben (z.B. 127.0.0.1). Keine IP angeben um einen Zugangsserver zu starten");
        String entryServerIp = sc.nextLine();
        int entryServerPort;
        boolean startedServer = false;
        while (entryServerIp.length() > 0 && !AddressValidator.validateIP(entryServerIp)) {
            System.out.println("IP unueltig, bitte erneut eingeben");
            entryServerIp = sc.nextLine();
        }
        if (entryServerIp.length() > 0) {
            System.out.println("Bitte Port des Zugangsservers eingeben (0 fuer default)");
            entryServerPort = sc.nextInt();
            while (entryServerPort != 0 && (entryServerPort > 65535 || entryServerPort < 1024)) {
                System.out.println("Port unueltig, bitte erneut eingeben");
                entryServerPort = sc.nextInt();
            }
            if (entryServerPort == 0) {
                entryServerPort = 3333;
            }
        } else {
            System.out.println("Starte Zugangsserver...");
            entryServerIp = InetAddress.getLocalHost().getHostAddress();
            entryServerPort = 3333;
            Server server = new Server(3333);
            new Thread(server).start();
            startedServer = true;
        }
        int clientPort = startedServer ? 3334 : 3333;
        Node server = new Node(entryServerIp, entryServerPort, 0);
        Node clientNode = new Node(InetAddress.getLocalHost().getHostAddress(), clientPort, 0);
        Client client = new Client(clientNode, server);
        new Thread(client).start();
    }

}
