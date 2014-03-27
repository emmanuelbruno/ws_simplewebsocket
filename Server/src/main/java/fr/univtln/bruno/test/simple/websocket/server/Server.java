package fr.univtln.bruno.test.simple.websocket.server;

import fr.univtln.bruno.test.simple.websocket.message.EchoBean;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 26/03/14.
 */

@ServerEndpoint(value = "/echo",
        encoders = {EchoBean.EchoBeanCode.class},
        decoders = {EchoBean.EchoBeanCode.class})
public class Server {
    private static List<Session> sessions = new ArrayList<>();
    public final static String SERVER_IP;
    public final static int SERVER_PORT;

    static {
        String ip = null;
        int port = 8025;
        try {
            ip = System.getProperty("fr.univtln.bruno.test.simple.websocket.server.ip");
        } catch (NullPointerException e) {
        }

        try {
            port = Integer.parseInt(System.getProperty("fr.univtln.bruno.test.simple.websocket.server.port"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {
        }
        if (ip == null) ip = "localhost";
        SERVER_IP = ip;
        SERVER_PORT = port;
        System.out.println("IP " + SERVER_IP + " PORT: " + SERVER_PORT);
    }


    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        System.out.println(session.getId() + " Client connected.");
        sessions.add(session);
    }

    @OnMessage
    public void echo(EchoBean bean, Session peer) throws IOException, EncodeException {
        bean.setReply(peer.getId() + " says " + bean.getMessage());

        for (Session session : sessions)
            session.getBasicRemote().sendObject(bean);
    }

    @OnClose
    public void onClose(final Session session, EndpointConfig endpointConfig) {
        System.out.println(session.getId() + " leaved.");
        sessions.remove(session);
    }

    public static void main(String[] args) {
        System.out.println("Server starting...");
        org.glassfish.tyrus.server.Server server =
                new org.glassfish.tyrus.server.Server(SERVER_IP, SERVER_PORT, "/", Server.class);

        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}