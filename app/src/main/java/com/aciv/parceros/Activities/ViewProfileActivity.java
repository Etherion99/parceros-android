package com.aciv.parceros.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aciv.parceros.Fragments.ClientInviteFragment;
import com.aciv.parceros.Fragments.ClientSettingsFragment;
import com.aciv.parceros.Fragments.PartnerChatFragment;
import com.aciv.parceros.Fragments.PartnerStatsFragment;
import com.aciv.parceros.Fragments.Partnert.ShowCommentsFragment;
import com.aciv.parceros.Fragments.Partnert.ShowLocationFragment;
import com.aciv.parceros.Fragments.Partnert.ShowScheduleFragment;
import com.aciv.parceros.Fragments.Partnert.ShowServicesFragment;
import com.aciv.parceros.Fragments.Partnert.ShowSocialFragment;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {
    private Context context;
    private long id;
    private RetrofitInterface retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ImageView pictureIV = findViewById(R.id.pictureIV);
        final TextView nameTV = findViewById(R.id.nameTV);
        final TextView professionTV = findViewById(R.id.professionTV);
        final TextView descriptionTV = findViewById(R.id.descriptionTV);
        final ViewPager2 pageView = findViewById(R.id.pagView);
        final TabLayout navView = findViewById(R.id.navView);

        context = this;

        retrofit = RetrofitTools.getInterface();

        id = getIntent().getLongExtra("id", -1);

        Log.e("id", id+"");

        Call<User> showUserCall = retrofit.showUser(id);

        showUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null){
                    User user = response.body();
                    Log.e("user", new Gson().toJson(user));

                    nameTV.setText(user.getName());
                    professionTV.setText(user.getProfile().getProfession().getName());
                    descriptionTV.setText(user.getProfile().getDescription());
                }else{
                    Toast.makeText(context, "Error al mostrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        Glide.with(context)
                .load("http://www.acivp.hol.es/backend/api/profile/picture/" + id)
                .placeholder(R.drawable.v_main_rounded)
                .error(R.drawable.v_button_alt)
                .centerCrop()
                .into(pictureIV);

        FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return new ShowServicesFragment();
                    case 1:
                        return new ShowCommentsFragment();
                    case 2:
                        return new ShowSocialFragment();
                    case 3:
                        return new ShowScheduleFragment();
                    case 4:
                        return new ShowLocationFragment();
                }

                return new PartnerStatsFragment();
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        };

        pageView.setAdapter(pagerAdapter);

        pageView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                navView.getTabAt(position).select();
            }
        });

        navView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pageView.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
