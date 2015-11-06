/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author SARIR
 */
public class Transaction {
    
    private String depositId;
    private String type;
    private String amount;
    private String transactionId;
    static public HashMap <String, Transaction> transactions = new LinkedHashMap<>();
    public Transaction(String transactionId, String type, String amount ,String depositId) {
        this.depositId = depositId;
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        transactions.put(this.transactionId,this);   
    }
    @Override public String toString(){
        return (depositId+";"+type+";"+amount+";"+transactionId);
    }
    
    
}
