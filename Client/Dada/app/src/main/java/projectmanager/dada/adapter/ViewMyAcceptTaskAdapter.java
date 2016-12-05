package projectmanager.dada.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import projectmanager.dada.R;
import projectmanager.dada.model.Task;

/**
 * jichao at 2016/12/03
 * 用来将任务集显示在手机上的adapter
 */
public class ViewMyAcceptTaskAdapter extends ArrayAdapter<Task> {

    int resourceId;

    /**
     * Adapter的构造方法
     * @param context            当前活动的上下文
     * @param textViewResourceId ListView的子项布局id
     * @param tasks              要适配的数据
     */
    public ViewMyAcceptTaskAdapter(Context context, int textViewResourceId, List<Task> tasks){
        super(context,textViewResourceId,tasks);
        System.out.println("Adapter构建成功");
        resourceId = textViewResourceId;
    }

    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Get View方法进入");
        Task task = getItem(position);//获得当前项的Task实例
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//子项的view
        TextView titleView = (TextView) view.findViewById(R.id.my_accept_task_title);
        titleView.setText(task.getTitle());
        TextView descriptionView = (TextView)view.findViewById(R.id.my_accept_task_content);
        descriptionView.setText(task.getDescription());
//        TextView publisherView;
//        TextView publishTimeView;
//        TextView deadlineView;
//        TextView locationView;
//        TextView statusView;
//        TextView creditView;
        return view;
    }


}
