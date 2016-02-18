package com.kalyter.ccwcc.ui.main.tab.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalyter.ccwcc.R;

public class StatisticsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(getTag(), "onCreateView");
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(getTag(),"onCreate");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(getTag(), "onActivityCreated");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(getTag(), "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(getTag(), "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(getTag(), "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(getTag(), "onDetach");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(getTag(), "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(getTag(), "onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(getTag(), "onSaveInstanceState");
    }



    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.i(getTag(), "onInflate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(getTag(), "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(getTag(), "onStop");
    }

}
