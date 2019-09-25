package com.lianliantao.yuetuan.kotlin_activity;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenPhonePhotosActivity extends BaseTitleActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<String> list = new ArrayList<>();

    @Override
    public int getContainerView() {
        return R.layout.openphonephotosactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("照片");
//        initView();
        getAllPhotoInfo();
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void initView() {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的生成日期
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的详细信息
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            list.add(desc);
        }

    }

    /**
     * 读取手机中所有图片信息
     */
    private void getAllPhotoInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                List<MediaBean> mediaBeen = new ArrayList<>();
//                HashMap<String, List<MediaBean>> allPhotosTemp = new HashMap<>();//所有照片
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] projImage = {MediaStore.Images.Media._ID
                        , MediaStore.Images.Media.DATA
                        , MediaStore.Images.Media.SIZE
                        , MediaStore.Images.Media.DISPLAY_NAME};
                Cursor mCursor = getContentResolver().query(mImageUri,
                        projImage,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024;
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        //用于展示相册初始化界面
//                        mediaBeen.add(new MediaBean(MediaBean.Type.Image, path, size, displayName));
                        // 获取该图片的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();
                        list.add(path);
                        //存储对应关系
//                        if (allPhotosTemp.containsKey(dirPath)) {
//                            List<MediaBean> data = allPhotosTemp.get(dirPath);
//                            data.add(new MediaBean(MediaBean.Type.Image, path, size, displayName));
//                            continue;
//                        } else {
//                            List<MediaBean> data = new ArrayList<>();
//                            data.add(new MediaBean(MediaBean.Type.Image, path, size, displayName));
//                            allPhotosTemp.put(dirPath, data);
//                        }
                    }
                    mCursor.close();
                }
                //更新界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyAdapter adapter = new MyAdapter(list);
                        recyclerview.setAdapter(adapter);
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("手机照片", "magic   " + list.get(i));
                        }
                    }
                });
            }
        }).start();
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.phone_photo_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Glide.with(getApplicationContext()).load(list.get(position)).into(holder.iv);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
