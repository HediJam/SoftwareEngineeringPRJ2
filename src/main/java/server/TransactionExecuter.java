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
    Transaction transaction;
    public TransactionExecuter(String clientMessage) {
        String[] parts = clientMessage.split(";");
        transaction = new Transaction();
        transaction.setDepositId(parts[0]);
        transaction.setType(parts[1]);
        transaction.setAmount(parts[2]);
        transaction.setTransactionId(parts[3]);    
    }
    public String execute(){
        if(transaction.getType().equals("deposit")){
            return Deposit.depositIn(transaction.getDepositId(), transaction.getAmount()) + "for logging";
        }
        else if(transaction.getType().equals("withdraw")){
            return Deposit.withdraw(transaction.getDepositId(), transaction.getAmount());
        }
        return "invalid action";
    }
    
    
}
