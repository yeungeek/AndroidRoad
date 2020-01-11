package com.yeungeek.archtraning.mvp;

import com.yeungeek.archtraning.api.Repo;

import java.util.List;

/**
 * @author yangjian
 * @date 2018/09/21
 */

public interface RepoView {
    void onSuccess(List<Repo> repos);
    void onFailed();
}
