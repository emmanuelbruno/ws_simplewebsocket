package fr.univtln.bruno.test.simple.websocket.message;

import fr.univtln.bruno.test.simple.jsoncoder.JSONCoder;

/**
 * Created by bruno on 26/03/14.
 */
public class PayloadBean {
    public static class PayloadBeanCode extends
            JSONCoder<PayloadBean> {
    }

    private String message;

    public PayloadBean(String message) {
        super();
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}