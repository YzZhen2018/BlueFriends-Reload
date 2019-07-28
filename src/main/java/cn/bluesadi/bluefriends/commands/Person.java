package cn.bluesadi.bluefriends.commands;

import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.gui.component.BluesText;
import cn.bluesadi.bluefriends.gui.component.BluesTextField;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.player.SystemMessage;
import cn.bluesadi.bluefriends.util.BFUtil;
import cn.bluesadi.bluefriends.util.PlayerInventoryUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static cn.bluesadi.bluefriends.config.Message.*;

public class Person implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof  Player) {
            int size = args.length;
            Player player = (Player)sender;
            BFPlayer bfPlayer = BFPlayer.getBFPlayer(((Player) sender).getUniqueId());
            if(size >= 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    if(size >= 2) {
                        if(sender.hasPermission("bluefriends.person.set."+args[1].toLowerCase())) {
                            if (args[1].equalsIgnoreCase("email")) {
                                bfPlayer.setEmail(args[2]);
                                bfPlayer.sendMessageBox(SET_EMAIL_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("qq")) {
                                bfPlayer.setQQ(args[2]);
                                bfPlayer.sendMessageBox(SET_QQ_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("sex")) {
                                bfPlayer.setSex(args[2]);
                                bfPlayer.sendMessageBox(SET_SEX_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("nickname")) {
                                bfPlayer.setNickname(args[2]);
                                bfPlayer.sendMessageBox(SET_NICKNAME_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("head")) {
                                bfPlayer.setHead(args[2]);
                                bfPlayer.sendMessageBox(SET_HEAD_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("headBorder")) {
                                bfPlayer.setHeadBorder(args[2]);
                                bfPlayer.sendMessageBox(SET_HEAD_BORDER_SUCCESS);
                            } else if (args[1].equalsIgnoreCase("signature")) {
                                bfPlayer.setSignature(args[2]);
                                bfPlayer.sendMessageBox(SET_SIGNATURE_SUCCESS);
                            }
                        }else{
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    }else{
                        BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                    }
                }else if(args[0].equalsIgnoreCase("editBox")){
                    if(sender.hasPermission("bluefriends.editbox")) {
                        if(size >= 4) {
                            BluesGui bluesGui = GuiManager.getInstance().createGui("edit_box", player);
                            bluesGui.getComponents().forEach(component -> {
                                Function<String, String> f = component.getPlaceholderFunction();
                                if(component instanceof BluesTextField){
                                    List<String> commands = new ArrayList<>();
                                    for(int i = 3;i < size;i++){
                                        commands.add(args[i].replaceAll("%blank%"," ").replaceAll("%%"," "));
                                    }
                                    ((BluesTextField)component).setCommands(commands);
                                }
                                component.setPlaceholderFunction(raw -> f.apply(raw).replaceAll("%title%", args[1])
                                        .replaceAll("%infor%",args[2]));
                            });
                            bluesGui.open();
                        }else{
                            bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                        }
                    }else{
                        bfPlayer.sendMessageBox(NO_PERMISSION);
                    }
                }else if(args[0].equalsIgnoreCase("mail")){
                    if(size >= 3 && !bfPlayer.hasMail(args[2])){
                        return false;
                    }
                    if(args[1].equalsIgnoreCase("view")){
                        if(size >= 3) {
                            if(sender.hasPermission("bluefreinds.mail.view")) {
                                String uuid = args[2];
                                if (BFUtil.existsMail(uuid)) {
                                    Mail mail = new Mail(UUID.fromString(uuid));
                                    BluesGui bluesGui = GuiManager.getInstance().createGui("mail", player);
                                    bluesGui.getComponents().forEach(component -> {
                                        Function<String, String> f = component.getPlaceholderFunction();
                                        component.setPlaceholderFunction(raw -> f.apply(raw).replaceAll("%mail_uuid%", mail.getUUID().toString())
                                                .replaceAll("%mail_date%", mail.getDate())
                                                .replaceAll("%mail_subject%", mail.getSubject())
                                                .replaceAll("%mail_content%", mail.getContent())
                                                .replaceAll("%mail_read%", mail.isRead(bfPlayer) ? Config.READ : Config.NOT_READ)
                                                .replaceAll("%mail_items%", String.valueOf(mail.hasGotItems(bfPlayer) ? 0 : mail.getItems().size())));
                                    });
                                    mail.setRead(bfPlayer);
                                    bluesGui.open();
                                } else {
                                    bfPlayer.sendMessageBox(MAIL_NOT_EXISTS);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }else{
                            bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                        }
                    }else if(args[1].equalsIgnoreCase("delete")){
                        if(size >= 3){
                            if(sender.hasPermission("bluefriends.mail.delete")) {
                                String uuid = args[2];
                                if (BFUtil.existsMail(uuid)) {
                                    bfPlayer.deleteMail(UUID.fromString(uuid));
                                } else {
                                    bfPlayer.sendMessageBox(MAIL_NOT_EXISTS);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }
                    }else if(args[1].equalsIgnoreCase("read")){
                        if(size >= 3){
                            if(sender.hasPermission("bluefriends.mail.read")) {
                                String uuid = args[2];
                                if (BFUtil.existsMail(uuid)) {
                                    Mail mail = new Mail(UUID.fromString(uuid));
                                    mail.setRead(bfPlayer);
                                } else {
                                    bfPlayer.sendMessageBox(MAIL_NOT_EXISTS);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }
                    }else if(args[1].equalsIgnoreCase("items")){
                        if(size >= 3){
                            if(sender.hasPermission("bluefriends.mail.items")) {
                                String uuid = args[2];
                                if (BFUtil.existsMail(uuid)) {
                                    Mail mail = new Mail(UUID.fromString(uuid));
                                    if(!mail.hasGotItems(bfPlayer)) {
                                        List<ItemStack> items = mail.getItems();
                                        if (PlayerInventoryUtils.canPut(player, items)) {
                                            items.forEach(item -> player.getInventory().addItem(item));
                                            mail.setGotItems(bfPlayer);
                                            bfPlayer.sendMessageBox(GET_ITEMS_SUCCESS);
                                        } else {
                                            bfPlayer.sendMessageBox(NO_ENOUGH_SPACE);
                                        }
                                    }else{
                                        bfPlayer.sendMessageBox(GET_ITEMS_FAIL);
                                    }
                                } else {
                                    bfPlayer.sendMessageBox(MAIL_NOT_EXISTS);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }
                    }else{
                        bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                    }
                }else if(args[0].equalsIgnoreCase("message")){
                    if(args.length >= 2){
                        if(args[1].equalsIgnoreCase("view")){
                            if(sender.hasPermission("bluefriends.message.view")){
                                if(args.length >= 3){
                                    String uuid = args[2];
                                    if(BFUtil.existsSystemMessage(uuid)) {
                                        bfPlayer.sendMessageBox(new SystemMessage(UUID.fromString(uuid)).getContent());
                                    }else{
                                        bfPlayer.sendMessageBox(SYSTEM_MESSAGE_NOT_EXISTS);
                                    }
                                }else {
                                    bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }else if(args[1].equalsIgnoreCase("delete")){
                            if(sender.hasPermission("bluefriends.message.delete")) {
                                if (args.length >= 3) {
                                    String uuid = args[2];
                                    if (BFUtil.existsSystemMessage(uuid)) {
                                        bfPlayer.deleteMessage(UUID.fromString(uuid));
                                    } else {
                                        bfPlayer.sendMessageBox(SYSTEM_MESSAGE_NOT_EXISTS);
                                    }
                                } else {
                                    bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }
                    }else{
                        bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                    }
                }
            }else{
                bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
            }
        }else{
            sender.sendMessage(PLAYER_ONLY);
        }
        return false;
    }
}
