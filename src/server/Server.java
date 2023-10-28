package server;

import org.apache.logging.log4j.*;

import utils.ConfigLoader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    

    public static void main(String[] args) throws IOException {
    	logger.info("Holaaaaa");
        Properties config = ConfigLoader.loadConfig("config.properties");

        int port = Integer.parseInt(config.getProperty("server.port"));
        DatagramSocket socket = new DatagramSocket(port);

        logger.info("Servidor iniciado en el puerto " + port);
        System.out.println("Escuchando...");

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);
            
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            
            // Procesar el mensaje recibido
            if (receivedMessage.equalsIgnoreCase(config.getProperty("client3.stopServer"))) {
                logger.info("Servidor recibió la palabra clave para cerrarse. Cerrando el servidor.");
                socket.close();
                break;
            } else {
                String responseMessage = "Se recibió el mensaje: " + receivedMessage;
                
                System.out.println(responseMessage);
        
                byte[] bf = responseMessage.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(bf, bf.length,
                        receivePacket.getAddress(), receivePacket.getPort());
        
                socket.send(responsePacket);
            }
        }
        
        
    }
}
