package com.git.repositories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Repository> repositoryList;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private boolean isLoadingAdded = false;

    public RepositoryAdapter(List<Repository> repositories) {
        this.repositoryList = repositories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
            return new RepositoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RepositoryViewHolder) {
            RepositoryViewHolder repositoryViewHolder = ((RepositoryViewHolder) holder);
            Repository repo = repositoryList.get(position);
            repositoryViewHolder.repoName.setText(MessageFormat.format("Name: {0}", repo.getName()));
            repositoryViewHolder.repoDescription.setText(MessageFormat.format("Description: {0}", repo.getDescription()));
            repositoryViewHolder.repoLanguage.setText(MessageFormat.format("Language: {0}", repo.getLanguage()));
            repositoryViewHolder.repoForks.setText(MessageFormat.format("Forks: {0}", repo.getForks()));

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = ((LoadingViewHolder) holder);
            loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
        }


    }

    public void addRepositories(List<Repository> repositories) {
        int startPosition = repositoryList.size();
        repositoryList.addAll(repositories);
        notifyItemRangeInserted(startPosition, repositories.size());
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        repositoryList.add(new Repository());  // Add a dummy item for the loading footer
        notifyItemInserted(repositoryList.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = repositoryList.size() - 1;
        repositoryList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == repositoryList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        TextView repoName, repoDescription, repoLanguage, repoForks;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.repo_name);
            repoDescription = itemView.findViewById(R.id.repo_description);
            repoLanguage = itemView.findViewById(R.id.repo_language);
            repoForks = itemView.findViewById(R.id.repo_forks);
        }
    }

    // ViewHolder for loading item
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}

