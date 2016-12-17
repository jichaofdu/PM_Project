package projectmanager.dada.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import projectmanager.dada.R;
import projectmanager.dada.model.SexType;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;
import projectmanager.dada.util.FileImageUpload;
import projectmanager.dada.util.MPoPuWindow;

/**
 * Created by JScarlet on 2016/12/6.
 */
public class UserInformationActivity extends Activity {
    private User currentUser;
    private ImageView avatar;
    private TextView username;
    private TextView sex;
    private TextView phone;
    private TextView bio;
    private Type type;
    private MPoPuWindow puWindow;
    private File file;
    private Uri ImgUri;
    private String picPath = null;
    private Uri temp;

    public enum Type {
        PHONE, CAMERA
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        currentUser = DataManager.getInstance().getCurrentUser();
        avatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        sex = (TextView) findViewById(R.id.sex);
        phone = (TextView) findViewById(R.id.phone);
        bio = (TextView) findViewById(R.id.bio);
        if(currentUser != null){
            if(currentUser.getUsername() != null && !currentUser.getUsername().equals("")){
                username.setText(currentUser.getUsername());
            }else {
                username.setText("未填写");
            }

            if(currentUser.getSex() <= 3) {
                Toast.makeText(this, currentUser.getSex() + " ", Toast.LENGTH_SHORT).show();
                sex.setText(SexType.getTypeBySexId(currentUser.getSex()));
            }else {
                sex.setText(SexType.getTypeBySexId(0));
            }

            if(currentUser.getPhone() != null && !currentUser.getPhone().equals("")){
                phone.setText(currentUser.getPhone());
            }else {
                phone.setText("未绑定手机");
            }

            if(currentUser.getBio() != null && !currentUser.getBio().equals("")){
                bio.setText(currentUser.getBio());
            }else {
                bio.setText("未填写");
            }
        }

        View avatarView = findViewById(R.id.avatarLayout);
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInformationActivity.this, "click the avatar view", Toast.LENGTH_SHORT).show();
                puWindow = new MPoPuWindow(UserInformationActivity.this, UserInformationActivity.this);
                puWindow.showPopupWindow(findViewById(R.id.avatarLayout));
                puWindow.setOnGetTypeClckListener(new MPoPuWindow.onGetTypeClckListener() {

                    @Override
                    public void getType(Type type) {
                        UserInformationActivity.this.type = type;
                    }


                    @Override
                    public void getImgUri(Uri ImgUri, File file) {
                        UserInformationActivity.this.ImgUri = ImgUri;
                        UserInformationActivity.this.file = file;
                    }
                });

            }
        });

        View usernameView = findViewById(R.id.usernameLayout);
        usernameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInformationActivity.this, "click the username view", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserInformationActivity.this, UsernameModifyActivity.class);
                startActivity(intent);
            }
        });

        View sexView = findViewById(R.id.sexLayout);
        sexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInformationActivity.this, "click the sex view", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(UserInformationActivity.this);
                builder.setTitle("请选择性别");
                final String[] sexStrs = new String[]{"男", "女", "其他"};

                int tempSex = currentUser.getSex();
                if(tempSex == 0){
                    tempSex = 3;
                }
                builder.setSingleChoiceItems(sexStrs, tempSex - 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserInformationActivity.this, "性别为" + sexStrs[i], Toast.LENGTH_SHORT).show();
                        currentUser.setSex(i + 1);

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UserInfoTask userInfoTask = new UserInfoTask(currentUser.getUsername(), currentUser.getSex(), currentUser.getBio());
                        userInfoTask.execute((Void) null);
                 }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        View phoneView = findViewById(R.id.phoneLayout);
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInformationActivity.this, "click the phone view", Toast.LENGTH_SHORT).show();
            }
        });

        View bioView = findViewById(R.id.bioLayout);
        bioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInformationActivity.this, "click the bio view", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserInformationActivity.this, UserBioModifyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = DataManager.getInstance().getCurrentUser();
        avatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        sex = (TextView) findViewById(R.id.sex);
        phone = (TextView) findViewById(R.id.phone);
        bio = (TextView) findViewById(R.id.bio);
        if(currentUser != null){
            if(currentUser.getUsername() != null && !currentUser.getUsername().equals("")){
                username.setText(currentUser.getUsername());
            }else {
                username.setText("未填写");
            }

            if(currentUser.getSex() <= 3) {
                Toast.makeText(this, currentUser.getSex() + " ", Toast.LENGTH_SHORT).show();
                sex.setText(SexType.getTypeBySexId(currentUser.getSex()));
            }else {
                sex.setText(SexType.getTypeBySexId(0));
            }

            if(currentUser.getPhone() != null && !currentUser.getPhone().equals("")){
                phone.setText(currentUser.getPhone());
            }else {
                phone.setText("未绑定手机");
            }

            if(currentUser.getBio() != null && !currentUser.getBio().equals("")){
                bio.setText(currentUser.getBio());
            }else {
                bio.setText("未填写");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", type + "");
        if (requestCode == 1) {
            if (ImgUri != null) {
                puWindow.onPhoto(ImgUri, 300, 300);
            }
        } else if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                temp = uri;
        //        Log.i("xwk", data.toString());

                puWindow.onPhoto(uri, 300, 300);
            }
        } else if (requestCode == 3) {
            if (type == Type.PHONE) {
                if (data != null) {
//                    Bundle bundle = data.getExtras();
//                    Uri uri= bundle.getParcelable(MediaStore.EXTRA_OUTPUT);
//                    Log.i("xwk", bundle.toString());
////                    Uri uri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
//                    Log.i("xwk", String.valueOf(uri));

                    try{
                        String[] pojo = {MediaStore.Images.Media.DATA};
                        Log.i("xwk", String.valueOf(temp));
                        Cursor cursor = managedQuery(temp, pojo, null, null, null);
                        if(cursor != null){
                            ContentResolver cr = this.getContentResolver();
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            String path = cursor.getString(column_index);

                            if(path.endsWith("jpg") || path.endsWith("png")){
                                picPath = path;
                                Log.i("xwk", picPath);
                                UploadFileTask uploadFileTask = new UploadFileTask(this);
                                uploadFileTask.execute(picPath);
                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(temp));
                                if(bitmap != null){
                                    avatar.setImageBitmap(bitmap);
                                }
                            }else {
                                alert();
                            }
                        }else {
                            alert();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
              /*      Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    if (bitmap != null) {
                        avatar.setImageBitmap(bitmap);
                    }*/
                }
            } else if (type == Type.CAMERA) {
                avatar.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                Log.i("xwk", file.getPath());
//                UploadFileTask uploadFileTask = new UploadFileTask(this);
//                uploadFileTask.execute(file.getPath());
            }
        }
    }

    private void alert()
    {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                picPath = null;
                            }
                        })
                .create();
        dialog.show();
    }

    public class UserInfoTask extends AsyncTask<Void, Void, Boolean> {
        private final String username;
        private final int sexId;
        private final String bio;
        private User currentUser;

        UserInfoTask(String username, int sex, String bio) {
            this.username = username;
            this.sexId = sex;
            this.bio = bio;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            currentUser = ApiManager.getInstance().handleUpdateProfile(DataManager.getInstance().getCurrentUser().getUserId(), username, sexId, DataManager.getInstance().getCurrentUser().getAvatar(), bio);
            if(currentUser == null){
                System.out.println("[Tip] Login Fail.");
                return false;
            }else{
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                DataManager.getInstance().setCurrentUser(currentUser);
                sex.setText(SexType.getTypeBySexId(currentUser.getSex()));
            }else {
                Toast.makeText(UserInformationActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
        }
    }

    public class UploadFileTask extends AsyncTask<String, Void, String>{
        public static final String requestURL="http://192.168.208.13:8080/AndroidUploadFileWeb/FileImageUploadServlet";
        private ProgressDialog progressDialog;
        private Activity context = null;

        public UploadFileTask(Activity context){
            this.context = context;
            progressDialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
        }

        @Override
        protected String doInBackground(String... strings) {
            File file = new File(strings[0]);
            return FileImageUpload.uploadFile(file, requestURL);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(FileImageUpload.SUCCESS.equalsIgnoreCase(s)){
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
