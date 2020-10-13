package com.rakpak.pak_parak_admin.Homepage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rakpak.pak_parak_admin.BottomnavPage.Idcardpage;
import com.rakpak.pak_parak_admin.BottomnavPage.NewsTabPages;
import com.rakpak.pak_parak_admin.BottomnavPage.UsersPage;
import com.rakpak.pak_parak_admin.GlobalChat;
import com.rakpak.pak_parak_admin.Login.LoginPage;
import com.rakpak.pak_parak_admin.Navagation.Help;
import com.rakpak.pak_parak_admin.Navagation.OurTeam;
import com.rakpak.pak_parak_admin.R;
import com.rakpak.pak_parak_admin.RecoderPage.RecoderPage;
import com.rakpak.pak_parak_admin.SampleUploadingPage.SampleUploadFile;


public class Homepage extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ImageView addimage;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout back_button;
    private FirebaseAuth Mauth;
    private String CurrentUserID;


    public Homepage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.homepage, container, false);

        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();

        back_button = view.findViewById(R.id.menu_buttonID);
        drawerLayout = view.findViewById(R.id.DrawerLayoutID);
        navigationView = view.findViewById(R.id.NavigationviewID);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.OurTeamID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setChecked(true);
                    item.setCheckable(true);
                    goto_teampage(new OurTeam());
                }

                if (item.getItemId() == R.id.GlobalChatID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setChecked(true);
                    item.setCheckable(true);


                    gotoglobalchat_page(new GlobalChat());
                }
                if (item.getItemId() == R.id.LogOutID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setChecked(true);
                    item.setCheckable(true);

                    Mauth.signOut();

                    goto_login_page(new LoginPage());

                }

                if(item.getItemId() == R.id.HelpIDs){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setChecked(true);
                    item.setCheckable(true);


                    goto_helppage(new Help());
                }

               /* if (item.getItemId() == R.id.Recoder) {
                    goto_recoderpage(new RecoderPage());
                }*/

                return true;
            }
        });

        addimage = view.findViewById(R.id.AddImage);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_uploadimagedile(new SampleUploadFile());
            }
        });
        bottomNavigationView = view.findViewById(R.id.BottomnavID);
        bottomNavigationView.setItemIconTintList(null);

        goto_idcardpage(new Idcardpage());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.IDCARD) {
                    goto_idcardpage(new Idcardpage());

                }
                if (item.getItemId() == R.id.NewsID) {
                    gotonewspage(new NewsTabPages());
                }
               /* if (item.getItemId() == R.id.UsersID) {
                    goto_userpage(new UsersPage());
                }*/


                return true;
            }
        });


        return view;
    }

    private void goto_idcardpage(Fragment fragment) {

        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFreamID, fragment);
            transaction.commit();
        }

    }

    private void goto_uploadimagedile(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFreamID, fragment);
            transaction.commit();
        }
    }

    private void gotonewspage(Fragment fragment) {

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.MainFreamID, fragment);
            fragmentTransaction.commit();
        }

    }

    private void goto_teampage(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }

    private void goto_helppage(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);

            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }

    private void gotoglobalchat_page(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);

            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }

    private void goto_recoderpage(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }

    private void goto_userpage(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFreamID, fragment);
            transaction.commit();
        }
    }


    private void remove_current_account() {

        final MaterialAlertDialogBuilder Mbuilder = new MaterialAlertDialogBuilder(getActivity());
        Mbuilder.setTitle("Delete Account");
        Mbuilder.setMessage("Are you sure to delete account? If you delete this account you will permanently loss your profile message and your photos");
        Mbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseUser Muser = Mauth.getCurrentUser();
                Muser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Account remove success", Toast.LENGTH_SHORT).show();
                                    goto_login_page(new LoginPage());
                                } else {
                                    Log.d("TAG", task.getException().getMessage());
                                }
                            }
                        });
            }
        });

        Mbuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });


        AlertDialog alertDialog = Mbuilder.create();
        alertDialog.show();

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser Muser = Mauth.getCurrentUser();
        if (Muser == null) {
            goto_login_page(new LoginPage());
        }

    }

    private void goto_login_page(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFream, fragment);
            transaction.commit();
        }
    }
}