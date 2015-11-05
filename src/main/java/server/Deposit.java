/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.math.BigDecimal;

/**
 *
 * @author Hedieh Jam
 */
public class Deposit {

    private String name;
    private BigDecimal Balance;
    private BigDecimal upperBound;

    Deposit(String name, String initialBalance, String upperBound) {
        this.name = name;
        this.Balance = new BigDecimal(initialBalance.replaceAll(",", ""));
        this.upperBound = new BigDecimal(upperBound.replaceAll(",", ""));
    }

    public String depositIn(String amount) {

        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(!isPossitiveAmount(amountOfMoney))
            System.out.println("manfie ke");
      //sync
        if (isPossibleToDeposit(amountOfMoney)) {
            Balance.add(amountOfMoney);
            return ("deposit was successful");
        }
        return "unsuccesful";
    }

    public String withdraw(String amount) {
        BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
        if(isPossibleToWithdraw(amountOfMoney)){
            Balance.subtract(amountOfMoney);
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
