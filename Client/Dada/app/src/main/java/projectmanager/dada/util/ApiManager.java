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
import java.util.List;
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
                    User returnUser = new User(userId, userPhoneString, passwordString, userUsernameString,
                            credit, sex, avatorString, bioString);
                    return returnUser;
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
                    String userIdString = userJson.getString("userId");
                    int rtUserId = Integer.parseInt(userIdString);
                    String userPhoneString = userJson.getString("phone");
                    String passwordString = "DefaultPassword";
                    String userUsernameString = userJson.getString("username");
                    String creditString = userJson.getString("credit");
                    int credit = Integer.parseInt(creditString);
                    String sexString = userJson.getString("sex");
                    int rtSex = Integer.parseInt(sexString);
                    String avatorString = userJson.optString("avator");
                    String bioString = userJson.getString("bio");
                    User returnUser = new User(rtUserId,userPhoneString,passwordString,userUsernameString,
                            credit,rtSex,avatorString,bioString);
                    return returnUser;
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
    public User getUserById(int userId){
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
                    String userIdString = userJson.getString("userId");
                    int rtUserId = Integer.parseInt(userIdString);
                    String userPhoneString = userJson.getString("phone");
                    String passwordString = "DefaultPassword";
                    String userUsernameString = userJson.getString("username");
                    String creditString = userJson.getString("credit");
                    int credit = Integer.parseInt(creditString);
                    String sexString = userJson.getString("sex");
                    int rtSex = Integer.parseInt(sexString);
                    String avatorString = userJson.optString("avator");
                    String bioString = userJson.getString("bio");
                    User returnUser = new User(rtUserId,userPhoneString,passwordString,userUsernameString,
                            credit,rtSex,avatorString,bioString);
                    return returnUser;
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
