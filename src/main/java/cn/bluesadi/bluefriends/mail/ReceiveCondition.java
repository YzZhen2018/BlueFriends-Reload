package cn.bluesadi.bluefriends.mail;

import cn.bluesadi.bluefriends.player.BFPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import java.util.function.Predicate;

public class ReceiveCondition {

    private String left;
    private String right;
    private Predicate<BFPlayer> testFunction;

    public ReceiveCondition(String condition){
        if(condition.matches(".+(\\.matches:).+")){
            String[] args = condition.split("%matches%",2);
            testFunction = bfPlayer -> {
                left = PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0]);
                right = PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1]);
                return left.matches(right);
            };
        }else if(condition.matches("[0-9]+(>)[0-9]+")){
            String[] args = condition.split(">",2);
            testFunction = bfPlayer -> {
                int leftValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0])).intValue();
                int rightValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1])).intValue();
                return leftValue > rightValue;
            };
        }else if(condition.matches("[0-9]+(>=)[0-9]+")){
            String[] args = condition.split(">",2);
            testFunction = bfPlayer -> {
                int leftValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0])).intValue();
                int rightValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1])).intValue();
                return leftValue >= rightValue;
            };
        }else if(condition.matches("[0-9]+(<)[0-9]+")){
            String[] args = condition.split(">",2);
            testFunction = bfPlayer -> {
                int leftValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0])).intValue();
                int rightValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1])).intValue();
                return leftValue < rightValue;
            };
        }else if(condition.matches("[0-9]+(<=)[0-9]+")){
            String[] args = condition.split(">",2);
            testFunction = bfPlayer -> {
                int leftValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0])).intValue();
                int rightValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1])).intValue();
                return leftValue <= rightValue;
            };
        }else if(condition.matches("[0-9]+(=)[0-9]+")){
            String[] args = condition.split(">",2);
            testFunction = bfPlayer -> {
                int leftValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[0])).intValue();
                int rightValue = Double.valueOf(PlaceholderAPI.setPlaceholders(bfPlayer.getOfflinePlayer(),args[1])).intValue();
                return leftValue == rightValue;
            };
        }else{
            throw new IllegalArgumentException("ReceiveCondition参数错误!");
        }
    }

    public boolean test(BFPlayer bfPlayer){
        return testFunction.test(bfPlayer);
    }
}
