package com.yeungeek.archtraning.mvc;

import android.os.Bundle;
import android.view.View;

import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.adapter.RepoAdapter;
import com.yeungeek.archtraning.api.GithubApi;
import com.yeungeek.archtraning.api.Repo;
import com.yeungeek.archtraning.api.ServiceGenerator;
import com.yeungeek.archtraning.base.BaseFragment;
import com.yeungeek.archtraning.base.LinearDivider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 * +-------+               +------+
 * |       +--------------->      |
 * | Model |               | View |
 * |       <---------------+      |
 * +---^---+               +--+---+
 *     |                      |
 *     |                      |
 *     |    +------------+    |
 *     |    |            |    |
 *     +----+ Controller <----+
 *          |            |
 *          +------------+
 * </pre>
 *
 * @author yangjian
 * @date 2018/09/20
 */

public class MvcFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RepoAdapter adapter;
    private GithubApi githubApi;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_view);
        adapter = new RepoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new LinearDivider(getActivity().getApplicationContext(), R.color.divider, R.dimen.divider));
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.get_repo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRepo();
            }
        });
    }

    private void getRepo() {
        githubApi = ServiceGenerator.createService(GithubApi.class);
        githubApi.userRepos("yeungeek").enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mvc;
    }
}
