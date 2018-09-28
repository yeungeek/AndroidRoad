package com.yeungeek.modulea;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yeungeek.library.BaseFragment;
import com.yeungeek.library.data.model.Repo;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.RouterUtils;
import com.yeungeek.library.router.ToastService;
import com.yeungeek.library.ui.LinearDivider;
import com.yeungeek.modulea.adapter.RepoAdapter;
import com.yeungeek.modulea.mvp.RepoPresenter;
import com.yeungeek.modulea.mvp.RepoView;

import java.util.List;


/**
 * @author yangjian
 * @date 2018/09/27
 */
@Route(path = Consts.MODULEA_MVP_FRAGMENT)
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

        view.findViewById(R.id.module_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterUtils.navigation(Consts.MODULE_PAY_MAIN);
            }
        });

        view.findViewById(R.id.service_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastService toastService = (ToastService) RouterUtils.navigation(Consts.PAY_SERVCIE);
                toastService.toast("service pay");
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
