package TinyAuth;

import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_HangingEntityType;
import PluginReference.MC_ItemFrameActionType;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_PotionEffectType;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import PluginReference.PluginBase;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TinyAuth extends PluginBase
{
  public static ConfigFile kf;
  public static ConfigFile pf;
  public List<Player> pl = new ArrayList();
  public static List<String> pln = new ArrayList();
  public static MC_Location spawnloc;

  public TinyAuth()
  {
    String dir = KFA.fileCopy(this, "config.yml", false);
    kf = new ConfigFile(dir + File.separator + "config.yml");
    pf = new ConfigFile(dir + File.separator + "players.yml");
    spawnloc = kf.getLocation("location", KFA.srv.getWorld(0).getSpawnLocation());
  }

  public void onTick(int tickNumber) {
    for (Player p : this.pl)
      if (p.plr.getLocation().distanceTo(spawnloc) > 2.0D) {
        p.plr.teleport(spawnloc);
        p.plr.sendMessage(p.getMessage());
      }
  }

  public void onPlayerJoin(MC_Player plr)
  {
    Player p = new Player(plr);
    this.pl.add(p);
    plr.setInvisible(true);
    plr.setInventory(new ArrayList());
    plr.teleport(spawnloc);
    plr.sendMessage(p.getMessage());
  }

  public void onPlayerLogout(String playerName, UUID uuid) {
    int id = pln.indexOf(playerName);
    if (id != -1) {
      this.pl.remove(id);
      pln.remove(id);
    }
  }

  public boolean eventCancelTest(MC_Player plr) { int id = pln.indexOf(plr.getName());
    if (id != -1) {
      return true;
    }
    return false;
  }

  public void onAttemptBlockBreak(MC_Player plr, MC_Location loc, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptAttackEntity(MC_Player plr, MC_Entity ent, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptItemFrameInteract(MC_Player plr, MC_Location loc, MC_ItemFrameActionType actionType, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptDamageHangingEntity(MC_Player plr, MC_Location loc, MC_HangingEntityType entType, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptPlaceOrInteract(MC_Player plr, MC_Location loc, MC_EventInfo ei, MC_DirectionNESWUD dir)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptPlayerMove(MC_Player plr, MC_Location locFrom, MC_Location locTo, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onAttemptPotionEffect(MC_Player plr, MC_PotionEffectType potionType, MC_EventInfo ei)
  {
    ei.isCancelled = eventCancelTest(plr);
  }

  public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
    int id = pln.indexOf(plr.getName());
    if (id != -1) {
      ei.isCancelled = true;
      Player p = (Player)this.pl.get(id);
      boolean registered = p.password != 0;
      String[] cmd = msg.split("\\ ", 2);
      if (registered) {
        if ((cmd[0].equalsIgnoreCase("/l")) && (cmd.length == 2)) {
          if (cmd[1].hashCode() == p.password) {
            plr.sendMessage(kf.get("message.pwdok"));
            plr.teleport(p.loc);
            plr.setInventory(p.inv);
            plr.setInvisible(false);
            pf.remove("location." + p.name);
            pf.remove("inventory." + p.name);
            pf.save();
            pln.remove(id);
            this.pl.remove(id);
          }
          else {
            p.tries -= 1;
            if (p.tries == 0) {
              KFA.srv.executeCommand("kick " + plr.getName());
            }
            plr.sendMessage(kf.get("message.wrongpwd"));
          }

        }

      }
      else if ((cmd[0].equalsIgnoreCase("/reg")) && (cmd.length == 2)) {
        pf.set("password." + p.name, Integer.valueOf(cmd[1].hashCode()));
        pf.save();
        this.pl.remove(id);
        pln.remove(id);
        plr.setInvisible(false);
        plr.sendMessage(kf.get("message.registered"));
        return;
      }

      plr.sendMessage(p.getMessage());
    }
    else if ((msg.startsWith("/setauthspawn")) && (plr.hasPermission("tinyauth.admin"))) {
      spawnloc = plr.getLocation();
      spawnloc.y -= 2.0D;
      kf.setlocation("location", spawnloc);
      plr.sendMessage(kf.get("message.setspawn")
        .replace("<x>", spawnloc.getBlockX())
        .replace("<y>", spawnloc.getBlockZ())
        .replace("<z>", spawnloc.getBlockY()));
      kf.save();
      ei.isCancelled = true;
    }
  }
}

/* Location:           D:\GitHub\TinyAuth.jar
 * Qualified Name:     TinyAuth.TinyAuth
 * JD-Core Version:    0.6.2
 */