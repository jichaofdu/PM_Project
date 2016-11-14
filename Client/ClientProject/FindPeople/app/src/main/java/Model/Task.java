package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jichao on 2016/11/4.
 */

public class Task {

    private int taskId;
    private String title;
    private String description;
    private int userId;
    private Date publishedTime;
    private Date deadline;
    private int addressId;
    private Location location;
    private ArrayList<String> tags;


}
