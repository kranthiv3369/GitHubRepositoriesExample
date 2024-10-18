package com.git.repositories;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepositoryAdapter adapter;
    private List<Repository> repositoryList;
    private GitHubApi gitHubApi;

    private int page = 1;
    private final int perPage = 10;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repositoryList = new ArrayList<>();
        adapter = new RepositoryAdapter(repositoryList);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubApi = retrofit.create(GitHubApi.class);

        fetchRepositories("user");

        // Pagination (Load more as user scrolls)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // End of list reached, load more data
                    if (!isLoading) {
                        fetchRepositories("username");
                    }
                }
            }
        });
    }

    private void fetchRepositories(String username) {
        isLoading = true;
        adapter.addLoadingFooter();

        Call<List<Repository>> call = gitHubApi.getRepositories(page, perPage);//gitHubApi.getRepositories(username, page, perPage);

        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.isSuccessful()) {
                    List<Repository> newRepositories = response.body();
                    adapter.removeLoadingFooter();
                    adapter.addRepositories(newRepositories);  // Add new data to the adapter
                    page++;
                } else {
                    // Handle errors, e.g., rate limits
                    adapter.removeLoadingFooter();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                adapter.removeLoadingFooter();
                isLoading = false;
            }
        });
    }
}
