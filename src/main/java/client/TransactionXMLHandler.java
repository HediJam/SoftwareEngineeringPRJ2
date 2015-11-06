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
public class TransactionXMLHandler {

    private XMLInputFactory factory;
    private InputStream xmlInputStream;
    private XMLEventReader eventReader;

    private String serverIpAddress;
    private String serverPort;
    private String terminalId;
    private String logFile;
    private String terminalType;

    public TransactionXMLHandler() {

        factory = XMLInputFactory.newInstance();
        try {
            xmlInputStream = new FileInputStream("terminal.xml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parseTransactionsFile() {
        String tagValue = null;

        try {
            eventReader = factory.createXMLEventReader(xmlInputStream);
        } catch (XMLStreamException ex) {
            Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE, "can not event reader", ex);
        }
        while (eventReader.hasNext()) {
            XMLEvent event = null;
            try {
                event = eventReader.nextEvent();
            } catch (XMLStreamException ex) {
                Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            //reach the start of an item
            if (event.isStartElement()) {

                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart().equals("terminal")) {
                    System.out.println("--start of an item");
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
                    Transaction currentTransaction = null;
                    try {
                        event = eventReader.nextEvent();
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("id")) {
                            System.out.println("id = " + attribute.getValue());
                            currentTransaction.setTransactionId(attribute.getValue());
                        } else if (attribute.getName().toString().equals("type")) {
                            System.out.println("type = " + attribute.getValue());
                            currentTransaction.setType(attribute.getValue());
                        } else if (attribute.getName().toString().equals("amount")) {
                            System.out.println("amount = " + attribute.getValue());
                            currentTransaction.setAmount(attribute.getValue());
                        } else if (attribute.getName().toString().equals("deposit")) {
                            System.out.println("deposit id = " + attribute.getValue());
                            currentTransaction.setDepositId(attribute.getValue());
                        }

                    }
                    currentTransaction.addTransaction();
                }

            }

            //reach the end of an item
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart() == "terminal") {
                    System.out.println("--end of an item\n");
                }
            }

        }
    }
}
