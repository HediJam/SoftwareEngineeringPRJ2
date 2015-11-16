/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.Terminal;
import client.TerminalFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hedieh Jam
 */
public class BankServer {

    private String coreFilePath = "core.json";
    private int port;
    private Logger serverLogger;
    private static CoreHandler jsonCoreHandler;

    private BankServer() {
        jsonCoreHandler = new CoreHandler(coreFilePath);
        jsonCoreHandler.readJSONFile();
        port = jsonCoreHandler.getPort();      
        setLogger(jsonCoreHandler.getLogFile());
    }

    private void setLogger(String logFileName) {
        serverLogger = Logger.getLogger("serverLogger");
        FileHandler logFileHandler = null;
        try {
            logFileHandler = new FileHandler(logFileName);
        } catch (IOException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverLogger.addHandler(logFileHandler);
        ServerFormatter formatter = new ServerFormatter();
        logFileHandler.setFormatter(formatter);

    }

    public static void main(String[] args) throws Exception {
        System.out.println("The Banking server is running.");
        BankServer bs = new BankServer();
        ServerSocket listener = new ServerSocket(bs.port);

        new ServerCommandLine().start();
        try {
            while (true) {
                new TransactionServiceProvider(listener.accept(),bs.serverLogger).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class TransactionServiceProvider extends Thread {

        private Socket socket;
        private Logger serverLogger;

        public TransactionServiceProvider(Socket socket, Logger serverLogger) {
            this.socket = socket;
            this.serverLogger = serverLogger;
            //log("New connection with client# " + clientNumber + " at " + socket);
        }


        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        break;
                    }
                    System.out.println("from client :   " + input);
                    TransactionExecuter executer = new TransactionExecuter(input);
                    
                    String transactionResult = executer.execute();
                    //System.out.println(transactionResult);
                    out.println(transactionResult);
                    serverLogger.info(executer.getOriginTerminal()+ ":"+ transactionResult);
                }
            } catch (IOException e) {

            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    
                }

            }
        }

    }

    private static class ServerCommandLine extends Thread {

        public void run() {
            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String command = bufferRead.readLine();
                    System.out.println("entered command is : " + command);
                    jsonCoreHandler.writeToJsonFile();

                }
            } catch (IOException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
