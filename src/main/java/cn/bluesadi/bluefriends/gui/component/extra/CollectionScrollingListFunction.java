package cn.bluesadi.bluefriends.gui.component.extra;

@FunctionalInterface
public interface CollectionScrollingListFunction <T>{

    String setPlaceholder(String raw,T element);
}
