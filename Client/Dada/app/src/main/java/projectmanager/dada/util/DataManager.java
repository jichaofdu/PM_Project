package projectmanager.dada.util;

import java.util.HashMap;
import projectmanager.dada.model.Task;
import projectmanager.dada.model.User;

/**
 * 相当于Session。从服务器获取到的信息都存在本类中。
 */
public class DataManager {

    private static DataManager dm;

    //登录失败之后系统返回的信息
    private String loginErrorMesssage;

    //注册失败之后系统返回的信息
    private String registerErrorMessage;

    //当前处于登陆状态的用户
    private User currentUser;

    //用户自己发布的任务列表。Entry中Integer是taskId。
    private HashMap<Integer,Task> myPublishList;

    //用户自己接受的任务列表。Entry中Integer是taskId。
    private HashMap<Integer,Task> myAcceptList;

    //用户附近的任务列表。Entry中Integer是taskId。
    private HashMap<Integer,Task> myNearbyList;

    //新任务
    private Task newTask;

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

    public String getRegisterErrorMessage() {
        return registerErrorMessage;
    }

    public void setRegisterErrorMessage(String registerErrorMessage) {
        this.registerErrorMessage = registerErrorMessage;
    }

    public String getLoginErrorMesssage() {
        return loginErrorMesssage;
    }

    public void setLoginErrorMesssage(String loginErrorMesssage) {
        this.loginErrorMesssage = loginErrorMesssage;
    }

    public Task getNewTask() {
        return newTask;
    }

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }
}
