/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Hedieh Jam
 */
public class Deposit {

    private String name;
    private BigDecimal balance;
    private BigDecimal upperBound;
    private String id;
    static private HashMap<String, Deposit> deposits = new HashMap<String, Deposit>();

    Deposit(String name, String id, String initialBalance, String upperBound) {
        this.id = id;
        this.name = name;
        this.balance = new BigDecimal(initialBalance.replaceAll(",", ""));
        this.upperBound = new BigDecimal(upperBound.replaceAll(",", ""));
        deposits.put(this.id, this);
    }

    public static String depositIn(String id, String amount) {
        Deposit curDeposit = getDepositById(id);
        
        if (curDeposit != null) {
            BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
            BigDecimal balanceBeforeOperation = curDeposit.balance;
            if (!curDeposit.isPossitiveAmount(amountOfMoney)) {
                return curDeposit.operationReport("unsuccessful : negative amount of money", curDeposit, balanceBeforeOperation, amountOfMoney);
            }

            if (curDeposit.addDepositBalance(amountOfMoney)) {
                return curDeposit.operationReport("successful", curDeposit, balanceBeforeOperation, amountOfMoney);
            }
            return curDeposit.operationReport("unsuccessful : balance will be negative ", curDeposit, balanceBeforeOperation, amountOfMoney);
        }
        return "unsuccessful : Deposit with id " + id + " not found";
    }

    public static String withdraw(String id, String amount) {
        Deposit curDeposit = getDepositById(id);    
        if (curDeposit != null) {
            BigDecimal amountOfMoney = new BigDecimal(amount.replaceAll(",", ""));
            BigDecimal balanceBeforeOperation = curDeposit.balance;
            if (!curDeposit.isPossitiveAmount(amountOfMoney)) {
                return curDeposit.operationReport("unsuccessful : negative amount of money", curDeposit, balanceBeforeOperation, amountOfMoney);
            }
            if (curDeposit.subcribeDepositBalance(amountOfMoney)) {
                return curDeposit.operationReport("successful", curDeposit, balanceBeforeOperation, amountOfMoney);
            }
            return curDeposit.operationReport("unsuccessful : balance will be greater than upper bound ", curDeposit, balanceBeforeOperation, amountOfMoney);
        }
        return "unsuccessful -> Deposit with id " + id + " not found";
     
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

    private synchronized boolean isPossibleToWithdraw(BigDecimal amountOfMoney) {
        BigDecimal tempBalance = balance.subtract(amountOfMoney);
        System.out.println(tempBalance);
        if (tempBalance.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        } else {
            return true;
        }
    }

    private synchronized boolean addDepositBalance(BigDecimal amountOfMoney) {
        if (isPossibleToDeposit(amountOfMoney)) {
            balance = balance.add(amountOfMoney);
            System.out.println(balance);
            System.out.println(deposits.get(id).balance);
            return true;
        }
        return false;

    }

    private synchronized boolean subcribeDepositBalance(BigDecimal amountOfMoney) {
        if (isPossibleToWithdraw(amountOfMoney)) {
            balance = balance.subtract(amountOfMoney);
            return true;
        }
        return false;
    }

    private JSONObject toJSON() {
        JSONObject depositJSONObj = new JSONObject();
        depositJSONObj.put("customer", this.name);
        depositJSONObj.put("id", id);
        depositJSONObj.put("initialBalance", setCommaForNumbers(this.balance.toString()));
        depositJSONObj.put("upperBound", setCommaForNumbers(this.upperBound.toString()));

        return depositJSONObj;

    }

    private String setCommaForNumbers(String number) {
        ArrayList<String> chunks = new ArrayList<String>();
        while (number.length() > 3) {
            chunks.add(number.substring(number.length() - 3));
            number = number.substring(0, number.length() - 3);
        }

        for (int i = 0; i < chunks.size(); i++) {
            number = number + "," + chunks.get(chunks.size() - i - 1);
        }
        return number;

    }

    public static JSONArray depositsToJSON() {
        JSONArray depositsJSONlist = new JSONArray();
        for (String key : deposits.keySet()) {
            depositsJSONlist.add(deposits.get(key).toJSON());
        }
        return depositsJSONlist;
    }

    public static HashMap<String, Deposit> getDeposits() {
        return deposits;
    }

    private String operationReport(String header, Deposit dep, BigDecimal balanceBeforeOperation, BigDecimal amount) {
        return header + "; " + "deposit id : " + dep.id + "; " + "amount : " + amount.toString() +"; "+ "initial balance : " + balanceBeforeOperation.toString() + "; " + "balance : " + dep.balance.toString();
    }

    public static Deposit getDepositById(String id) {
        if (deposits.containsKey(id)) {
            return deposits.get(id);
        } else {
            System.out.println("daram null barmigardunam");
            return null;
        }
    }
}
