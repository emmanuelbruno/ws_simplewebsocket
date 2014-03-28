package fr.univtln.bruno.test.simple.websocket.client;


import fr.univtln.bruno.test.simple.personne.Personne;
import fr.univtln.bruno.test.simple.websocket.message.PayloadBean;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by bruno on 26/03/14.
 */

@ClientEndpoint(encoders = {PayloadBean.PayloadBeanCode.class},
        decoders = {PayloadBean.PayloadBeanCode.class})
public class Client {
    public final static String SERVER_IP;
    public final static int SERVER_PORT;
    private Personne sender;

    public Client(Personne sender) {
        this.sender = sender;
    }

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
        System.out.println("Server IP:" + SERVER_IP + " Port: " + SERVER_PORT);
    }

    private Session session;

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) throws IOException, EncodeException {
        this.session = session;
        System.out.println("I am " + session.getId());
        System.out.println("Sending Hello message to server");
        session.getBasicRemote().sendObject(new PayloadBean(new Date(),sender,"Hello"));
    }

    @OnMessage
    public void OnMessage(PayloadBean bean) {
        System.out.println("RECU !");
        System.out.println(bean.getDate() + " (" //+ bean.getSender()
                + ") " + bean.getMessage());
    }

    @OnClose
    public void OnClose(final Session session, EndpointConfig endpointConfig) {
        System.out.println("Session closed");
    }

    public void closeSession() throws IOException {
        if (session.isOpen())
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "OK"));
    }

    public void sendMessage(String message) {
        PayloadBean bean = new PayloadBean(new Date(), sender, message);
        /*        System.out.println("The PayloadBean toString(): "+bean);

        //To print the JSON encoded version
        try {
            StringWriter sw = new StringWriter();
            new PayloadBean.PayloadBeanCode().encode(bean, sw);
            System.out.println("The JSON from the Payload: "+sw.toString());
        } catch (EncodeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          */
        try {
            session.getBasicRemote().sendObject(bean);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client beanClient = new Client(new Personne(1, "John", "Doe"));
        try {
            final ClientManager client = ClientManager.createClient();
            client.connectToServer(
                    beanClient
                    , ClientEndpointConfig.Builder.create()
                            .encoders(Arrays.<Class<? extends Encoder>>asList(PayloadBean.PayloadBeanCode.class))
                            .decoders(Arrays.<Class<? extends Decoder>>asList(PayloadBean.PayloadBeanCode.class))
                            .build(),
                    URI.create("ws://" + SERVER_IP + ":" + SERVER_PORT + "/echo")
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Send empty line to stop the client.");
            String line;
            do {
                line = reader.readLine();
                if (!"".equals(line))
                    beanClient.sendMessage(line);
            } while (!"".equals(line));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                beanClient.closeSession();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}