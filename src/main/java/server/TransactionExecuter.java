/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import client.Transaction;

/**
 *
 * @author SARIR
 */
public class TransactionExecuter {
    private Transaction transaction;
    private String originTerminal;
    public TransactionExecuter(String clientMessage) {
        String[] parts = clientMessage.split(";");
        transaction = new Transaction();
        transaction.setDepositId(parts[0]);
        transaction.setType(parts[1]);
        transaction.setAmount(parts[2]);
        transaction.setTransactionId(parts[3]);
        originTerminal = parts[5] + ":"+ parts[4];
        //System.out.println("tuye executer " + parts[4] + parts[5]);
        
    }
    public String execute(){
        if(transaction.getType().equals("deposit")){
            String operationResult = Deposit.depositIn(transaction.getDepositId(), transaction.getAmount());
            operationResult += attachTransactionId(transaction);
            return operationResult;
        }
        else if(transaction.getType().equals("withdraw")){
            String operationResult = Deposit.withdraw(transaction.getDepositId(), transaction.getAmount());
            operationResult += attachTransactionId(transaction);
            return operationResult;
            
        }
        return "invalid command\r\n";
    }
    public String getTransactionIdentification(){
        return "";
    }
    public String getOriginTerminal(){
        return originTerminal;
    }
    private String attachTransactionId(Transaction tr){
        return "transaction Id : " + tr.getTransactionId() + ";";
    }
    
    
}
