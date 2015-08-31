package TinyAuth;

import PluginReference.PluginBase;
import PluginReference.PluginInfo;

public class MyPlugin extends PluginBase
{
  PluginInfo info;
  public static PluginBase pl;

  public PluginInfo getPluginInfo()
  {
    PluginInfo inf = new PluginInfo();
    inf.eventSortOrder = -150000000.0D;
    inf.description = "Simple authenticator plugin";
    pl = this; inf.ref = this;
    return this.info = inf;
  }

  public void onServerFullyLoaded() {
    this.info.ref = new TinyAuth();
  }
}

/* Location:           D:\GitHub\TinyAuth.jar
 * Qualified Name:     TinyAuth.MyPlugin
 * JD-Core Version:    0.6.2
 */