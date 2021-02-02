package com.github.onyxiansoul.onyxianplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

//NOTE! all onyxianplugins must depend on the onyxiancore!
public abstract class OnyxianPlugin extends JavaPlugin {
  private static MessageUtil messenger;
  private static String pluginName;
  
    public static void sendInfo(String infoMessage){
      messenger.sendInfo(infoMessage);
    }
    public static void sendWarning(String warningMessage){
      messenger.sendWarning(warningMessage);
    }
    public static void sendWarning(Exception e){
      messenger.sendWarning(e);
    }
    public static void sendWarning(String extraMessage, Exception e){
      messenger.sendWarning(extraMessage+ e);
      Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
    }
    public static void errorDisable(String errorMessage){
      messenger.sendError(errorMessage);
      Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
    }
    public static void errorDisable(Exception e){
      messenger.sendError(e);
      Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
    }
    public static void errorDisable(String errorMessage, Exception e){
      messenger.sendError(errorMessage, e);
      Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
    }

  
  @Override
  public void onLoad(){
    messenger = new MessageUtil(this);
    pluginName = getName();
    ensureCoreAvailability();
  }
  
  private void ensureCoreAvailability(){
     if(!Bukkit.getPluginManager().isPluginEnabled("OnyxianCore")){
       String requiredCoreURL="https://jitpack.io/com/github/OnyxianSoul/OnyxianCore/"+getRecommendedCoreVersion()+"/OnyxianCore-"+getRecommendedCoreVersion()+".jar";
       String coreFileName = getDataFolder().getParent()+"/OnyxianCore.jar";
       try{
         sendInfo("OnyxianCore is required for "+ getName() + " to function, but it wasn't found. Attempting to download it!");
         downloadFile(requiredCoreURL,coreFileName);
         Bukkit.getPluginManager().loadPlugin(new File(coreFileName));
         Bukkit.getPluginManager().enablePlugin(this);
         sendInfo("Success!");
       }
       catch (IOException e){
           errorDisable("Tried to automatically download the OnyxianCore, but failed"+ System.lineSeparator()
           +"Since the core is essential for "+ getName() + " Plugin to work, it will disable itself."+ System.lineSeparator()
           +"Please download the core manually from: " + requiredCoreURL, e);
       } catch (InvalidPluginException | InvalidDescriptionException | UnknownDependencyException e) {
         errorDisable("Tried to load the OnyxianCore but it was impossible. This is unexpected, Please contact us ASAP. ", e);
       }
     }
   }
  
  protected abstract String getRecommendedCoreVersion();
    
  protected void downloadFile(String sourceURL, String outputFileName) throws MalformedURLException, IOException{
      URL website = new URL(sourceURL);
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream(outputFileName);
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
  }

}





