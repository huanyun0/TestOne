package com.example.testone.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.testone.Model.AppImage;
import com.example.testone.Model.Responses;
import com.example.testone.Net.OkHttpHelper;
import com.example.testone.Net.RequestCode;
import com.example.testone.Net.ServerHelper;
import com.example.testone.Net.TaskRunner;
import com.example.testone.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class ChannelFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.select)
    Button select;

    @BindView(R.id.send)
    Button send;

    @BindView(R.id.select_image)
    ImageView imageView;

    @BindView(R.id.download)
    Button download;

    @BindView(R.id.cancel1)
    Button cancell;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    AppImage appImage;

    Context context;

    String download_url = "https://pkg.biligame.com/games/FGO_v1.66.1_bili_105969.apk";

    boolean isCancelled ;
    boolean isDownload;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    appImage=(AppImage) msg.obj;
                    showImage();
                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.channel_fragment, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        isCancelled=false;
        isDownload =false;
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(data!=null){
                TaskRunner.execute(()->{
                    try {
                        ContentResolver resolver=getActivity().getContentResolver();
                        Uri uri=data.getData();
                        Log.d("Android的Uri：",uri.toString());
                        String[] arr = {MediaStore.Images.Media.DATA};
                        Cursor cursor = resolver.query(uri, arr, null, null, null);
                        Log.d("Cursor:",cursor.toString());
                        String img_path="";
                        if(cursor.moveToFirst()){
                            int img_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            Log.d("img_index:",String.valueOf(img_index));
                            img_path = cursor.getString(img_index);
                        }
//                        cursor.moveToFirst();
                        Log.d("img_path:",img_path);
                        cursor.close();
                        File file=new File(img_path);
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(resolver,uri);
                        Log.d("Bitmap:",bitmap.toString());
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=new AppImage(file,true,bitmap);
                        handler.sendMessage(msg);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("获取图片:","失败");
                    }
                });
            }
        }
    }

    @OnClick({R.id.select,R.id.send,R.id.download,R.id.cancel1})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select:
                Intent intent0=new Intent(Intent.ACTION_PICK,null);
                intent0.setType("image/*");
                startActivityForResult(intent0,1);
                break;
            case R.id.send:
                if(appImage.getIsUpLoad()){
                    String url= ServerHelper.getUrlSendImage();
                    try {
                        TaskRunner.execute(()->{
                            Type type = new TypeToken<Responses<AppImage>>() {
                            }.getType();
                            Responses<AppImage> responses=OkHttpHelper.post(url,type,appImage.getFile());
                            getActivity().runOnUiThread(()->{
                                if(responses!=null){
                                    switch (responses.getCode()){
                                        case RequestCode.SUCCESS:
                                            Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
                                            Log.d("上传图片:",responses.getMsg());
                                            break;
                                        case RequestCode.NOT_FOUND:
                                            Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();
                                            Log.d("上传图片:",responses.getMsg());
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        });

                    }
                    catch (Exception e){
                        Log.d("上传图片:","发生错误");
                    }
                }
                break;
            case R.id.download:
                if(!isDownload){
                    isDownload=true;
                    download();
                }
                else Toast.makeText(context,"正在下载中，请勿频繁点击下载键!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel1:
                isCancelled =true;
            default:
                break;
        }
    }

    private void download() {
        TaskRunner.execute(()->{
            isDownload=false;
            long FileLength =0;
            InputStream inputStream=null;
            FileOutputStream savedFile = null;
            File file =null;
            try {
                String fileName = download_url.substring(download_url.lastIndexOf("/"));
                String directory= Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS).getPath();
                Log.e("路径:",directory);
                file=new File(directory+fileName);
                if(file.exists()) {
                    FileLength = file.length();
                }
                long filesize = OkHttpHelper.getContentLength(download_url);
                if(filesize == 0){
                    Log.e("下载","失败");
                    return;
                }
                else if(filesize == FileLength){
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(context,"该文件已下载",Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                Response response = OkHttpHelper.get(download_url,FileLength);

                if(response!=null){
                    inputStream = response.body().byteStream();
                    savedFile = new FileOutputStream(file,true);//跳过已下载的字节
                    byte[] b=new byte[1024];
                    int total = 0;
                    int len;
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(context,"开始下载",Toast.LENGTH_SHORT).show();
                    });
                    while ((len=inputStream.read(b))!=-1){
                        if(isCancelled){
                            getActivity().runOnUiThread(()->{
                                Toast.makeText(context,"取消下载",Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(0);
                            });
                            isCancelled =false;
                            return;
                        }
                        else {
                            total+=len;
                            savedFile.write(b,0,len);
                            int progress = (int)((total + FileLength)*100/filesize);
                            getActivity().runOnUiThread(()->{
                                progressBar.setProgress(progress);
                            });
                        }
                    }
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(context,"下载成功",Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                try {
                    if(inputStream!=null){
                        inputStream.close();
                    }
                    if(savedFile!=null){
                        savedFile.close();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            getActivity().runOnUiThread(()->{
                Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void showImage(){
        File file=appImage.getFile();
        Glide.with(getActivity())
                .load(file)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Toast.makeText(getContext(),"加载成功",Toast.LENGTH_LONG).show();
                        return false;
                    }
                })
                .into(imageView);
    }
}
