package com.yeungeek.archtraning.mvp;

import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.base.BaseFragment;

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

public class MvpFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_mvp;
    }
}
