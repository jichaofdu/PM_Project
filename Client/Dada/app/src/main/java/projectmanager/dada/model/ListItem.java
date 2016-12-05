package projectmanager.dada.model;

import java.util.Map;

/**
 * Created by JScarlet on 2016/12/5.
 */
public class ListItem {
    public int type;
    public Map<Integer, ?> map;
    public ListItem(int type, Map<Integer, ?> map){
        this.map = map;
        this.type = type;
    }
}
