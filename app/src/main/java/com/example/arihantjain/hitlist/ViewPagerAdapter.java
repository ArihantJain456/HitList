package com.example.arihantjain.hitlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Arihant Jain on 12/26/2016.
 */
public class ViewPagerAdapter  extends FragmentStatePagerAdapter{

    public final static ArrayList<Fragment> mFragmentList = new ArrayList<>();
    public final static ArrayList<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;
    int crossId;

    public ViewPagerAdapter(FragmentManager fm, Context context, ViewPager viewPager, TabLayout tabLayout) {
        super(fm);
        this.context = context;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        crossId = R.drawable.red_cross;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFrag(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public void removeFrag(int position){
        removeTab(position);
        Fragment fragment = mFragmentList.get(position);
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
        destroyFragmentView(fragment);
        notifyDataSetChanged();
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void destroyFragmentView(Fragment fragment) {
        FragmentManager manager = fragment.getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
       trans.remove(fragment);
        trans.commit();
    }

    public void removeTab(int position) {
        if (tabLayout.getChildCount() >0) {
            tabLayout.removeTabAt(position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return this.POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("TabPosition : ",position + "");
       return mFragmentTitleList.get(position);
    }
    public int setCross(int pos){
        SingleEnemyFragment singleEnemyFragment = (SingleEnemyFragment) mFragmentList.get(pos);
        if(singleEnemyFragment.isAlive()){
            return 0;
        }
        else {
            return crossId;
        }
    }
    public void updateEnemyData(int i,EnemyModel enemy){
        SingleEnemyFragment fragment = (SingleEnemyFragment)mFragmentList.get(i);
        mFragmentTitleList.add(i,enemy.getName());
        mFragmentTitleList.remove(i+1);
        fragment.setEnemyModel(enemy);
        notifyDataSetChanged();
        viewPager.setCurrentItem(i,true);
    }
}
