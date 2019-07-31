package cn.bluesadi.bluefriends.config;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {

    private static FileConfiguration message;

    public static String VERSION;

    public static String NO_PERMISSION;
    public static String ARGUMENTS_WRONG;
    public static String PLAYER_ONLY;
    public static String PLAYER_NOT_EXISTS;
    public static String RELOAD_COMPLETED;
    public static String MAIL_NOT_EXISTS;
    public static String SYSTEM_MESSAGE_NOT_EXISTS;
    public static String SAVE_MAIL_SUCCESS;
    public static String CONDITION_WRONG;

    public static String CANT_APPLY_SELF;
    public static String HAS_BEEN_REQUESTER;
    public static String HAS_BEEN_FRIEND;
    public static String SEND_FRIEND_REQUEST;
    public static String RECEIVE_FRIEND_REQUEST;
    public static String PLAYER_NOT_REQUEST;
    public static String ACCEPT_FRIEND_REQUEST;
    public static String REJECT_FRIEND_REQUEST;
    public static String FRIEND_REQUEST_ACCEPTED;
    public static String FRIEND_REQUEST_REJECTED;
    public static String DELETE_PLAYER_NO_FRIEND;
    public static String DELETE_FRIEND_SUCCESS;
    public static String DELETED_BY_FRIEND;

    public static String GUI_NOT_FOUND;

    public static String SEND_SYSTEM_MESSAGE_SUCCESS;
    public static String RECEIVE_SYSTEM_MESSAGE;
    public static String SEND_MAIL_SUCCESS;
    public static String RECEIVE_MAIL;
    public static String NO_ENOUGH_SPACE;
    public static String GET_ITEMS_SUCCESS;
    public static String GET_ITEMS_FAIL;
    public static String CREATE_FAKE_PLAYER_SUCCESS;
    public static String REMOVE_FAKE_PLAYER_SUCCESS;
    public static String FAKE_PLAYER_EXISTED;
    public static String FAKE_PLAYER_NOT_EXISTED;

    public static String SET_EMAIL_SUCCESS;
    public static String SET_QQ_SUCCESS;
    public static String SET_HEAD_SUCCESS;
    public static String SET_HEAD_BORDER_SUCCESS;
    public static String SET_SEX_SUCCESS;
    public static String SET_NICKNAME_SUCCESS;
    public static String SET_SIGNATURE_SUCCESS;
    
    public static List<String> JOIN;


    private static String getString(String path){
        return BFUtil.color(message.getString(path,"<无法在message.yml中找到"+path+"选项>"));
    }

    private static List<String> getStringList(String path){
        return BFUtil.color(message.getStringList(path));
    }

    public static void load(){
        BlueFriends.getInstance().saveResource("message.yml",false);
        message = YamlConfiguration.loadConfiguration(new File(BlueFriends.getInstance().getDataFolder(),"message.yml"));
        VERSION = message.getString("version","1.0");
        NO_PERMISSION = getString("no_permission");
        ARGUMENTS_WRONG = getString("arguments_wrong");
        PLAYER_ONLY = getString("player_only");
        PLAYER_NOT_EXISTS = getString("player_not_exists");
        RELOAD_COMPLETED = getString("reload_completed");
        HAS_BEEN_REQUESTER = getString("has_been_requester");
        HAS_BEEN_FRIEND = getString("has_been_friend");
        SEND_FRIEND_REQUEST = getString("send_friend_request");
        RECEIVE_FRIEND_REQUEST = getString("receive_friend_request");
        PLAYER_NOT_REQUEST = getString("player_not_request");
        ACCEPT_FRIEND_REQUEST = getString("accept_friend_request");
        REJECT_FRIEND_REQUEST = getString("reject_friend_request");
        FRIEND_REQUEST_ACCEPTED = getString("friend_request_accepted");
        FRIEND_REQUEST_REJECTED = getString("friend_request_rejected");
        DELETE_PLAYER_NO_FRIEND = getString("delete_player_no_friend");
        DELETE_FRIEND_SUCCESS = getString("delete_friend_success");
        DELETED_BY_FRIEND = getString("deleted_by_friend");
        GUI_NOT_FOUND = getString("gui_not_found");
        SEND_SYSTEM_MESSAGE_SUCCESS = getString("send_system_message_success");
        MAIL_NOT_EXISTS = getString("mail_not_exists");
        SYSTEM_MESSAGE_NOT_EXISTS = getString("system_message_not_exists");
        RECEIVE_SYSTEM_MESSAGE = getString("receive_system_message");
        SEND_MAIL_SUCCESS = getString("send_mail_success");
        RECEIVE_MAIL = getString("receive_mail");
        CREATE_FAKE_PLAYER_SUCCESS = getString("create_fake_player_success");
        REMOVE_FAKE_PLAYER_SUCCESS = getString("remove_fake_player_success");
        FAKE_PLAYER_EXISTED = getString("fake_player_existed");
        FAKE_PLAYER_NOT_EXISTED = getString("fake_player_not_existed");
        SET_EMAIL_SUCCESS = getString("set_email_success");
        SET_QQ_SUCCESS = getString("set_qq_success");
        SET_SEX_SUCCESS = getString("set_sex_success");
        SET_HEAD_SUCCESS = getString("set_head_success");
        SET_HEAD_BORDER_SUCCESS = getString("set_headborder_success");
        SET_NICKNAME_SUCCESS = getString("set_nickname_success");
        SET_SIGNATURE_SUCCESS = getString("set_signature_success");
        CANT_APPLY_SELF = getString("cant_apply_self");
        NO_ENOUGH_SPACE = getString("no_enough_space");
        GET_ITEMS_SUCCESS = getString("get_items_success");
        GET_ITEMS_FAIL = getString("get_items_fail");
        SAVE_MAIL_SUCCESS = getString("save_mail_success");
        CONDITION_WRONG = getString("condition_wrong");
        JOIN = getStringList("join");
    }
}
