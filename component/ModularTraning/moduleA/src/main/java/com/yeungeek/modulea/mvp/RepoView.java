package com.yeungeek.modulea.mvp;


import com.yeungeek.library.data.model.Repo;

import java.util.List;

/**
 * @author yangjian
 * @date 2018/09/21
 */

public interface RepoView {
    void onSuccess(List<Repo> repos);
    void onFailed();
}
