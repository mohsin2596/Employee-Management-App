package com.filmfactory.ffem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.filmfactory.ffem.Services.JuniorTaskService;
import com.filmfactory.ffem.Services.SeniorTaskService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        sharedPreferences = getSharedPreferences("userRole",MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        getSupportActionBar().setTitle("Welcome " + name);

        String role = sharedPreferences.getString("role","");
        if(role.equalsIgnoreCase("senior")){
            startService(new Intent(this, SeniorTaskService.class));
        }
        else
            startService(new Intent(this, JuniorTaskService.class));

        init();
    }

    private void init(){
        viewPager = findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsfeedFragment(), "Newsfeed");
        adapter.addFragment(new ChatFragment(), "Chat");
        adapter.addFragment(new TaskFragment(), "Tasks");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
                break;
            }

            case R.id.action_search: {
                startActivity(new Intent(this,SearchUser.class));
                break;
            }

            case R.id.action_editprofile: {
                startActivity(new Intent(this,EditProfile.class));
                break;
            }

            case R.id.action_achievements: {
                startActivity(new Intent(this,Achievement.class));
                break;
            }

        }
        return false;
    }
}
