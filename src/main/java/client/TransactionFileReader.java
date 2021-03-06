/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Hedieh Jam
 */
public class TransactionFileReader {

    private XMLInputFactory factory;
    private InputStream xmlInputStream;
    

    private String serverIpAddress;
    private String serverPort;
    private String terminalId;
    private String logFileName;
    private String terminalType;

    public TransactionFileReader() {

        factory = XMLInputFactory.newInstance();
        try {
            xmlInputStream = new FileInputStream("terminal.xml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TransactionFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parseTransactionsFile() {
        XMLEventReader eventReader = null;

        try {
            eventReader = factory.createXMLEventReader(xmlInputStream);
        } catch (XMLStreamException ex) {
            Logger.getLogger(TransactionFileReader.class.getName()).log(Level.SEVERE, "can not event reader", ex);
        }
        while (eventReader.hasNext()) {
            XMLEvent event = null;
            try {
                event = eventReader.nextEvent();
            } catch (XMLStreamException ex) {
                Logger.getLogger(TransactionFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }

            //reach the start of an item
            if (event.isStartElement()) {

                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart().equals("terminal")) {
                    System.out.println("--start of an terminal");
                    // attribute
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("id")) {
                            System.out.println("id = " + attribute.getValue());
                            terminalId = attribute.getValue();
                        } else if (attribute.getName().toString().equals("type")) {
                            System.out.println("type = " + attribute.getValue());
                            terminalType = attribute.getValue();
                        }
                    }
                } else if (event.asStartElement().getName().getLocalPart().equals("transaction")) {
                    Transaction currentTransaction = new Transaction();
                    try {
                        event = eventReader.nextEvent();
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(TransactionFileReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        switch (attribute.getName().toString()) {
                            case "id":
                                System.out.println("id = " + attribute.getValue());
                                currentTransaction.setTransactionId(attribute.getValue());
                                break;
                            case "type":
                                System.out.println("type = " + attribute.getValue());
                                currentTransaction.setType(attribute.getValue());
                                break;
                            case "amount":
                                System.out.println("amount = " + attribute.getValue());
                                currentTransaction.setAmount(attribute.getValue());
                                break;
                            case "deposit":
                                System.out.println("deposit id = " + attribute.getValue());
                                currentTransaction.setDepositId(attribute.getValue());
                                break;
                        }

                    }
                    currentTransaction.addTransaction();
                } else if (event.asStartElement().getName().getLocalPart().equals("outLog")) {
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("path")) {
                            System.out.println("path = " + attribute.getValue());
                            logFileName = attribute.getValue();
                        }
                    }
                } else if (event.asStartElement().getName().getLocalPart().equals("server")) {
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("ip")) {
                            System.out.println("ip = " + attribute.getValue());
                            serverIpAddress = attribute.getValue();
                        }
                        if (attribute.getName().toString().equals("port")) {
                            System.out.println("port = " + attribute.getValue());
                            serverPort = attribute.getValue();
                        }
                    }
                }

            }

            //reach the end of an item
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals("terminal")) {
                    System.out.println("--end of an terminal\n");
                }
            }

        }
    }
    public String getServerIp(){
        return serverIpAddress;
    }
    public String getServerPort(){
        return serverPort;
    }
    public String getLogFileName(){
        return logFileName;
    }
    public String getTerminalType(){
        return terminalType;
    }
    public String getTerminalId(){
        return terminalId;
    }
}
