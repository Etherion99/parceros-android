package com.aciv.parceros.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.aciv.parceros.Fragments.PartnerChatFragment;
import com.aciv.parceros.Fragments.PartnerInviteFragment;
import com.aciv.parceros.Fragments.PartnerSettingsFragment;
import com.aciv.parceros.Fragments.PartnerStatsFragment;
import com.aciv.parceros.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HomePartnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_partner);

        final BottomNavigationView navView = findViewById(R.id.navView);
        final ViewPager2 pagView = findViewById(R.id.pagView);

        FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return new PartnerStatsFragment();
                    case 1:
                        return new PartnerInviteFragment();
                    case 2:
                        return new PartnerSettingsFragment();
                    case 3:
                        return new PartnerChatFragment();
                }

                return new Fragment();
            }

            @Override
            public int getItemCount() {
                return 4;
            }
        };

        pagView.setAdapter(pagerAdapter);


        pagView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                navView.setSelectedItemId(navView.getMenu().getItem(position).getItemId());
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_stats:
                        pagView.setCurrentItem(0);
                        break;
                    case R.id.nav_invite:
                        pagView.setCurrentItem(1);
                        break;
                    case R.id.nav_settings:
                        pagView.setCurrentItem(2);
                        break;
                    case R.id.nav_chat:
                        pagView.setCurrentItem(3);
                        break;
                }

                return true;
            }
        });
    }
}
