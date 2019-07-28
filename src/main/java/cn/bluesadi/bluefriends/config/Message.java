package cn.bluesadi.bluefriends.config;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Message {

    private static FileConfiguration message;
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



    public static void load(){
        BlueFriends.getInstance().saveResource("message.yml",false);
        message = YamlConfiguration.loadConfiguration(new File(BlueFriends.getInstance().getDataFolder(),"message.yml"));
        NO_PERMISSION = BFUtil.color(message.getString("no_permission","&c发生啥事?"));
        ARGUMENTS_WRONG = BFUtil.color(message.getString("arguments_wrong","&c发生啥事?"));
        PLAYER_ONLY = BFUtil.color(message.getString("player_only","&c发生啥事?"));
        PLAYER_NOT_EXISTS = BFUtil.color(message.getString("player_not_exists","&c发生啥事?"));
        RELOAD_COMPLETED = BFUtil.color(message.getString("reload_completed","&c发生啥事?"));
        HAS_BEEN_REQUESTER = BFUtil.color(message.getString("has_been_requester","&c发生啥事?"));
        HAS_BEEN_FRIEND = BFUtil.color(message.getString("has_been_friend","&c发生啥事?"));
        SEND_FRIEND_REQUEST = BFUtil.color(message.getString("send_friend_request","&c发生啥事?"));
        RECEIVE_FRIEND_REQUEST = BFUtil.color(message.getString("receive_friend_request","&c发生啥事?"));
        PLAYER_NOT_REQUEST = BFUtil.color(message.getString("player_not_request","&c发生啥事?"));
        ACCEPT_FRIEND_REQUEST = BFUtil.color(message.getString("accept_friend_request","&c发生啥事?"));
        REJECT_FRIEND_REQUEST = BFUtil.color(message.getString("reject_friend_request","&c发生啥事?"));
        FRIEND_REQUEST_ACCEPTED = BFUtil.color(message.getString("friend_request_accepted","&c发生啥事?"));
        FRIEND_REQUEST_REJECTED = BFUtil.color(message.getString("friend_request_rejected","&c发生啥事?"));
        DELETE_PLAYER_NO_FRIEND = BFUtil.color(message.getString("delete_player_no_friend","&c发生啥事?"));
        DELETE_FRIEND_SUCCESS = BFUtil.color(message.getString("delete_friend_success","&c发生啥事?"));
        DELETED_BY_FRIEND = BFUtil.color(message.getString("deleted_by_friend","&c发生啥事?"));
        GUI_NOT_FOUND = BFUtil.color(message.getString("gui_not_found","&c发生啥事?"));
        SEND_SYSTEM_MESSAGE_SUCCESS = BFUtil.color(message.getString("send_system_message_success","&c发生啥事?"));
        MAIL_NOT_EXISTS = BFUtil.color(message.getString("mail_not_exists","&c发生啥事?"));
        SYSTEM_MESSAGE_NOT_EXISTS = BFUtil.color(message.getString("system_message_not_exists","&c发生啥事?"));
        RECEIVE_SYSTEM_MESSAGE = BFUtil.color(message.getString("receive_system_message","&c发生啥事?"));
        SEND_MAIL_SUCCESS = BFUtil.color(message.getString("send_mail_success","&c发生啥事?"));
        RECEIVE_MAIL = BFUtil.color(message.getString("receive_mail","&c发生啥事?"));
        CREATE_FAKE_PLAYER_SUCCESS = BFUtil.color(message.getString("create_fake_player_success","&c发生啥事?"));
        REMOVE_FAKE_PLAYER_SUCCESS = BFUtil.color(message.getString("remove_fake_player_success","&c发生啥事?"));
        FAKE_PLAYER_EXISTED = BFUtil.color(message.getString("fake_player_existed","发生啥事?"));
        FAKE_PLAYER_NOT_EXISTED = BFUtil.color(message.getString("fake_player_not_existed","发生啥事?"));
        SET_EMAIL_SUCCESS = BFUtil.color(message.getString("set_email_success","发生啥事?"));
        SET_QQ_SUCCESS = BFUtil.color(message.getString("set_qq_success","发生啥事?"));
        SET_SEX_SUCCESS = BFUtil.color(message.getString("set_sex_success","发生啥事?"));
        SET_HEAD_SUCCESS = BFUtil.color(message.getString("set_head_success","发生啥事?"));
        SET_HEAD_BORDER_SUCCESS = BFUtil.color(message.getString("set_headborder_success","发生啥事?"));
        SET_NICKNAME_SUCCESS = BFUtil.color(message.getString("set_nickname_success","发生啥事?"));
        SET_SIGNATURE_SUCCESS = BFUtil.color(message.getString("set_signature_success","发生啥事?"));
        CANT_APPLY_SELF = BFUtil.color(message.getString("cant_apply_self","发生啥事?"));
        NO_ENOUGH_SPACE = BFUtil.color(message.getString("no_enough_space","发生啥事?"));
        GET_ITEMS_SUCCESS = BFUtil.color(message.getString("get_items_success","发生啥事?"));
        GET_ITEMS_FAIL = BFUtil.color(message.getString("get_items_fail","发生啥事?"));
        SAVE_MAIL_SUCCESS = BFUtil.color(message.getString("save_mail_success","发生啥事?"));
        CONDITION_WRONG = BFUtil.color(message.getString("condition_wrong","发生啥事?"));
    }
}
