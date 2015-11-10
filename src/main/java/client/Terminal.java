/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Hedieh Jam
 */
public class Terminal {

    private String serverIpAddress;
    private String serverPort;
    private String terminalId;
    private FileHandler logFileHandler;
    private String terminalType;
    private Logger terminalLogger;

    public Terminal() {
        TransactionFileReader xmlHandler = new TransactionFileReader();
        xmlHandler.parseTransactionsFile();
        System.out.println(Transaction.transactions);
        serverIpAddress = xmlHandler.getServerIp();
        serverPort = xmlHandler.getServerPort();
        terminalId = xmlHandler.getTerminalId();
        terminalType = xmlHandler.getTerminalType();
        setLogger(xmlHandler.getLogFileName());

    }

    private void setLogger(String logFileName) {
        terminalLogger = Logger.getLogger("terminalLogger" + terminalId);
        try {
            logFileHandler = new FileHandler(logFileName);
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
        terminalLogger.addHandler(logFileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        logFileHandler.setFormatter(formatter);
    }

 

    public static void main(String[] args) throws Exception {
        
        Terminal runnigTerminal = new Terminal();
        Socket clientSocket = runnigTerminal.connectToServer();
        runnigTerminal.requestTransactionExecution(clientSocket);

    }
   private Socket connectToServer() throws IOException {
        Socket clientSocket = new Socket(serverIpAddress, Integer.parseInt(serverPort));
        return clientSocket;
    }
    private void requestTransactionExecution(Socket clientSocket) {
        DataOutputStream outToServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            terminalLogger.severe("socket can not write on server port");
        }
        BufferedReader inFromServer = null;
        try {
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(Transaction.transactions.size());
        for (String formattedTransaction : Transaction.transactions.keySet()) {
            try {
                //System.out.println(Transaction.transactions.get(formattedTransaction).toString());
                outToServer.writeBytes(Transaction.transactions.get(formattedTransaction).toString() + '\n');
            } catch (IOException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            String reponseForServer = null;
            try {
                reponseForServer = inFromServer.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("From server :" + reponseForServer);
        }
    }

}
