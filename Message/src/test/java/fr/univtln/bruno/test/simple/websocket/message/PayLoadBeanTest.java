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
        new PayloadBean(new Date(), personne, message);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Test d'encode et de décodage.
     */
    @Test
    public void testEncodeDecode() throws Exception {
        //On encode une personne en JSON dans un StringWriter
        StringWriter sw = new StringWriter();
        new PayloadBean.PayloadBeanCode().encode(payloadBean, sw);

        //On decode le StringWriter dans une autre instance de Personne
        PayloadBean newPayLoadBean = new PayloadBean.PayloadBeanCode().decode(new StringReader(sw.toString()));

        //On vérifie l'égalite
        Assert.assertEquals(payloadBean, newPayLoadBean);
    }
} 
