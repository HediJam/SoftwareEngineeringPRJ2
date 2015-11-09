/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hedieh Jam
 */
public class Deposit {

    private String name;
    private BigDecimal balance;
    private BigDecimal upperBound;
    private String id;
    static private HashMap <String,Deposit> deposits = new HashMap<String , Deposit>();

    Deposit(String name,String id, String initialBalance, String upperBound) {
        this.id = id;
        this.name = name;
        this.balance = new BigDecimal(initialBalance.replaceAll(",", ""));
        this.upperBound = new BigDecimal(upperBound.replaceAll(",", ""));
        deposits.put(this.id,this);
    }

    public static String depositIn(String id,String amount) {
        Deposit curDeposit = deposits.get(id);
        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(!curDeposit.isPossitiveAmount(amountOfMoney))
            System.out.println("manfie ke");
      //sync
        /*if (curDeposit.isPossibleToDeposit(amountOfMoney)) {
            curDeposit.balance = curDeposit.balance.add(amountOfMoney);
            
            System.out.println(curDeposit.balance);
            System.out.println(deposits.get(id).balance);
            return ("deposit was successful");
        }*/
        boolean successState = curDeposit.addDepositBalance(amountOfMoney);
        
        if (successState)
            return "succesful";
        return "unsuccesful";
    }

    public static String withdraw(String id,String amount) {
        Deposit curDeposit = deposits.get(id);
        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(curDeposit.subcribeDepositBalance(amountOfMoney))
            return "successful";
        return "unsucessful";
    }

    private synchronized boolean isPossibleToDeposit(BigDecimal amountOfMoney) {

        BigDecimal tempBalance = balance.add(amountOfMoney);
        if (tempBalance.compareTo(upperBound) < 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossitiveAmount(BigDecimal amountOfMoney) {
        System.out.println(amountOfMoney);
        if (amountOfMoney.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
           return true;
    }
    private synchronized boolean isPossibleToWithdraw(BigDecimal amountOfMoney){
        BigDecimal tempBalance = balance.subtract(amountOfMoney);
        System.out.println(tempBalance);
        if (tempBalance.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        } else {
            return true;
        }
    }
    private synchronized boolean addDepositBalance(BigDecimal amountOfMoney){
        if (isPossibleToDeposit(amountOfMoney)) {
            balance = balance.add(amountOfMoney);
            
            System.out.println(balance);
            System.out.println(deposits.get(id).balance);
            return true;
        }
        return false;
        
    }
    private synchronized boolean subcribeDepositBalance(BigDecimal amountOfMoney){
        if(isPossibleToWithdraw(amountOfMoney)){
            balance = balance.subtract(amountOfMoney);
            return true;
        }
        return false;
    }
}
