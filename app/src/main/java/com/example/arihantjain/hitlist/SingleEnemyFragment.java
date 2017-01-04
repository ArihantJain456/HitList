package com.example.arihantjain.hitlist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.PopupMenuCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.arihantjain.hitlist.MainActivity.ENEMY_REQUEST_CODE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleEnemyFragment extends Fragment {
    private static final int ENEMY_EDIT_REQUEST_CODE = 11;
    TextView eName,ePlace,eReason,eHouse,eLifeStatus;
    ImageView eImage;
    Button eEdit,eMore;
    public View rootView;
    Communicator communicator;
    private boolean alive;
    EnemyModel enemyModel;
    public SingleEnemyFragment() {

    }
    public SingleEnemyFragment(EnemyModel enemyModel){
        this.enemyModel = enemyModel;
        Log.d("Id",enemyModel.getID());
    }
    public interface Communicator{
        void respond(String ID);
        void update(EnemyModel enemyModel);
        void refreshList();
    }

    public EnemyModel getEnemyModel() {
        return enemyModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("fragment created");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_single_enemy, container, false);
        eName = (TextView) rootView.findViewById(R.id.enemy_name_text);
        ePlace = (TextView) rootView.findViewById(R.id.enemy_place_text);
        eReason = (TextView) rootView.findViewById(R.id.enemy_reason_text);
        eHouse = (TextView)rootView.findViewById(R.id.enemy_house_text);
        eLifeStatus = (TextView)rootView.findViewById(R.id.enemy_life_text);
        eImage = (ImageView)rootView.findViewById(R.id.enemy_imageview);
        eMore = (Button) rootView.findViewById(R.id.more_text);
        eEdit=(Button)rootView.findViewById(R.id.enemy_edit_text);
        eMore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final PopupMenu popupMenu = new PopupMenu(getActivity(),view);
                        MenuInflater inflater1 = popupMenu.getMenuInflater();
                        inflater1.inflate(R.menu.more_menu,popupMenu.getMenu());
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                if(id == R.id.delete_button){
                                    deleteFragment("Are you sure to delete this?");
                                }
                                else  if(id == R.id.dead_declaration){
                                    if(alive) {
                                        enemyModel.setAlive("No");
                                        communicator.update(enemyModel);
                                        Toast.makeText(getActivity(), "Marked as Dead", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Already Marked as Dead", Toast.LENGTH_SHORT).show();

                                }
                                return false;
                            }
                        });
                    }
                }
        );
        eEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),AddEnemyActivity.class);
                        intent.putExtra("enemyEdit",enemyModel);
                        intent.putExtra("code","U");
                        getActivity().startActivityForResult(intent,ENEMY_EDIT_REQUEST_CODE);
                    }
                }
        );
        eName.setText("" + enemyModel.getName());
        ePlace.setText("" + enemyModel.getPlaceToFind());
        eReason.setText("" + enemyModel.getReason());
        eHouse.setText("" + enemyModel.getHouse());
        eName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),eName.getText()+" "+ enemyModel.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        if(eImage!=null) {
            if (enemyModel.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(enemyModel.getImage(), 0, enemyModel.getImage().length);
                eImage.setImageBitmap(bitmap);
            }
        }
        if(enemyModel.getAlive().equals("Yes")){
            eLifeStatus.setText("Alive");
            alive = true;
        }
        else {
                alive = false;
            eLifeStatus.setText("Dead");

        }
        communicator = (Communicator)getActivity();
        return rootView;
    }
    private void deleteFragment(String message){
        final AlertDialog.Builder choiceDialog = new AlertDialog.Builder(getActivity());
        choiceDialog.setMessage(message).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                communicator.respond(enemyModel.getID());
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setEnemyModel(EnemyModel enemyModel) {
        this.enemyModel = enemyModel;
        System.out.println(enemyModel.getName() + "Single");
        eName.setText("" + enemyModel.getName());
        System.out.println(eName.getText()+"");
        ePlace.setText("" + enemyModel.getPlaceToFind());
        eReason.setText("" + enemyModel.getReason());
        eHouse.setText("" + enemyModel.getHouse());
        if(eImage!=null) {
            if (enemyModel.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(enemyModel.getImage(), 0, enemyModel.getImage().length);
                eImage.setImageBitmap(bitmap);
            }
        }
        if(enemyModel.getAlive().equals("Yes")){
            eLifeStatus.setText("Alive");
            alive = true;
        }
        else {
            alive = false;
            eLifeStatus.setText("Dead");
        }
        communicator.refreshList();
    }
}
