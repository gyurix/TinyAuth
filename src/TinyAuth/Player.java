package TinyAuth;

import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.util.ArrayList;
import java.util.List;

public class Player
{
  String name;
  MC_Location loc;
  MC_Player plr;
  int password;
  int tries = 3;
  List<MC_ItemStack> inv = new ArrayList();

  public Player(MC_Player p) { this.name = p.getName();
    this.plr = p;
    this.password = ((int)TinyAuth.pf.getLong("password." + this.name, 0L));
    if (TinyAuth.pf.list1.contains("location." + this.name)) {
      this.loc = TinyAuth.pf.getLocation("location." + this.name);
    }
    else {
      this.loc = p.getLocation();
      TinyAuth.pf.setlocation("location." + this.name, this.loc);
    }
    List i = this.plr.getInventory();
    if (TinyAuth.pf.list1.contains("inventory." + this.name)) {
      this.inv = KFA.invfromstr(TinyAuth.pf.get("inventory." + this.name));
    } else {
      this.inv = i;
      TinyAuth.pf.set("inventory." + this.name, KFA.invtostr(i));
    }
    TinyAuth.pln.add(this.name);
    TinyAuth.pf.save(); }

  public String getMessage() {
    return this.password == 0 ? TinyAuth.kf.get("message.register") : TinyAuth.kf.get("message.login");
  }

  public boolean equals(Object obj) {
    return obj.toString().equals(this.name);
  }

  public String toString() {
    return this.name;
  }
}

/* Location:           D:\GitHub\TinyAuth.jar
 * Qualified Name:     TinyAuth.Player
 * JD-Core Version:    0.6.2
 */