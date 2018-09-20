package com.yeungeek.archtraning.mvvm;

import com.yeungeek.archtraning.base.BaseFragment;

/**
 * <pre>
 * +-------+               +------+
 * |       |               |      |
 * | Model |               | View |
 * |       |               |      |
 * +-+--^--+               +---^--+
 *   |  |                      |
 *   |  |                    binding
 *   |  |    +-----------+     |
 *   |  +----+           |     |
 *   |       | ViewModel <-----+
 *   +------->           |
 *           +-----------+
 * </pre>
 *
 * @author yangjian
 * @date 2018/09/20
 */

public class MvvmFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return 0;
    }
}
