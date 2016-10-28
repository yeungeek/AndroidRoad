package com.yeungeek.views.flow;

import com.yeungeek.views.R;
import com.yeungeek.views.base.BaseFragment;
import com.yeungeek.views.flow.view.CustomView;

import butterknife.BindView;


public class FlowFragment extends BaseFragment {

    @BindView(R.id.id_view_custom)
    CustomView customView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flow;
    }

    @Override
    protected void init() {
        super.init();
    }
}
