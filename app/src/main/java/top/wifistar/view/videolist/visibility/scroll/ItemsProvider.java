package top.wifistar.view.videolist.visibility.scroll;


import top.wifistar.view.videolist.visibility.items.ListItem;

/**
 * This interface is used by
 * Using this class to get
 *
 * @author Wayne
 */
public interface ItemsProvider {

    ListItem getListItem(int position);

    int listItemSize();

}
