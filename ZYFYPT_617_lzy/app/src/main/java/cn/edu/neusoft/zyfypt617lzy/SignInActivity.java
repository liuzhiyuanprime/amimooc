package cn.edu.neusoft.zyfypt617lzy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.SignInFrags.Blog;
import cn.edu.neusoft.zyfypt617lzy.SignInFrags.Keynote;
import cn.edu.neusoft.zyfypt617lzy.SignInFrags.Owner;
import cn.edu.neusoft.zyfypt617lzy.SignInFrags.Sample;
import cn.edu.neusoft.zyfypt617lzy.SignInFrags.Video;

@SuppressLint("Registered")
public class SignInActivity extends AppCompatActivity {
    private BottomNavigationView menu;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private String sessionid;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;
    private ViewPager.OnPageChangeListener pageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        sharedPreferences = getSharedPreferences("zyfypt", Context.MODE_PRIVATE);
        sessionid = sharedPreferences.getString("sessionid","");
        init();
    }

    private void init(){
        viewPager = findViewById(R.id.signin_viewPager);
        menu = findViewById(R.id.signin_menu);

        fragmentList = new ArrayList<>();
        fragmentList.add(new Blog(sessionid));
        fragmentList.add(new Keynote(sessionid));
        fragmentList.add(new Video(sessionid));
        fragmentList.add(new Sample(sessionid));
        fragmentList.add(new Owner(sessionid));

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.blog :
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.keynote :
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.video :
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.sample:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.owner:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        };
        menu.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        menu.setSelectedItemId(R.id.blog);
                        break;
                    case 1:
                        menu.setSelectedItemId(R.id.keynote);
                        break;
                    case 2:
                        menu.setSelectedItemId(R.id.video);
                        break;
                    case 3:
                        menu.setSelectedItemId(R.id.sample);
                        break;
                    case 4:
                        menu.setSelectedItemId(R.id.owner);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(pageChangeListener);
    }
}