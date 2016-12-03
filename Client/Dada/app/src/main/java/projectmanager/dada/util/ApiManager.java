package projectmanager.dada.util;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projectmanager.dada.model.Location;
import projectmanager.dada.model.Task;
import projectmanager.dada.model.User;

/**
 * 和服务器相关联的相关接口均在此类中完成。
 */
public class ApiManager {

    private final String scheme = "https";
    private final String host = "relay.nxtsysx.net";
    private static ApiManager apiManager;

    /**
     * 本函数书用于像应用层提供登录的接口。应用层调取此方法并得到返回的对象。
     * 注意：如果任一步骤出错，将返回null
     * @param phone    登录所必需的账号
     * @param password 登录所必须的密码
     * @return         如果任一步骤出错，返回null。否则返回服务器返回的对象。
     */
    public User handleLogin(String phone, String password){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/login/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("phone", phone));
            postParameters.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if (result.equals("failed")) {
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Login process. Login fail. Error Message:" + error);
                    return null;
                } else {
                    String userString = resultJson.getString("user");
                    System.out.println(userString);
                    JSONObject userJson = new JSONObject(userString);
                    return parseUser(userJson);
                }
            } else {
                System.out.println("[Error] Login Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 本函数用于向应用层提供注册的接口。应用层调取此方法并得到返回的对象。
     * 注意：如果任一步骤出错，将返回null
     * @param phone     注册所必需的账号
     * @param username  注册所必需的用户名
     * @param password  注册所必需的密码
     * @return          如果任一步骤出错，返回null。否则返回服务器返回的对象。
     */
    public User handleRegister(String phone,String username,String password){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/register/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("phone", phone));
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Register process. Register fail. Error Message:" + error);
                    return null;
                }else{
                    String userString = resultJson.getString("user");
                    System.out.println(userString);
                    JSONObject userJson = new JSONObject(userString);
                    return parseUser(userJson);
                }
            } else {
                System.out.println("[Error] Register Process. Status Code:" +
                                    response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 本函数用于提供修改密码的接口
     * @param userId      被修改密码的用户id
     * @param oldPassword 用户的原始密码
     * @param newPassword 用户的新密码
     * @return            返回一个字符串，succeed或者failed
     */
    public String handleChangePassword(int userId,String oldPassword,String newPassword){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/changePassword/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            postParameters.add(new BasicNameValuePair("oldPassword", oldPassword));
            postParameters.add(new BasicNameValuePair("newPassword", newPassword));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if (result.equals("succeed")) {
                    return "succeed";
                }else{
                    return resultJson.getString("error");
                }
            } else {
                System.out.println("[Error] Change Password Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return "failed";
            }
        }catch (JSONException e){
            e.printStackTrace();
            return "failed";
        }catch (IOException e){
            e.printStackTrace();
            return "failed";
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    /**
     * 本类适用于更新用户信息的接口。
     * @param userId   待更新的用户的id
     * @param username 待更新的用户的新用户名
     * @param sex      准备更新的性别
     * @param avator   头像
     * @param bio      用户的签名
     * @return         返回更新后的User对象。
     */
    public User handleUpdateProfile(int userId,String username,int sex,String avator,String bio){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/updateProfile/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("sex", "" + sex));
            postParameters.add(new BasicNameValuePair("avatar", "" + avator));
            postParameters.add(new BasicNameValuePair("bio", "" + bio));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Update Profile process. Update profile fail. Error Message:" + error);
                    return null;
                }else{
                    String userString = resultJson.getString("user");
                    System.out.println(userString);
                    JSONObject userJson = new JSONObject(userString);
                    return parseUser(userJson);
                }
            } else {
                System.out.println("[Error] Update profile Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过用户的id来获取用户对象本身。
     * @param userId 准备获取的用户的id
     * @return       成功则返回对象本身。否则返回Null
     */
    public User handleGetUserById(int userId){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/getUser/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Get User process. Get User profile fail. Error Message:" + error);
                    return null;
                }else{
                    String userString = resultJson.getString("user");
                    System.out.println(userString);
                    JSONObject userJson = new JSONObject(userString);
                    return parseUser(userJson);
                }
            } else {
                System.out.println("[Error] Update profile Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发布新任务的接口
     * @param title        任务标题
     * @param description  任务描述
     * @param userId       发布者id
     * @param deadline     截止时间
     * @param longitude    地点经纬度
     * @param latitude     地点经纬度
     * @param locationDscp 地点描述
     * @param tags         标签
     * @param credit       信用
     * @return             返回新创建的任务对象本身
     */
    private Task handlePublishTask(String title, String description, int userId, Date deadline, double longitude, double latitude, String locationDscp, String[] tags, int credit){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/publishTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("title", "" + title));
            postParameters.add(new BasicNameValuePair("description", description));
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            postParameters.add(new BasicNameValuePair("deadline", "" + deadline.toString()));
            postParameters.add(new BasicNameValuePair("longitude", "" + longitude));
            postParameters.add(new BasicNameValuePair("latitude", "" + latitude));
            postParameters.add(new BasicNameValuePair("locationDscp", "" + longitude));
            postParameters.add(new BasicNameValuePair("tags", "" + ""));
            postParameters.add(new BasicNameValuePair("credit", "" + credit));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Publish Task process. Error Message:" + error);
                    return null;
                }else{
                    String taskString = resultJson.getString("task");
                    System.out.println(taskString);
                    JSONObject taskJson = new JSONObject(taskString);
                    return parseTask(taskJson);
                }
            }else{
                System.out.println("[Error] Publish Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id来获取对应的Task对象
     * @param taskId 你想要获得的任务对象的id
     * @return       任意一步出错将返回null
     */
    private Task handleGetTaskById(int taskId){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/viewTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("taskId", "" + taskId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] View Task process. Error Message:" + error);
                    return null;
                }else{
                    String taskString = resultJson.getString("task");
                    System.out.println(taskString);
                    JSONObject taskJson = new JSONObject(taskString);
                    return parseTask(taskJson);
                }
            }else{
                System.out.println("[Error] View Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改任务信息的接口
     * @param taskId       任务id
     * @param title        标题
     * @param description  任务描述
     * @param userId       用户id
     * @param deadline     截止日期
     * @param longitude    经纬度
     * @param latitude     经纬度
     * @param locationDscp 地址描述*
     * @param tags         任务标签
     * @return             成功修改将返回对象，其它出错情况均返回null
     */
    private Task handleEditTask(int taskId,String title,String description,int userId,Date deadline,double longitude,double latitude,String locationDscp,String[] tags){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/editTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("taskId", "" + taskId));
            postParameters.add(new BasicNameValuePair("title", "" + title));
            postParameters.add(new BasicNameValuePair("description", "" + description));
            postParameters.add(new BasicNameValuePair("deadline", "" + deadline));
            postParameters.add(new BasicNameValuePair("longitude", "" + longitude));
            postParameters.add(new BasicNameValuePair("latitude", "" + latitude));
            postParameters.add(new BasicNameValuePair("locationDscp", "" + locationDscp));
            postParameters.add(new BasicNameValuePair("tags", "" + ""));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Edit Task process. Error Message:" + error);
                    return null;
                }else{
                    String taskString = resultJson.getString("task");
                    System.out.println(taskString);
                    JSONObject taskJson = new JSONObject(taskString);
                    return parseTask(taskJson);
                }
            }else{
                System.out.println("[Error] Edit Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 接受任务的接口
     * @param taskId 要接受的任务id
     * @param userId 将要接受任务的用户id
     * @return 如果报错或者没有正常返回200，则返回null
     *         正常返回但是fail，返回错误报告
     *         正常且成功，则会返回“success”
     */
    private String handleAcceptTask(int taskId,int userId){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/acceptTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("taskId", "" + taskId));
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Accept Task process. Error Message:" + error);
                    return error;
                }else{
                    return "success";
                }
            }else{
                System.out.println("[Error] Accept Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 完成某个任务的接口
     * @param taskId 已接受的任务id
     * @param userId 接受任务的用户id
     * @return 如果报错或者没有正常返回200，则返回null
     *         正常返回但是fail，返回错误报告
     *         正常且成功，则会返回“success”
     */
    private String handleDoneTask(int taskId,int userId){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/doneTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("taskId", "" + taskId));
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Done Task process. Error Message:" + error);
                    return error;
                }else{
                    return "success";
                }
            }else{
                System.out.println("[Error] Done Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取消某个任务的接口。
     * @param taskId 任务的id
     * @param userId 发起者用户的id
     * @return 如果报错或者没有正常返回200，则返回null
     *         正常返回但是fail，返回错误报告
     *         正常且成功，则会返回“success”
     */
    private String handleCancelTask(int taskId,int userId){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://relay.nxtsysx.net/cancelTask/");
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("taskId", "" + taskId));
            postParameters.add(new BasicNameValuePair("userId", "" + userId));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println(request.getURI().toASCIIString());
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                String str = convertStreamToString(is);
                JSONObject resultJson = new JSONObject(str);
                String result = resultJson.getString("result");
                if(result.equals("failed")){
                    String error = resultJson.getString("error");
                    System.out.println("[Error] Cancel Task process. Error Message:" + error);
                    return error;
                }else{
                    return "success";
                }
            }else{
                System.out.println("[Error] Cancel Task Process. Status Code:" +
                        response.getStatusLine().getStatusCode());
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解析Task数据
     */
    public Task parseTask(JSONObject taskJson) throws JSONException{
        String taskIdString = taskJson.getString("taskId");
        int taskId = Integer.parseInt(taskIdString);
        String titleString = taskJson.getString("title");
        String descriptionString = taskJson.getString("description");
        String publisherInfoString = taskJson.getString("publisher");
        JSONObject userJson = new JSONObject(publisherInfoString);
        User publisher = parseUser(userJson);
        String publishedTimeString = taskJson.getString("publishedTime");
        Date publishedTime = new Date(publishedTimeString);
        String deadlineString = taskJson.getString("deadline");
        Date deadlineTime = new Date(deadlineString);
        String locationInfoString = taskJson.getString("location");
        JSONObject locationJson = new JSONObject(locationInfoString);
        Location loacation = parseLocation(locationJson);
        String tagsInfoString = taskJson.getString("tags");
        String creditString = taskJson.getString("credit");
        int credit = Integer.parseInt(creditString);
        String statusString = taskJson.getString("status");
        int status = Integer.parseInt(statusString);
        String accepterString = taskJson.getString("accepter");
        JSONObject accepterJson = new JSONObject(accepterString);
        User accepter = parseUser(accepterJson);
        //todo tags和accepter暂时设置为null
        return new Task(taskId,titleString,descriptionString,publisher,publishedTime,deadlineTime,loacation,
                null,credit,status,null);
    }

    /**
     * 解析Locations数据
     */
    public Location parseLocation(JSONObject locationJson) throws  JSONException{
        String logitutdeString = locationJson.getString("longitude");
        double longitude = Double.parseDouble(logitutdeString);
        String latitudeString = locationJson.getString("latitude");
        double latitude = Double.parseDouble(latitudeString);
        String description = locationJson.getString("description");
        return new Location(0,longitude,latitude,description);
    }

    /**
     * 解析User数据
     */
    public User parseUser(JSONObject userJson) throws JSONException{
        String userIdString = userJson.getString("userId");
        int userId = Integer.parseInt(userIdString);
        String userPhoneString = userJson.getString("phone");
        String passwordString = "DefaultPassword";
        String userUsernameString = userJson.getString("username");
        String creditString = userJson.getString("credit");
        int credit = Integer.parseInt(creditString);
        String sexString = userJson.getString("sex");
        int sex = Integer.parseInt(sexString);
        String avatorString = userJson.optString("avatar");
        String bioString = userJson.getString("bio");
        User returnUser = new User(userId,userPhoneString,passwordString,userUsernameString,
                credit,sex,avatorString,bioString);
        return returnUser;
    }



    /**
     * 本方法用于将字节流转化成字符串。
     * 主要使用场景是，获取服务器的response之后，读取response中的content内容。
     * @param   is 从response中拿到的字节流
     * @return     从字节流中拿出的字符串
     */
    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
