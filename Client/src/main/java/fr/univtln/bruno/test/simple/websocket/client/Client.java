package fr.univtln.bruno.test.simple.websocket.client;


import fr.univtln.bruno.test.simple.websocket.message.EchoBean;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by bruno on 26/03/14.
 */

@ClientEndpoint(encoders = {EchoBean.EchoBeanCode.class},
        decoders = {EchoBean.EchoBeanCode.class})
public class Client {
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

    private Session session;

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) throws IOException, EncodeException {
        this.session = session;
        System.out.println("I am " + session.getId());
        System.out.println("Sending Hello message to server");
        session.getBasicRemote().sendObject(new EchoBean("Hello"));
    }

    @OnMessage
    public void OnMessage(EchoBean bean) {
        System.out.println("Received: " + bean.getReply());
    }

    @OnClose
    public void OnClose(final Session session, EndpointConfig endpointConfig) {
        System.out.println("Server leaved");
    }

    public void closeSession() {
        try {
            if (session.isOpen())
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "All fine"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        EchoBean bean = new EchoBean(message);
        try {
            session.getBasicRemote().sendObject(bean);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client beanClient = new Client();
        try {
            final ClientManager client = ClientManager.createClient();
            client.connectToServer(
                    beanClient
                    , ClientEndpointConfig.Builder.create()
                            .encoders(Arrays.<Class<? extends Encoder>>asList(EchoBean.EchoBeanCode.class))
                            .decoders(Arrays.<Class<? extends Decoder>>asList(EchoBean.EchoBeanCode.class))
                            .build(),
                    URI.create("ws://" + SERVER_IP + ":" + SERVER_PORT + "/echo")
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Empty line to stop the client.");
            String line;
            do {
                line = reader.readLine();
                if (!"".equals(line))
                    beanClient.sendMessage(line);
            } while (!"".equals(line));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            beanClient.closeSession();
        }
    }
}