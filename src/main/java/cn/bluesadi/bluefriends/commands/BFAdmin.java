package cn.bluesadi.bluefriends.commands;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailAttributes;
import cn.bluesadi.bluefriends.mail.MailEditor;
import cn.bluesadi.bluefriends.mail.ReceiveCondition;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFCalendar;
import cn.bluesadi.bluefriends.util.BFUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.config.Message.*;

public class BFAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("maileditor")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
                    if(args.length == 1) {
                        if (player.hasPermission("bluefriends.maileditor")) {
                            MailEditor mailEditor = BlueFriends.getMailEditor();
                            BluesGui mailEditorGui = BlueFriends.getGuiManager().createGui("mail_editor", player);
                            mailEditorGui.getComponents().forEach(bluesComponent -> bluesComponent.setPlaceholderFunction(raw ->
                                    PlaceholderAPI.setPlaceholders(mailEditorGui.getViewer(), raw)
                                            .replaceAll("%mail_subject%", mailEditor.getSubject())
                                            .replaceAll("%mail_content%", mailEditor.getContent())
                                            .replaceAll("%mail_items%", String.valueOf(mailEditor.getItems().size()))));
                            mailEditorGui.open();
                        } else {
                            bfPlayer.sendMessageBox(NO_PERMISSION);
                        }
                    }else{
                        if (args[1].equalsIgnoreCase("set")) {
                            if(args.length >= 3) {
                                if(sender.hasPermission("bluefriends.maileditor.set."+args[1].toLowerCase())) {
                                    if (args[2].equalsIgnoreCase("subject")) {
                                        if(args.length >= 4) {
                                            BlueFriends.getMailEditor().setSubject(args[3]);
                                        }else{
                                            bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                                        }
                                    } else if (args[2].equalsIgnoreCase("content")) {
                                        if(args.length >= 4) {
                                            BlueFriends.getMailEditor().setContent(args[3]);
                                        }else{
                                            bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                                        }
                                    } else if (args[2].equalsIgnoreCase("items")) {
                                        Inventory inv = Bukkit.createInventory(null,54,"编辑邮件附件");
                                        inv.setContents(BlueFriends.getMailEditor().getItems().toArray(new ItemStack[0]));
                                        player.openInventory(inv);
                                    }
                                }else{
                                    bfPlayer.sendMessageBox(NO_PERMISSION);
                                }
                            }else{
                                BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                            }
                        }else if(args[1].equalsIgnoreCase("save")){
                            if(sender.hasPermission("bluefriends.maileditor.save")) {
                                BlueFriends.getMailEditor().save();
                                bfPlayer.sendMessageBox(SAVE_MAIL_SUCCESS);
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }else if(args[1].equalsIgnoreCase("load")){
                            if(sender.hasPermission("bluefriends.maileditor.load")) {
                                if(args.length >= 3){
                                    if(BFUtil.existsMail(args[2])) {
                                        BlueFriends.getMailEditor().load(UUID.fromString(args[2]));
                                    }else{
                                        bfPlayer.sendMessageBox(MAIL_NOT_EXISTS);
                                    }
                                }else{
                                    bfPlayer.sendMessageBox(ARGUMENTS_WRONG);
                                }
                            }else{
                                bfPlayer.sendMessageBox(NO_PERMISSION);
                            }
                        }
                    }
                }else{
                    BFUtil.sendMessageBox(sender,PLAYER_ONLY);
                }
            }else if(args[0].equalsIgnoreCase("mail")){
                if(args.length >= 2){
                    if(args[1].equalsIgnoreCase("sendTo")){
                        if(sender.hasPermission("bluefriends.mail.sendto")) {
                            if (args.length >= 4) {
                                if (BFUtil.existsPlayer(args[2])) {
                                    if(BFUtil.existsMail(args[3])){
                                        BFPlayer bfPlayer = BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(args[2]));
                                        Mail mail = new Mail(UUID.fromString(args[3]));
                                        mail.setDate(BFCalendar.getDate(Config.DATE_FORMAT));
                                        bfPlayer.addMail(mail.getUUID());
                                        bfPlayer.sendMessage(RECEIVE_MAIL);
                                        BFUtil.sendMessageBox(sender,SEND_MAIL_SUCCESS);
                                    }else{
                                        BFUtil.sendMessageBox(sender,MAIL_NOT_EXISTS);
                                    }
                                } else {
                                    BFUtil.sendMessageBox(sender, PLAYER_NOT_EXISTS);
                                }
                            } else {
                                BFUtil.sendMessageBox(sender, ARGUMENTS_WRONG);
                            }
                        }else{
                            BFUtil.sendMessageBox(sender,NO_PERMISSION);
                        }
                    }else if(args[1].equalsIgnoreCase("sendAll")){
                        if(sender.hasPermission("bluefriends.mail.sendall")) {
                            if (args.length >= 3) {
                                if(BFUtil.existsMail(args[2])){
                                    BlueFriends.getBFPlayers().forEach(bfPlayer -> {
                                        Mail mail = new Mail(UUID.fromString(args[2]));
                                        mail.setDate(BFCalendar.getDate(Config.DATE_FORMAT));
                                        bfPlayer.addMail(mail.getUUID());
                                        bfPlayer.sendMessage(RECEIVE_MAIL);
                                    });
                                    BFUtil.sendMessageBox(sender,SEND_MAIL_SUCCESS);
                                }else{
                                    BFUtil.sendMessageBox(sender,MAIL_NOT_EXISTS);
                                }
                            } else {
                                BFUtil.sendMessageBox(sender, ARGUMENTS_WRONG);
                            }
                        }else{
                            BFUtil.sendMessageBox(sender,NO_PERMISSION);
                        }
                    }else if(args[1].equalsIgnoreCase("sendIf")){
                        if(sender.hasPermission("bluefriends.mail.sendif")) {
                            if (args.length >= 4) {
                                if(BFUtil.existsMail(args[3])){
                                    try {
                                        ReceiveCondition receiveCondition = new ReceiveCondition(args[2]);
                                        BlueFriends.getBFPlayers().forEach(bfPlayer -> {
                                            if (receiveCondition.test(bfPlayer)) {
                                                Mail mail = new Mail(UUID.fromString(args[3]));
                                                mail.setDate(BFCalendar.getDate(Config.DATE_FORMAT));
                                                bfPlayer.addMail(mail.getUUID());
                                                bfPlayer.sendMessage(RECEIVE_MAIL);
                                            }
                                        });
                                        BFUtil.sendMessageBox(sender, SEND_MAIL_SUCCESS);
                                    }catch (Exception e){
                                        BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                                    }
                                }else{
                                    BFUtil.sendMessageBox(sender,MAIL_NOT_EXISTS);
                                }
                            } else {
                                BFUtil.sendMessageBox(sender, CONDITION_WRONG);
                            }
                        }else{
                            BFUtil.sendMessageBox(sender,NO_PERMISSION);
                        }
                    }else if(args[1].equalsIgnoreCase("delete")){
                        if(sender.hasPermission("bluefriends.mail.delete")){
                            if(args.length >= 3){
                                String uuid = args[2];
                                if(BFUtil.existsMail(uuid)){
                                    new Mail(UUID.fromString(uuid)).delete();
                                }else{
                                    BFUtil.sendMessageBox(sender,MAIL_NOT_EXISTS);
                                }
                            }else{
                                BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                            }
                        }else{
                            BFUtil.sendMessageBox(sender,NO_PERMISSION);
                        }
                    }
                }else{
                    BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                }
            }else if(args[0].equalsIgnoreCase("messageBox")){
                if(sender.hasPermission("bluefriends.messagebox")) {
                    if (args.length >= 3) {
                        if(BFUtil.existsPlayer(args[1])){
                            BFPlayer target = BFPlayer.getBFPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                            target.sendMessageBox(args[2]);
                            BFUtil.sendMessageBox(sender,SEND_SYSTEM_MESSAGE_SUCCESS.replaceAll("%player%",args[1]));
                        }
                    } else {
                        BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                    }
                }else{
                    BFUtil.sendMessageBox(sender,NO_PERMISSION);
                }
            }else if(args[0].equalsIgnoreCase("message")){
                if(sender.hasPermission("bluefriends.message")) {
                    if (args.length >= 3) {
                        if(BFUtil.existsPlayer(args[1])){
                            BFPlayer target = BFPlayer.getBFPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                            target.sendMessage(args[2]);
                            BFUtil.sendMessageBox(sender,SEND_SYSTEM_MESSAGE_SUCCESS.replaceAll("%player%",args[1]));
                        }
                    } else {
                        BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                    }
                }else{
                    BFUtil.sendMessageBox(sender,NO_PERMISSION);
                }
            }else if(args[0].equalsIgnoreCase("fakeplayer")){
                if (args.length >= 2) {
                    if(args[1].equalsIgnoreCase("create")){
                        if(sender.hasPermission("bluefriends.fakeplayer.create")){
                            if(args.length >= 4){
                                if(!BFUtil.existsFakePlayer(args[2])) {
                                    String name = args[2];
                                    String nickName = args[3];
                                    BFPlayer.createFakePlayer(name, nickName);
                                    BFUtil.sendMessageBox(sender, CREATE_FAKE_PLAYER_SUCCESS);
                                }else{
                                    BFUtil.sendMessageBox(sender,FAKE_PLAYER_EXISTED);
                                }
                            }else{
                                BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                            }
                        }else{
                            BFUtil.sendMessageBox(sender,NO_PERMISSION);
                        }
                    }else if(args[1].equalsIgnoreCase("remove")){
                        if(sender.hasPermission("bluefriends.fakeplayer.remove")){
                            if(args.length >= 3){
                                String name = args[2];
                                if(BFUtil.existsFakePlayer(name)) {
                                    Row fakeRow = BFDatabase.getInstance().getFakePlayerTable().getRow(args[2]);
                                    String uuid = fakeRow.getValue("uuid").getString();
                                    Row row = BFDatabase.getInstance().getPlayerTable().getRow(uuid);
                                    if (row.existsRow()) {
                                        if (BFPlayer.getBFPlayer(UUID.fromString(uuid)).isFakePlayer()) {
                                            row.delete();
                                            fakeRow.delete();
                                            BFUtil.sendMessageBox(sender, REMOVE_FAKE_PLAYER_SUCCESS);
                                        } else {
                                            BFUtil.sendMessageBox(sender, ARGUMENTS_WRONG);
                                        }
                                    } else {
                                        BFUtil.sendMessageBox(sender, ARGUMENTS_WRONG);
                                    }
                                }else{
                                    BFUtil.sendMessageBox(sender,FAKE_PLAYER_NOT_EXISTED);
                                }
                            }
                        }
                    }
                }else{
                    BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
                }
            }
        }else{
            BFUtil.sendMessageBox(sender,ARGUMENTS_WRONG);
        }
        return false;
    }
}
