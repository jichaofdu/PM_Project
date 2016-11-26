package projectmanager.dada.model;

public enum SexType {

    NONE   (0,   "未填写"),
    MALE   (1,   "男"),
    FEMALE (2,   "女"),
    OTHER  (3,   "其它");

    private int code;
    private String name;

    SexType(int code, String name) {
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


