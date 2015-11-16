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
 * @author Hedieh Jam
 */
public class Transaction {
    
    private String depositId;
    private String type;
    private String amount;
    private String transactionId;
    static public HashMap <String, Transaction> transactions = new LinkedHashMap<>();
    @Override public String toString(){
        return (depositId+";"+type+";"+amount+";"+transactionId+";");
    }
    public void setDepositId(String depId){
        depositId = depId;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
    public void setTransactionId(String id){
        this.transactionId = id;
    }

    public void addTransaction(){
        transactions.put(this.transactionId, this);
    }
    public String getType(){
        return type;
    }
    public String getAmount(){
        return amount;
    }
    public String getDepositId(){
        return depositId;
    }
    public String getTransactionId(){
        return transactionId;
    }
    
    
}
