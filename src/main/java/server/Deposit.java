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
    private BigDecimal Balance;
    private BigDecimal upperBound;
    static HashMap <String,Deposit> deposits = new HashMap<String , Deposit>();

    Deposit(String name,String id, String initialBalance, String upperBound) {
        this.name = name;
        this.Balance = new BigDecimal(initialBalance.replaceAll(",", ""));
        this.upperBound = new BigDecimal(upperBound.replaceAll(",", ""));
        deposits.put(id,this);
    }

    public String depositIn(String id,String amount) {
        Deposit curDeposit = deposits.get(id);
        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(!curDeposit.isPossitiveAmount(amountOfMoney))
            System.out.println("manfie ke");
      //sync
        if (curDeposit.isPossibleToDeposit(amountOfMoney)) {
            curDeposit.Balance.add(amountOfMoney);
            return ("deposit was successful");
        }
        return "unsuccesful";
    }

    public String withdraw(String id,String amount) {
        Deposit curDeposit = deposits.get(id);
        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(curDeposit.isPossibleToWithdraw(amountOfMoney)){
            curDeposit.Balance.subtract(amountOfMoney);
            return "withdraw was succesful";
        }
        return "unsucessful";
    }

    private boolean isPossibleToDeposit(BigDecimal amountOfMoney) {

        BigDecimal tempBalance = Balance.add(amountOfMoney);
        if (tempBalance.compareTo(upperBound) > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossitiveAmount(BigDecimal amountOfMoney) {
        if (amountOfMoney.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        }
           return true;
    }
    private boolean isPossibleToWithdraw(BigDecimal amountOfMoney){
        BigDecimal tempBalance = Balance.subtract(amountOfMoney);
        if (tempBalance.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        } else {
            return true;
        }
    }
}
