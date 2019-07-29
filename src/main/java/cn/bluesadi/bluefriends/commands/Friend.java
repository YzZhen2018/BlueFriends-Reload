package cn.bluesadi.bluefriends.commands;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFLogger;
import cn.bluesadi.bluefriends.util.BFUtil;
import com.sun.org.apache.bcel.internal.generic.NOP;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;

import static cn.bluesadi.bluefriends.config.Message.*;

public class Friend implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 2) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
                if(BFUtil.existsFakePlayer(args[1]) || BFUtil.existsPlayer(args[1])) {
                    if(args[0].equalsIgnoreCase("query")){
                        if (player.hasPermission("bluefriends.friend.query")) {
                            BluesGui gui = BlueFriends.getGuiManager().createGui("query", player);
                            OfflinePlayer off = Bukkit.getOfflinePlayer(BFUtil.getPlayerUUID(args[1]));
                            gui.getComponents().forEach(bluesComponent -> {
                                Function<String, String> f = bluesComponent.getPlaceholderFunction();
                                bluesComponent.setPlaceholderFunction(raw -> f.apply(PlaceholderAPI.
                                        setPlaceholders(off, raw)));
                            });
                            gui.open();
                        }else{
                            BFUtil.sendMessageBox(sender, NO_PERMISSION);
                        }
                    }else if (args[0].equalsIgnoreCase("apply")) {
                        if(BFUtil.existsFakePlayer(args[1])){
                            BFPlayer another = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[1]));
                            if (player.hasPermission("bluefriends.fakeplayer.apply")) {
                                if(!another.getUUID().equals(bfPlayer.getUUID())) {
                                    if (!bfPlayer.hasFriend(another)) {
                                        if (another.isFakePlayer()) {
                                            bfPlayer.addFriend(another);
                                            another.addFriend(bfPlayer);
                                            bfPlayer.sendMessageBox(FRIEND_REQUEST_ACCEPTED.replaceAll("%player%", args[1]));
                                        } else {
                                            bfPlayer.sendMessageBox(PLAYER_NOT_EXISTS.replaceAll("%player%", args[1]));
                                        }
                                    } else {
                                        bfPlayer.sendMessageBox(HAS_BEEN_FRIEND.replaceAll("%player%", args[1]));
                                    }
                                }else{
                                    bfPlayer.sendMessageBox(CANT_APPLY_SELF);
                                }
                            } else {
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                            return false;
                        }
                        if(player.hasPermission("bluefriends.friend.apply")) {
                            BFPlayer another = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[1]));
                            if (!bfPlayer.hasRequester(another)) {
                                if (!bfPlayer.hasFriend(another)) {
                                    if(!bfPlayer.getUUID().equals(another.getUUID())) {
                                        another.addRequester(bfPlayer);
                                        bfPlayer.sendMessageBox(SEND_FRIEND_REQUEST.replaceAll("%player%", args[1]));
                                        another.sendMessage(RECEIVE_FRIEND_REQUEST.replaceAll("%player%", sender.getName()));
                                    }else{
                                        bfPlayer.sendMessageBox(CANT_APPLY_SELF);
                                    }
                                } else {
                                    bfPlayer.sendMessageBox(HAS_BEEN_FRIEND.replaceAll("%player%",args[1]));
                                }
                            } else {
                                bfPlayer.sendMessageBox(HAS_BEEN_REQUESTER.replaceAll("%player%",args[1]));
                            }
                        }else{
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        if(player.hasPermission("bluefriends.friend.accept")){
                            BFPlayer another = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[1]));
                            if(bfPlayer.hasRequester(another)){
                                bfPlayer.removeRequester(another);
                                bfPlayer.addFriend(another);
                                another.addFriend(bfPlayer);
                                bfPlayer.sendMessageBox(ACCEPT_FRIEND_REQUEST.replaceAll("%player%",args[1]));
                                another.sendMessage(FRIEND_REQUEST_ACCEPTED.replaceAll("%player%",sender.getName()));
                            }else{
                                bfPlayer.sendMessageBox(PLAYER_NOT_REQUEST.replaceAll("%player%",args[1]));
                            }
                        }else{
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    } else if (args[0].equalsIgnoreCase("reject")) {
                        if(player.hasPermission("bluefriends.friend.reject")){
                            BFPlayer another = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[1]));
                            if(bfPlayer.hasRequester(another)){
                                bfPlayer.removeRequester(another);
                                bfPlayer.sendMessageBox(REJECT_FRIEND_REQUEST.replaceAll("%player%",args[1]));
                                another.sendMessage(FRIEND_REQUEST_REJECTED.replaceAll("%player%",sender.getName()));
                            }else{
                                bfPlayer.sendMessageBox(PLAYER_NOT_REQUEST.replaceAll("%player%",args[1]));
                            }
                        }else{
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if(player.hasPermission("bluefriends.friend.delete")){
                            BFPlayer another = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[1]));
                            if(bfPlayer.hasFriend(another)){
                                bfPlayer.removeFriend(another);
                                another.removeFriend(bfPlayer);
                                bfPlayer.sendMessageBox(DELETE_FRIEND_SUCCESS.replaceAll("%player%",args[1]));
                                another.sendMessage(DELETED_BY_FRIEND.replaceAll("%player%",sender.getName()));
                            }else{
                                bfPlayer.sendMessageBox(DELETE_PLAYER_NO_FRIEND.replaceAll("%player%",args[1]));
                            }
                        }else{
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    }
                }else{
                    BFUtil.sendMessageBox(sender,PLAYER_NOT_EXISTS.replaceAll("%player%",args[1]));
                }
            }else{
                BFUtil.sendMessageBox(sender,PLAYER_ONLY);
            }
        }else{
            BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG.replaceAll("%wrong_size%",String.valueOf(args.length).replaceAll("right_size","2")));
        }
        return false;
    }
}
