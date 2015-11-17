/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

 


/**
 *
 * @author SARIR
 */
public class TransactionResultWriter {

    public TransactionResultWriter() {

    }
    public void writeToXML(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("terminal");
            doc.appendChild(rootElement);
            rootElement.setAttribute("id", "1");
            rootElement.setAttribute("type", "ATM");
            
            
            
            Element responses = doc.createElement("responses");
            rootElement.appendChild(responses);
            Element response = doc.createElement("response");
            response.setAttribute("balance", "10000");
            responses.appendChild(response);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("response.xml"));
            transformer.transform(source, result);
            
            System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(TransactionResultWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(TransactionResultWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TransactionResultWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    
    
}
