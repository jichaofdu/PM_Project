package projectmanager.dada.pages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import projectmanager.dada.R;
import projectmanager.dada.adapter.MyAdapter;
import projectmanager.dada.model.ListItem;
import projectmanager.dada.model.User;
import projectmanager.dada.util.DataManager;


public class UserInfoActivity extends Activity {
    private ListView listView;
    public static UserInfoActivity ma;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ma=this;
        listView = (ListView)findViewById(R.id.list);

        ArrayList<ListItem> list_GroupItem = new ArrayList<ListItem>();
        MyAdapter mAdapter_ListGroup = new MyAdapter(this, list_GroupItem);
        mAdapter_ListGroup.AddType(R.layout.activity_user_info_avatar);
        mAdapter_ListGroup.AddType(R.layout.activity_user_info_row_item);
        listView.setAdapter(mAdapter_ListGroup);

        list_GroupItem.add(new ListItem(0, getHashMap0("头像", R.mipmap.ic_launcher)));
        list_GroupItem.add(new ListItem(1, getHashMap1("昵称", "JScarlet")));

        mAdapter_ListGroup.notifyDataSetChanged();

        User user = DataManager.getInstance().getCurrentUser();
        Toast.makeText(this, user.getUsername(), Toast.LENGTH_SHORT).show();
     //   Log.i("123", user.getUsername());
    }

    private HashMap<Integer, Object> getHashMap0(String s1,int s2) {
        HashMap<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.avatarTitle, s1);
        map1.put(R.id.avatarImage, s2);
        return map1;
    }

    private HashMap<Integer, Object> getHashMap1(String s1, String s2) {
        HashMap<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.attribute, s1);
        map1.put(R.id.value, s2);

        return map1;
    }
}
