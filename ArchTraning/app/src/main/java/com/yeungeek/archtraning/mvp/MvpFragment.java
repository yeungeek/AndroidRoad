package com.yeungeek.archtraning.mvp;

import android.os.Bundle;
import android.view.View;

import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.adapter.RepoAdapter;
import com.yeungeek.archtraning.api.Repo;
import com.yeungeek.archtraning.base.BaseFragment;
import com.yeungeek.archtraning.base.LinearDivider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * +-------+               +------+
 * |       |               |      |
 * | Model |               | View |
 * |       |               |      |
 * +---^---+               +-^--+-+
 *     |                     |  |
 *     |                     |  |
 *     |     +-----------+   |  |
 *     |     |           +---+  |
 *     +-----+ Presenter |      |
 *           |           <------+
 *           +-----------+
 *
 *  </pre>
 *
 * @author yangjian
 * @date 2018/09/20
 */

public class MvpFragment extends BaseFragment implements RepoView {
    private RecyclerView recyclerView;
    private RepoAdapter adapter;
    private RepoPresenter presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_view);
        adapter = new RepoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new LinearDivider(getActivity().getApplicationContext(), R.color.divider, R.dimen.divider));
        recyclerView.setAdapter(adapter);

        presenter = new RepoPresenter(this);

        view.findViewById(R.id.get_repo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getRepo("yeungeek");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mvp;
    }

    @Override
    public void onSuccess(List<Repo> repos) {
        adapter.setData(repos);
    }

    @Override
    public void onFailed() {

    }
}
