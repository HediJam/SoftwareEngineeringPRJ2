/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.util.ArrayList;
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

    private String terminalId;
    private String terminalType;
    private static ArrayList<TransactionResult> responsesList;

    public TransactionResultWriter() {
    }

    public TransactionResultWriter(String id, String type) {
        terminalId = id;
        terminalType = type;
        responsesList = new ArrayList<>();
    }

    public void addResponse(String res) {
        TransactionResult tr = new TransactionResult(res);
        responsesList.add(tr);
    }

    public void writeToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("terminal");
            doc.appendChild(rootElement);
            rootElement.setAttribute("id", terminalId);
            rootElement.setAttribute("type", terminalType);

            Element responses = doc.createElement("responses");
            rootElement.appendChild(responses);
            for (TransactionResult tr : responsesList) {
                Element response = doc.createElement("response");
                response.setAttribute("balance", tr.balance);
                response.setAttribute("termId", terminalId);
                responses.appendChild(response);
            }

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

    private class TransactionResult {

        Transaction transaction;
        String state;
        String balance;
        String initialBalance;

        public TransactionResult(String result) {
            //INFO;ATM;21374;unsuccessful : balance will be greater than upper bound ; deposit id : 35527439; amount : 10000; 
            String[] parts = result.split(";");
            //System.out.println(parts[0]);
            state = parts[0].split(":")[0];
            if (parts.length > 3) {
                transaction = new Transaction();
                transaction.setDepositId(parts[1].split(":")[1]);
                transaction.setAmount(parts[2].split(":")[1]);
                balance = parts[3].split(":")[1];
                initialBalance = parts[4].split(":")[1];
                

            }

        }

    }

}
