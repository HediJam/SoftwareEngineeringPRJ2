/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Hedieh Jam
 */
public class TransactionXMLHandler {

    private XMLInputFactory factory;
    private XMLStreamReader reader;
    private int event;
    public TransactionXMLHandler(){
        try {
            factory = XMLInputFactory.newInstance();
            try {
                reader = factory.createXMLStreamReader(new FileInputStream(new File("terminal.xml")));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE,"terminal.xml not found", ex);
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(TransactionXMLHandler.class.getName()).log(Level.SEVERE,"problem in parssing xml", ex);
        }
    }
}
