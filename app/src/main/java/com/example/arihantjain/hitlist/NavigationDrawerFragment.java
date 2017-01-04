package com.example.arihantjain.hitlist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import static com.example.arihantjain.hitlist.MainActivity.ENEMY_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    public ListView listView;
    public static final String PREF_NAME = "testpref";
    private static final String KEYUSER_LEARNED = "user_learned_drawer";
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private boolean userLearnedDrawer;
    private boolean fromSavedInstance;
    private View container;
    private String[] optionList;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEYUSER_LEARNED,"false"));
        if(savedInstanceState!=null){
            fromSavedInstance = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        listView = (ListView) view.findViewById(R.id.drawer_list);

        return view;
    }

    public void setUp(int navigation_drawer, DrawerLayout drawer, Toolbar toolbar) {
        container = getActivity().findViewById(navigation_drawer);
        this.drawerLayout = drawer;
        drawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!userLearnedDrawer){
                    userLearnedDrawer = true;
                    saveTopreferences(getActivity(),KEYUSER_LEARNED,userLearnedDrawer+ "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(userLearnedDrawer == false && !fromSavedInstance){
            drawerLayout.openDrawer(container);
        }
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        optionList = getResources().getStringArray(R.array.navigation_option_array);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,optionList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){

                    Intent intent = new Intent(getActivity(),AddEnemyActivity.class);
                    intent.putExtra("code","A");
                    getActivity().startActivityForResult(intent,ENEMY_REQUEST_CODE);
                    drawerLayout.closeDrawers();
                }

            }
        });
    }
    public static void saveTopreferences(Context context, String preference, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preference,preferenceValue);
        editor.apply();
    }
    public static String readFromPreferences(Context context,String preference,String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString(preference,preferenceValue);
    }
}
