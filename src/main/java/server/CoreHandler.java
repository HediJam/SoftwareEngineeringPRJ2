/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Hedieh Jam
 */
public class CoreHandler {

    private FileReader jsonFileReader;
    private int portFromFile;
    private String logFileName;

    public CoreHandler(String jsonFilePath) {
        try {
            System.out.println("json file name is: " + jsonFilePath);
            jsonFileReader = new FileReader(jsonFilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readJSONFile() {
        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(jsonFileReader);
        } catch (IOException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, "json core file not found", ex);
        } catch (ParseException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, "there are some problems in parsing", ex);
        }

        JSONObject jsonObject = (JSONObject) obj;

        portFromFile = Integer.valueOf(((Long) jsonObject.get("port")).toString());
        //String pp = (String) jsonObject.get("port");

        JSONArray depositsArray = (JSONArray) jsonObject.get("deposits");

        System.out.println("\ndeposits Array:");
        Iterator<JSONObject> iteratorOverDeposits = depositsArray.iterator();
        while (iteratorOverDeposits.hasNext()) {
            JSONObject deposit = iteratorOverDeposits.next();
            String customerName = (String) deposit.get("customer");
            String customerId = (String) deposit.get("id");
            String initialBalance = (String) deposit.get("initialBalance");
            String upperBound = (String) deposit.get("upperBound");
            Deposit curDeposit = new Deposit(customerName, customerId, initialBalance, upperBound);
            System.out.println(customerId + " " + customerName + " " + initialBalance + " " + upperBound);
        }
        logFileName = (String) jsonObject.get("outLog");

        System.out.println(logFileName + " " + portFromFile);

    }

    public int getPort() {
        return portFromFile;
    }

    public String getLogFile() {
        return logFileName;
    }

    public void writeToJsonFile() {
        JSONObject core = createCoreFormat();
        try {

            FileWriter file = new FileWriter("core2.json");
            file.write(core.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createCoreFormat() {
        JSONObject coreJSONObj = new JSONObject();
        coreJSONObj.put("port", portFromFile);
        JSONArray depositsJSONlist = Deposit.depositsToJSON();
        coreJSONObj.put("deposits", depositsJSONlist);
        return coreJSONObj;

    }
}
