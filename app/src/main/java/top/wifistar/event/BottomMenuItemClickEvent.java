package top.wifistar.event;

import top.wifistar.customview.BottomMenuView;

/**
 * Created by hasee on 2017/3/27.
 */

public class BottomMenuItemClickEvent {
    public int menuItem = BottomMenuView.Item_Chats;

    public BottomMenuItemClickEvent(int menuItem) {
        this.menuItem = menuItem;
    }
}
