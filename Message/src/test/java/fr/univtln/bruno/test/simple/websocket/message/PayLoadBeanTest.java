package fr.univtln.bruno.test.simple.websocket.message;

import fr.univtln.bruno.test.simple.personne.Personne;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

/**
 * PayLoadBean Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>mars 24, 2014</pre>
 */
public class PayLoadBeanTest {
    private Personne personne = new Personne(1, "John", "Doe");
    private String message = "Hello worlds !";
    private PayloadBean payloadBean;

    @Before
    public void before() throws Exception {
        payloadBean=new PayloadBean(new Date(), personne, message);
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertTrue(new PayloadBean(payloadBean.getDate(),personne,message).equals(payloadBean));
        Assert.assertTrue(new PayloadBean(payloadBean.getDate(),personne,message).hashCode()==payloadBean.hashCode());
    }

    /**
     * Test d'encode et de décodage.
     */
    @Test
    public void testEncodeDecode() throws Exception {
        //On encode une personne en JSON dans un StringWriter
        StringWriter sw = new StringWriter();

        PayloadBean.PayloadBeanCode payloadBeanCoder = new PayloadBean.PayloadBeanCode();
        payloadBeanCoder.init();
        payloadBeanCoder.encode(payloadBean, sw);

        //On decode le StringWriter dans une autre instance de Personne
        PayloadBean newPayLoadBean = payloadBeanCoder.decode(new StringReader(sw.toString()));

        //On vérifie l'égalite
        Assert.assertEquals(payloadBean, newPayLoadBean);
    }
} 
