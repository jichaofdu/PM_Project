package projectmanager.dada.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import projectmanager.dada.R;
import projectmanager.dada.model.SexType;
import projectmanager.dada.model.User;
import projectmanager.dada.util.DataManager;

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
                final String[] sex = new String[]{"男", "女", "其他"};

                builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserInformationActivity.this, "性别为" + sex[i], Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
}
