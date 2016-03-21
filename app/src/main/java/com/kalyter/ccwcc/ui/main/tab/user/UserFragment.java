package com.kalyter.ccwcc.ui.main.tab.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kalyter.ccwcc.R;

public class UserFragment extends Fragment {

    private LinearLayout layoutProfile, layoutAbout;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        findViews(inflater,container);
        setListenerToLayoutAbout();
        setListenerToLayoutProfile();
        return mView;
    }

    private void findViews(LayoutInflater inflater, ViewGroup container) {
        //判断是否为第一次加载view，否则用上一次保存的view
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_user, container, false);
        }
        // 缓存的view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        layoutProfile = (LinearLayout) mView.findViewById(R.id.layout_user_profile);
        layoutAbout = (LinearLayout) mView.findViewById(R.id.layout_user_about);
    }

    private void setListenerToLayoutAbout(){
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("ABOUT_ACTIVITY"));
            }
        });
    }

    private void setListenerToLayoutProfile(){
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("PROFILE_ACTIVITY"));
            }
        });
    }
}