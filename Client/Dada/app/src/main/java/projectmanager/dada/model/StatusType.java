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

    public static String getTypeBySexId(int statusId){
        StatusType[] statusTypes = StatusType.values();
        for(StatusType status : statusTypes){
            if(status.getCode() == statusId){
                return status.getName();
            }
        }
        return "未知状态";
    }
}
