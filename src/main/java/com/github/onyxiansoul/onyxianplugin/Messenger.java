package com.github.onyxiansoul.onyxianplugin;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public final class Messenger {
  private static ConsoleCommandSender CONSOLE_SENDER;
  private static String INFO_PREFIX;
  private static String WARNING_PREFIX;
  private static String ERROR_PREFIX;
  private static String ERROR_SUFIX;
  private static boolean debugEnabled;

  protected Messenger(OnyxianPlugin onyxianPlugin){
    CONSOLE_SENDER = Bukkit.getConsoleSender();
    String pluginName = "["+onyxianPlugin.getName()+"]";
    INFO_PREFIX = ChatColor.GREEN +pluginName+ " ";
    WARNING_PREFIX = System.lineSeparator()+ ChatColor.GOLD+"[WARNING]"+pluginName+" ";
    ERROR_PREFIX = System.lineSeparator()+ ChatColor.RED+"[ERROR]"+pluginName+" ";
    ERROR_SUFIX = System.lineSeparator() + ChatColor.RED+ "Disabling"+" "+pluginName + "!";
    debugEnabled = true;
  }

  public void sendInfo(String infoMessage){
    CONSOLE_SENDER.sendMessage(INFO_PREFIX + infoMessage);
  }

  public void sendWarning(String warningMessage){
    CONSOLE_SENDER.sendMessage(WARNING_PREFIX + warningMessage);
  }
  public void sendWarning(Exception e){
    sendWarning(getFormattedException(e));
  }

  public void sendWarning(String extraMessage, Exception e){
    sendWarning(extraMessage+ " this was caused by: "+ System.lineSeparator()+ getFormattedException(e));
  }

  public void sendError(String errorMessage){
      CONSOLE_SENDER.sendMessage(ERROR_PREFIX + errorMessage + ERROR_SUFIX);
  }

  public void sendError(Exception e){
    String errorMessage = getFormattedException(e);
    sendError(errorMessage);
  }

  public void sendError(String errorMessage, Exception e){
    sendError(errorMessage+ " this was caused by: "+ System.lineSeparator()+ getFormattedException(e));
  }

  private String getFormattedException(Throwable ex){
      StringBuilder sb = new StringBuilder();
      if(ex.getMessage()!=null){
        sb.append(ex.getMessage());
      }
      else{
        sb.append(ex.getClass().getSimpleName());
      }
      sb.append(" ");
      if(debugEnabled){
          sb.append(System.lineSeparator());
          sb.append(ChatColor.GOLD);
          sb.append("-------------------------------------");
          sb.append(System.lineSeparator());
          sb.append(ChatColor.DARK_GRAY);
          sb.append(ex.getClass().getName());
          sb.append(System.lineSeparator());
          for(StackTraceElement element: ex.getStackTrace()){
              sb.append(" at: ");
              sb.append(element);
              sb.append(System.lineSeparator());
          }
          sb.append(ChatColor.GOLD);
          sb.append("-------------------------------------" );
      }
      if(ex.getCause()!=null){
          sb.append(System.lineSeparator());
          sb.append("Caused By: " );
          sb.append(ChatColor.GOLD);
          sb.append(System.lineSeparator()); //
          sb.append(getFormattedException(ExceptionUtils.getCause(ex)));
          //sb.append(getFormattedException(ExceptionUtils.getRootCause(ex)));
      }
      else{
        sb.append(System.lineSeparator());
        sb.append("---------------------------------------------------------");
      }
      return sb.toString();
  }
    
  public void setDebug(boolean debug){
      debugEnabled = debug;
  }

  public boolean isDebugEnabled() {
    return debugEnabled;
  }

        
}
