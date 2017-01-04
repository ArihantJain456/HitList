package com.example.arihantjain.hitlist;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    public static int IDProvider = 0;
    public static TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    ArrayList<String> arrayList;
    EnemyDatabaseHelper dbHelper;
    ImageView redCross;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.my_tab_layout);
        redCross = (ImageView) view.findViewById(R.id.red_cross_image);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();
                if(tab.getPosition()>=0){
                    int res = adapter.setCross(tab.getPosition());
                    redCross.setImageResource(res);
                    if(res!=0){
                        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
                        redCross.startAnimation(animation);
                    }
                }
                Log.d("Selected", "Selected " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                redCross.setImageResource(0);
            }
        });
        return view;
    }

    int selectedTabPosition;

    public void addPage(EnemyModel enemyModel) {

        String pagename = enemyModel.getName();
        if(enemyModel.getID() == null)
        enemyModel.setID(""+(++IDProvider));
        Log.d("Enemy","Adding enemy with id "+ enemyModel.getID());
        SingleEnemyFragment fragmentChild = new SingleEnemyFragment(enemyModel);
        adapter.addFrag(fragmentChild, pagename);
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) {
            tabLayout.setupWithViewPager(viewPager);
        }
        viewPager.setCurrentItem(adapter.getCount() - 1,true);
        selectedTabPosition = viewPager.getCurrentItem();
    }

    public void removePage(String id) {
        adapter.removeFrag(tabLayout.getSelectedTabPosition());
        dbHelper.delete(id);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList = new ArrayList<>();
        dbHelper = new EnemyDatabaseHelper(getActivity());
        try {
            dbHelper.checkAndCopyDatabase();
            System.out.println("checkable");
            dbHelper.openDataBase();
            System.out.println("opening");
        } catch (SQLException e) {
            System.out.println("error in copying");
        }

        try {
            Cursor cursor = dbHelper.QueryData("select * from Enemies_table");
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    do {
                        EnemyModel enemyModel = new EnemyModel();
                        enemyModel.setID(cursor.getString(0));
                        enemyModel.setName(cursor.getString(1));
                        IDProvider = Integer.parseInt(enemyModel.getID());
                        enemyModel.setHouse(cursor.getString(2));
                        enemyModel.setPlaceToFind(cursor.getString(3));
                        enemyModel.setReason(cursor.getString(4));
                        byte[] image = cursor.getBlob(5);
                        enemyModel.setImage(image);
                        enemyModel.setAlive(cursor.getString(6));
                        addPage(enemyModel);
                    }
                    while (cursor.moveToNext());
                    System.out.println(arrayList);
                }
            }
        } catch (SQLException e) {
        }
        System.out.println(arrayList);
        redCross.setImageResource(0);
    }

    public void addEnemyToDatabase(EnemyModel enemyModel){
        dbHelper.insertData(enemyModel);
    }
    public void updateEnemyDD(EnemyModel enemyModel){
        Log.d("update","Updating ID : " +enemyModel.getID());
        adapter.updateEnemyData(tabLayout.getSelectedTabPosition(),enemyModel);
        dbHelper.updateData(enemyModel);
        tabLayout.setScrollPosition(tabLayout.getSelectedTabPosition(),0,true);
    }
    public void refreshData() {
        adapter.notifyDataSetChanged();
    }
}


