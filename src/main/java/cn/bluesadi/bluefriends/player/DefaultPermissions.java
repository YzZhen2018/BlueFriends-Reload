package cn.bluesadi.bluefriends.player;


import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class DefaultPermissions {
    
    private static final String[] permissions = new String[]{
            "bluefriends.person.set.qq",
            "bluefriends.person.set.email",
            "bluefriends.person.set.sex",
            "bluefriends.person.set.nickname",
            "bluefriends.person.set.head",
            "bluefriends.person.set.headborder",
            "bluefriends.person.set.signature",
            "bluefriends.person.editbox",
            "bluefriends.mail.view",
            "bluefriends.mail.delete",
            "bluefriends.mail.read",
            "bluefriends.mail.items",
            "bluefriends.message.view",
            "bluefriends.message.delete",
            "bluefriends.friend.apply",
            "bluefriends.friend.delete",
            "bluefriends.friend.query",
            "bluefriends.friend.accept",
            "bluefriends.friend.reject",
            "bluefriends.fakeplayer.apply",
            "bluefriends.open.*"
    };

    public static int register(){
        for(String permission : permissions){
            new Permission(permission).setDefault(PermissionDefault.TRUE);
        }
        return permissions.length;
    }
    
}
