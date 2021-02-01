package com.github.onyxiansoul.onyxianplugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

//NOTE! all onyxianplugins must depend on the onyxiancore!
public abstract class OnyxianPlugin extends JavaPlugin {
  private final MessageUtil messageUtil;
  private final String coreVersion;
  
  public OnyxianPlugin(String coreVersion){
    System.out.println("Plugin instanced");
    this.coreVersion = coreVersion;
    this.messageUtil = new MessageUtil(this);
    downloadCoreIfMissing();
    //fire the evnet here?
  }
  
  @Override
  public void onLoad(){
    System.out.println("Plugin loaded");
  }
  
  private void downloadCoreIfMissing(){
    if(!isCorePresent()){
      String requiredCoreURL="https://jitpack.io/com/github/OnyxianSoul/OnyxianCore/"+coreVersion+"/OnyxianCore-"+coreVersion+".jar";
      String coreFileName = getDataFolder().getParent()+"/OnyxianCore.jar";
      try{
        System.out.println("Trying to download" + coreFileName);
        downloadFile(requiredCoreURL,coreFileName);
        System.out.println("Success!");
      }
      catch (IOException e){
          messageUtil.errorDisable("Tried to automatically download the OnyxianCore, but failed"+ System.lineSeparator()
          +"Since the core is essential for "+ getName() + " Plugin to work, it will disable itself."+ System.lineSeparator()
          +"Please download the core manually from: " + requiredCoreURL);
          e.printStackTrace();
      }
    }
  }
  
  private boolean isCorePresent(){
    return Bukkit.getPluginManager().isPluginEnabled("OnyxianCore");
  }
  
  private void downloadFile(String sourceURL, String outputFileName) throws MalformedURLException, IOException{
      URL website = new URL(sourceURL);
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream(outputFileName);
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
  }

}


