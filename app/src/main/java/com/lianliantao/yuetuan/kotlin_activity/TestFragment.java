package com.lianliantao.yuetuan.kotlin_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lianliantao.yuetuan.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestFragment extends Fragment {

    private String title;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            title = bundle.getString("title");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testfragment, container, false);
        ButterKnife.bind(this, view);
        tv.setText(title);
        return view;
    }
}
