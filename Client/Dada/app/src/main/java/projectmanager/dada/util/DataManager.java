package projectmanager.dada.util;

import projectmanager.dada.model.User;

/**
 * 相当于Session。从服务器获取到的信息都存在本类中。
 */
public class DataManager {

    private static DataManager dm;

    //当前登录的用户
    private User currentUser;

    private DataManager(){
        currentUser = null;
    }

    public static DataManager getInstance(){
        if(dm == null){
            dm = new DataManager();
        }
        return dm;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
