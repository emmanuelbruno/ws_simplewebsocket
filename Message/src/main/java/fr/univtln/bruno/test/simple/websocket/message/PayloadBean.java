package fr.univtln.bruno.test.simple.websocket.message;

import fr.univtln.bruno.test.simple.jsoncoder.JSONCoder;
import fr.univtln.bruno.test.simple.personne.Personne;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bruno on 26/03/14.
 */
public class PayloadBean implements Serializable {
    public final static class PayloadBeanCode extends
            JSONCoder<PayloadBean> {
    }

    private Date date;
    private Personne sender;
    private String message;

    public PayloadBean() {
    }

    public PayloadBean(Date date,Personne sender,String message) {
        this.date = date;
        this.sender = sender;
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSender(Personne sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public Personne getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "PayloadBean{" +
                "date=" + date +
                ", sender=" + sender +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayloadBean that = (PayloadBean) o;

        if (!date.equals(that.date)) return false;
        if (!message.equals(that.message)) return false;
        if (!sender.equals(that.sender)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}