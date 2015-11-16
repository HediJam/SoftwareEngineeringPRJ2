/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author SARIR
 */
public class ServerFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        String msg = String.valueOf(System.currentTimeMillis());
        msg += "\r\n" + record.getLevel() + ":" +record.getMessage() + "\r\n";
        return msg;
    }
    
}
