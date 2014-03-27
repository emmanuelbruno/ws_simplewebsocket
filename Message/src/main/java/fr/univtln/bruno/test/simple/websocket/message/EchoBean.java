package fr.univtln.bruno.test.simple.websocket.message;

import fr.univtln.bruno.test.simple.jsoncoder.JSONCoder;

/**
 * Created by bruno on 26/03/14.
 */
public class EchoBean {
    public static class EchoBeanCode extends
            JSONCoder<EchoBean> {
    }

    private String _message;
    private String _reply;

    public EchoBean() {
    }

    public EchoBean(String _message) {
        super();
        this._message = _message;
    }

    public void setMessage(String _message) {
        this._message = _message;
    }

    public String getMessage() {
        return _message;
    }

    public void setReply(String _reply) {
        this._reply = _reply;
    }

    public String getReply() {
        return _reply;
    }
}