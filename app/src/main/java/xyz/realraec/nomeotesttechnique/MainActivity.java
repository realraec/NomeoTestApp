package xyz.realraec.nomeotesttechnique;


import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);


        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.tab_text_1);
                                break;
                            case 1:
                                tab.setText(R.string.tab_text_2);
                                break;
                            default:
                                tab.setText(R.string.tab_text_3);
                        }
                    }
                }).attach();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            if (findViewById(R.id.webview) != null && ((WebView) findViewById(R.id.webview)).canGoBack()) {
                ((WebView) findViewById(R.id.webview)).goBack();
            } else {
                viewPager.setCurrentItem(0);
            }
        } else {
            super.onBackPressed();
        }

    }

}