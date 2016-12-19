package projectmanager.dada.pages;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import projectmanager.dada.R;

/**
 * Created by JScarlet on 2016/12/19.
 */
public class ProjectInstructionActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ImageView[] imageViews;
    private int[] imgIdArray;
    private String[] description;
    private TextView textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_instruction);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imgIdArray = new int[]{R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};
        textDescription = (TextView) findViewById(R.id.viewPagerDescription);
        description = new String[]{"1. 哒哒找人（后简称“本应用”）是一款使用移动互联网技术来为实际生活提供便利的应用，类似于“嘀嘀打车”。在生活中我们常常有很多需要其他人帮助的一些小事情，比如帮忙拿快递这样的很小的事情但是却一时找不到合适的人来帮忙。本应用的目的在于打破这种信息不对称的情况，可以让更多的人知道用户的求助，来帮用户完成任务。",
                "2. “嘀嘀打车”通过地图和GPS定位系统，使得用户可以在自己指定的地点附近呼叫或预约出租车。该呼叫或预约请求将被广播给附近的出租车司机。收到通知的司机可以选择接受或不接受请求。但是“滴滴打车”的应用场景较窄，仅仅囊括了用户的出行打车需求。当用户需要其它类型的协助时，“滴滴打车”将不能胜任类似任务。",
                "3. 与“滴滴打车”类似，本应用将应用场景从“预约出租车”上进行了一定的扩展。本应用允许用户将自己需要帮助的请求发送到指定地点附近的其他用户的手机上，然后获得附近比较空闲的用户的帮助来完成任务。求助类型可以包括“代领快递”、“借用物品”、“问卷填写”、“个性化内容推送”或者其它用户自定义的类型。",
                "4. 本应用涉及的任务对于任务的接受者来说都是一些不需要用太多时间和精力都能完成的小任务，但这些任务对于任务的发出者却可以帮很大的忙。这种任务特性保证了任务的接收者不用因为任务将占用自己的太多时间而拒绝接受任务的情况出现。"};

     /*   imageViews = new ImageView[imgIdArray.length];
        for(int i = 0; i < imageViews.length; i++){
            ImageView imageView = new ImageView(this);
            imageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }*/

        if (imgIdArray.length == 1) {
            imageViews = new ImageView[2];
            for (int i = 0; i < (imageViews.length); i++) {
                ImageView imageView = new ImageView(this);
                imageViews[i] = imageView;
                imageView.setBackgroundResource(imgIdArray[0]);
            }

            viewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
        }else if (imgIdArray.length == 2 || imgIdArray.length == 3) {
            imageViews = new ImageView[imgIdArray.length * 2];
            for (int i = 0; i < (imageViews.length); i++) {
                ImageView imageView = new ImageView(this);
                imageViews[i] = imageView;
                imageView.setBackgroundResource(imgIdArray[(i > (imgIdArray.length-1)) ? i -imgIdArray.length  : i]);
            }
        } else {
            imageViews = new ImageView[imgIdArray.length];
            for (int i = 0; i < imageViews.length; i++) {
                ImageView imageView = new ImageView(this);
                imageViews[i] = imageView;
                imageView.setBackgroundResource(imgIdArray[i]);
            }
        }

        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(imageViews.length * 100);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        textDescription.setText(description[position % imageViews.length]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(imageViews[position % imageViews.length]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(imageViews[position % imageViews.length], 0);
            return imageViews[position % imageViews.length];
        }
    }

}
