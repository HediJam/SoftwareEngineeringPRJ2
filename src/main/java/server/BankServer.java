/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hedieh Jam
 */
public class BankServer {

    public String coreFilePath = "core.json";
    public String logFilePath;
    public int port;

    public static void main(String[] args) throws Exception {
        System.out.println("The Banking server is running.");
        BankServer bs = new BankServer();
        //load core.json
        CoreHandler jsonCoreHandler = new CoreHandler(bs.coreFilePath);
        jsonCoreHandler.readJSONFile();
        bs.port = jsonCoreHandler.getPort();
        bs.logFilePath = jsonCoreHandler.getLogFile();
        Deposit.withdraw("33227781", "10");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(bs.port);

        new ServerCommandLine().start();
        try {
            while (true) {
                new TransactionServiceProvider(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket. The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class TransactionServiceProvider  extends Thread {

        private Socket socket;
        private int clientNumber;

        public TransactionServiceProvider(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the client a welcome
         * message then repeatedly reading strings and sending back the
         * capitalized version of the string.
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("we are ready to service you");

                // Get messages from the client, line by line; return them
                // capitalized
                while (true) {
                    String input = in.readLine();
                    System.out.println("message for client! " + input);
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    TransactionExecuter executer = new TransactionExecuter(input);
                    out.println(executer.execute());
                }
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message. In this case we just write the message to the
         * server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }

    private static class ServerCommandLine extends Thread {

        public void run() {
            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String command = bufferRead.readLine();
                    System.out.println("entered command is : " + command);
                }
            } catch (IOException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
