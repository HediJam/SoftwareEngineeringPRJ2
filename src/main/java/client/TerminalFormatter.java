/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author SARIR
 */
public class TerminalFormatter extends  Formatter{
    String terminalType;
    String terminalId;
    TerminalFormatter(String id, String type){
        terminalType = type;
        terminalId = id;
    }
  @Override
    public String format(LogRecord record) {
        return record.getLevel() + ":" + terminalType + ":" + terminalId +":"+record.getMessage();
    }
}
