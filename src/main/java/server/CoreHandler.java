/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author SARIR
 */
public class CoreHandler {
    private FileReader jsonFileReader;
    public CoreHandler(String jsonFilePath) {
        try {
            
            jsonFileReader = new FileReader(jsonFilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void readJSONFile(){
        JSONParser parser = new JSONParser();
        Object obj = null;
        
        try {
            obj = parser.parse(jsonFileReader);
        } catch (IOException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            JSONObject jsonObject = (JSONObject) obj;
            String port = (String) jsonObject.get("port");
            JSONArray depositsArray = (JSONArray) jsonObject.get("deposits");
 
            System.out.println("port: " + port);  
            System.out.println("\ndeposits Array:");
            Iterator<String> iteratorOverDeposits = depositsArray.iterator();
            while (iteratorOverDeposits.hasNext()) {
                System.out.println(iteratorOverDeposits.next());
            }
    }
}
