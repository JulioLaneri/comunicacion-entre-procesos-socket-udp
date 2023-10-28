package client;

import org.apache.logging.log4j.*;

import utils.ConfigLoader;


import java.io.IOException;
import java.net.*;
import java.util.Properties;

import java.util.Scanner;;

public class Client1 {
    
    private static final Logger logger = LogManager.getLogger(Client1.class.getName());
    public static void main(String[] args) throws IOException {
        
        Scanner input = new Scanner(System.in);
       
        Properties config = ConfigLoader.loadConfig("config.properties");

        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        int client1Port = Integer.parseInt(config.getProperty("client1.port"));

        DatagramSocket socket = new DatagramSocket(client1Port);
        InetAddress serverAddress = InetAddress.getByName(config.getProperty("server.address"));

        String mesnsaje = input.nextLine();
        byte[] buffer = mesnsaje.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);

        socket.send(sendPacket);

        byte[] bf = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(bf, bf.length);
        
        // Establecer un temporizador para esperar la respuesta (configurable)
        socket.setSoTimeout(Integer.parseInt(config.getProperty("client1.timeout")));

        try {
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            logger.info("Cliente 1 recibió la respuesta: " + receivedMessage);
            System.out.println(receivedMessage);
        } catch (SocketTimeoutException e) {
           logger.error("El cliente 1 no recibió una respuesta a tiempo.");
           logger.error(e.getMessage());
        }

        socket.close();
        input.close();
    }
}
