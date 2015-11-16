/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Hedieh Jam
 */
public class Terminal {

    private String serverIpAddress;
    private String serverPort;
    private String terminalId;
    private String terminalType;
    private Logger terminalLogger;

    private Terminal() {
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
        FileHandler logFileHandler = null;
        try {
            logFileHandler = new FileHandler(logFileName);
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
        terminalLogger.addHandler(logFileHandler);
        TerminalFormatter formatter = new TerminalFormatter(terminalId, terminalType);
        logFileHandler.setFormatter(formatter);
    }

 

    public static void main(String[] args) throws Exception {
        
        Terminal runnigTerminal = new Terminal();
        Socket clientSocket = runnigTerminal.connectToServer();
        runnigTerminal.requestTransactionExecution(clientSocket);

    }
   private Socket connectToServer(){
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(serverIpAddress, Integer.parseInt(serverPort));
        } catch (IOException ex) {
            terminalLogger.severe("can not find the server:program terminated");
            System.exit(0);
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientSocket;
    }
    private void requestTransactionExecution(Socket clientSocket) {
        DataOutputStream outToServer = null;
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            terminalLogger.severe("socket can not write on server port in 'requestTransactionExecution' method");
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        BufferedReader inFromServer = null;
        try {
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            terminalLogger.severe("socket can not read from server port in 'requestTransactionExecution' method");
        }

        System.out.println(Transaction.transactions.size());
        for (String formattedTransaction : Transaction.transactions.keySet()) {
            System.out.println("sare for");
            try {
                outToServer.writeBytes(Transaction.transactions.get(formattedTransaction).toString() + terminalId + ";" + terminalType + '\n');
            } catch (IOException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                terminalLogger.severe("socket can not write on server port in 'requestTransactionExecution' method");
            }
            String reponseForServer = null;
            try {
    
                reponseForServer = inFromServer.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                terminalLogger.severe("socket can not read from server port in 'requestTransactionExecution' method");
                
            }
            System.out.println("From server :" + reponseForServer);
            terminalLogger.info(reponseForServer);
            
        }
    }

}
