package client;

import org.apache.logging.log4j.*;

import utils.ConfigLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Properties;

public class Client2 {
    private static final Logger logger = LogManager.getLogger(Client2.class.getName());

    public static void main(String[] args) {
        Properties config = ConfigLoader.loadConfig("config.properties");

        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        int client2Port = Integer.parseInt(config.getProperty("client2.port"));

        DatagramSocket socket = null;
        InetAddress serverAddress = null;
        //String stopKeyword = config.getProperty("client2.stopClient");

        try {
            socket = new DatagramSocket(client2Port);
            serverAddress = InetAddress.getByName(config.getProperty("server.address"));

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Ingrese un mensaje ('" + config.getProperty("client2.stopClient") + "' para detener): ");
                String messageToSend = userInput.readLine();

                if (messageToSend.equalsIgnoreCase(config.getProperty("client2.stopClient"))) {
                    logger.info("Cliente 2 ha ingresado '" + config.getProperty("client2.stopClient") + "', deteniendo el cliente.");
                    break; // Salir del bucle al ingresar la palabra clave de detención.
                }

                byte[] buffer = messageToSend.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);

                socket.send(sendPacket);
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
