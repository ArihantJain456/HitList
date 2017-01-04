package com.example.arihantjain.hitlist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SingleEnemyFragment.Communicator{
    public static final int ENEMY_REQUEST_CODE = 1;
    private Toolbar toolbar;
    ListFragment fragmentPsrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        NavigationDrawerFragment nav = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
       nav.setUp(R.id.navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar);
        fragmentPsrent = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.frament_list);

    }

    @Override
    public void respond(String ID) {
        fragmentPsrent = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.frament_list);
        fragmentPsrent.removePage(ID);
    }

    @Override
    public void update(EnemyModel enemyModel) {
        fragmentPsrent = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.frament_list);
        fragmentPsrent.updateEnemyDD(enemyModel);
    }

    @Override
    public void refreshList() {
        fragmentPsrent.refreshData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Activity result");
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case ENEMY_REQUEST_CODE: {

                    System.out.println("Activity result 1");

                    System.out.println("Activity result ok");
                    EnemyModel enemyModel = (EnemyModel) data.getSerializableExtra("enemyData");
                    Toast.makeText(this, enemyModel.getName(), Toast.LENGTH_LONG).show();
                    fragmentPsrent.addPage(enemyModel);
                    fragmentPsrent.addEnemyToDatabase(enemyModel);
                }
                    break;
                case 11: {
                    EnemyModel enemyModel = (EnemyModel) data.getSerializableExtra("enemyData");
                    System.out.println("11");
                    fragmentPsrent.updateEnemyDD(enemyModel);
                }
                    break;
                default:
            }
        }
    }
}
