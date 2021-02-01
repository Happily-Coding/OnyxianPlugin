package com.github.onyxiansoul.onyxianplugin;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;

public class MessageUtil {
    private final ConsoleCommandSender CONSOLE_SENDER;
    private final String WARNING_PREFIX;
    private final String ERROR_PREFIX;
    private final String ERROR_SUFIX;
    private boolean debugEnabled;
    
    protected MessageUtil(OnyxianPlugin onyxianPlugin){
      CONSOLE_SENDER = Bukkit.getConsoleSender();
      String pluginName = onyxianPlugin.getName();
      WARNING_PREFIX = System.lineSeparator()+ ChatColor.GOLD+"[WARNING]"+pluginName+" ";
      ERROR_PREFIX = System.lineSeparator()+ ChatColor.RED+"[ERROR]"+pluginName+" ";
      ERROR_SUFIX = System.lineSeparator() + ChatColor.RED+ "Disabling"+" "+pluginName + "!";
      debugEnabled = false;
    }
    
    public void sendWarning(String warningMessage){
        CONSOLE_SENDER.sendMessage(WARNING_PREFIX + warningMessage);
    }
    public void sendWarning(Exception e){
        String warningMessage = getFormattedException(e);
        sendWarning(warningMessage);
    }
    
    public void errorDisable(String errorMessage){
        CONSOLE_SENDER.sendMessage(ERROR_PREFIX + errorMessage + ERROR_SUFIX);
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.disablePlugin(pluginManager.getPlugin("DamagePotionEffects"));
    }
    
    public void errorDisable(Exception e){
        String errorMessage = getFormattedException(e);
        errorDisable(errorMessage);
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
    };
        
}
