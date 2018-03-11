package com.example.yc.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yc.lab11.model.Github;
import com.example.yc.lab11.model.Repos;
import com.example.yc.lab11.service.GithubService;
import com.example.yc.lab11.service.ReposService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yc on 2017/12/22.
 */

public class ReposActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progress;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.response);

        //获取传递的参数
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");

        listView = (ListView) findViewById(R.id.repos_item);
        progress = (ProgressBar) findViewById(R.id.progress);
        simpleAdapter = new SimpleAdapter(ReposActivity.this, data, R.layout.repos_item,
                new String[]{"name", "language", "description"}, new int[]{R.id.name, R.id.language, R.id.description});
        listView.setAdapter(simpleAdapter);

        //初始化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)//设置OKHttpClient
                .baseUrl("https://api.github.com") // 设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .build();
        //获取API
        ReposService ReposAPI = retrofit.create(ReposService.class);

        //异步调用
        Call<List<Repos>> userCall = ReposAPI.listRepos(login);
        userCall.enqueue(new Callback<List<Repos>>() {
            @Override
            public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                progress.setVisibility(View.GONE);
                List<Repos> body = response.body();
                if (body == null) {
                    Toast.makeText(ReposActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //用户存在，添加item
                    int size = body.size();
                    for (int i = 0; i < size; i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", body.get(i).getName());
                        map.put("language", body.get(i).getLanguage());
                        map.put("description", body.get(i).getDescription());
                        data.add(map);
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Repos>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                //判断是否被cancel掉了
                if (call.isCanceled()) {
                    Toast.makeText(ReposActivity.this, "the call is canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReposActivity.this, "the call is not canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
