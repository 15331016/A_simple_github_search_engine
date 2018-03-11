package com.example.yc.lab11;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yc.lab11.adapter.cardAdapter;
import com.example.yc.lab11.model.Github;
import com.example.yc.lab11.service.GithubService;


import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<Github> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private cardAdapter adapter = new cardAdapter(userList);
    private EditText search;
    private Button clear;
    private Button fetch;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
        myClick();
    }

    void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        search = (EditText) findViewById(R.id.search);
        clear = (Button) findViewById(R.id.clear);
        fetch = (Button) findViewById(R.id.fetch);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    void myClick() {
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将等待状态反馈给用户
                progress.setVisibility(View.VISIBLE);

                /**先判断是否有可用网络
                 *使用ConnectivityManager获取手机所有连接管理对象
                 *使用manager获取NetworkInfo对象
                 *最后判断当前网络状态是否为连接状态即可。
                 */
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if ((networkInfo == null) || !networkInfo.isConnected()) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                } else {//当前网络可用,进行数据获取
                    //使用Retrofit实现网络请求
                    retrofit();
                }
            }
        });

        adapter.setOnClickListener(new cardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //单击事件,进入详情界面,将login传递过去
                Intent it = new Intent(MainActivity.this, ReposActivity.class);
                it.putExtra("login", userList.get(position).getLogin());
                startActivity(it);
            }

            @Override
            public void onLongClick(int position) {
                //长按事件,删除该item
                userList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    void retrofit() {
        //初始化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)//设置OKHttpClient
                .baseUrl("https://api.github.com") // 设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .build();
        //获取GitHub的API
        GithubService gitHubAPI = retrofit.create(GithubService.class);

        //异步调用
        Call<Github> userCall = gitHubAPI.getUser(search.getText().toString());
        userCall.enqueue(new Callback<Github>() {
            @Override
            public void onResponse(Call<Github> call, Response<Github> response) {
                Github body = response.body();
                progress.setVisibility(View.GONE);
                if (body == null) {
                    Toast.makeText(MainActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //用户存在，添加item
                    userList.add(new Github(body.getLogin(), "id："+body.getId(), "blog："+body.getBlog()));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<Github> call, Throwable t) {
                progress.setVisibility(View.GONE);
                //判断是否被cancel掉了
                if (call.isCanceled()) {
                    Toast.makeText(MainActivity.this, "the call is canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
