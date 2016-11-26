package projectmanager.dada.model;

public enum StatusType {

    CLOSED   (-1,   "已取消"),
    OPEN     (1,    "开放申请"),
    GOINGON  (2,    "进行中"),
    FINISHED (3,    "已完成");

    private int code;
    private String name;

    StatusType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName() {
        return name;
    }





}
