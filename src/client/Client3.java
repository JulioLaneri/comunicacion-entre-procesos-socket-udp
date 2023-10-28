package client;

import org.apache.logging.log4j.*;

import utils.ConfigLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Properties;

public class Client3 {
    private static final Logger logger = LogManager.getLogger(Client3.class.getName());

    public static void main(String[] args) {
        Properties config = ConfigLoader.loadConfig("config.properties");

        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        int client3Port = Integer.parseInt(config.getProperty("client3.port"));

        DatagramSocket socket = null;
        InetAddress serverAddress = null;
        //String stopKeyword = config.getProperty("client3.stopServer");

        try {
            socket = new DatagramSocket(client3Port);
            serverAddress = InetAddress.getByName(config.getProperty("server.address"));

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Ingrese un mensaje ('" + config.getProperty("client3.stopServer") + "' para detener): ");
                logger.info("Ingrese el mensaje: ");
                String messageToSend = userInput.readLine();


                byte[] buffer = messageToSend.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);

                socket.send(sendPacket);
                
                if (messageToSend.equalsIgnoreCase(config.getProperty("client3.stopServer"))) {
                    logger.info("Cliente 3 ha ingresado '" + config.getProperty("client3.stopServer") + "', deteniendo el cliente.");
                    break; // Salir del bucle al ingresar la palabra clave de detención.
                }

            }
        } catch (IOException e) {
            logger.error("Error al enviar o recibir mensajes: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
