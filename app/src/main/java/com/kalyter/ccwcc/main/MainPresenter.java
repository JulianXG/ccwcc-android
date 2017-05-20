package com.kalyter.ccwcc.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.add.AddFragment;
import com.kalyter.ccwcc.home.HomeFragment;
import com.kalyter.ccwcc.user.UserFragment;

import java.util.List;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class MainPresenter implements MainContract.Presenter,
        BottomNavigationView.OnNavigationItemSelectedListener{
    private MainContract.View mView;
    private FragmentManager mFragmentManager;

    public MainPresenter(MainContract.View view, FragmentManager fragmentManager) {
        mView = view;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void toggleFragment(int resId) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragmentTransaction.hide(fragment);
            }
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(String.valueOf(resId));
        if (fragment != null) {
            fragmentTransaction.show(fragment);
        } else {
            switch (resId) {
                case R.id.home:
                    fragmentTransaction.add(
                            R.id.frame_content, new HomeFragment(),
                            String.valueOf(resId));
                    break;
                case R.id.add_record:
                    fragmentTransaction.add(
                            R.id.frame_content, new AddFragment(),
                            String.valueOf(resId));
                    break;
                case R.id.me:
                    fragmentTransaction.add(
                            R.id.frame_content, new UserFragment(),
                            String.valueOf(resId));
                    break;
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        toggleFragment(item.getItemId());
        return true;
    }

    @Override
    public void start() {
        mView.setBottomItemSelectListener(this);
        mView.showDefaultSection();
    }
}
