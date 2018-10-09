package com.yeungeek.archtraning.paging;

import android.app.Application;
import android.util.Log;

import com.yeungeek.archtraning.api.BaseResponse;
import com.yeungeek.archtraning.api.GankApi;
import com.yeungeek.archtraning.api.GankData;
import com.yeungeek.archtraning.api.ServiceGenerator;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;
import retrofit2.Response;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class GankViewModel extends AndroidViewModel {
    private GankApi gankApi;
    private LiveData<PagedList<GankData>> mLiveData;
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NUM = 1;
    private int mPage = PAGE_NUM;


    public GankViewModel(@NonNull Application application) {
        super(application);
        gankApi = ServiceGenerator.createService(GankApi.class);
    }

    public LiveData<PagedList<GankData>> getData() {
        initPageList();
        return mLiveData;
    }

    private void initPageList() {
        final PositionalDataSource<GankData> dataSource = new PositionalDataSource<GankData>() {
            @Override
            public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<GankData> callback) {
                Log.d("DEBUG", "loadInitial");
                try {
                    Response<BaseResponse<List<GankData>>> response = gankApi.getFuli(params.pageSize,
                            mPage).execute();
                    List<GankData> data = response.body().getResults();
                    callback.onResult(data, params.requestedStartPosition, Integer.MAX_VALUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<GankData> callback) {
                Log.d("DEBUG", "loadRange");
                try {
                    mPage++;
                    Response<BaseResponse<List<GankData>>> response = gankApi.getFuli(params.loadSize,
                            mPage).execute();

                    callback.onResult(response.body().getResults());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
//        final TiledDataSource<GankData> dataSource = new TiledDataSource<GankData>() {
//            @Override
//            public int countItems() {
//                return 30;
//            }
//
//            /**
//             *
//             * @param startPosition
//             * @param count
//             * @return
//             */
//            @Override
//            public List<GankData> loadRange(int startPosition, int count) {
//                List<GankData> gankData = new ArrayList<>();
//                try {
//                    Response<BaseResponse<List<GankData>>> response = gankApi.getFuli(PAGE_SIZE,mPage).execute();
//                    gankData.addAll(response.body().getResults());
//
//                    if (!gankData.isEmpty()) {
//                        mPage++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return gankData;
//            }
//        };

        mLiveData = new LivePagedListBuilder(new DataSource.Factory() {
            @Override
            public DataSource create() {
                return dataSource;
            }
        }, new PagedList.Config.Builder().setPageSize(PAGE_SIZE)
                .setPrefetchDistance(2).setEnablePlaceholders(false).build()).build();

//        mLiveData = new LivePagedListBuilder(new DataSource.Factory<Integer, GankData>() {
//            @Override
//            public DataSource<Integer, GankData> create() {
//                return dataSource;
//            }
//        }, new PagedList.Config.Builder().setPageSize(PAGE_SIZE)
//                .setPrefetchDistance(2).setEnablePlaceholders(false).build()).build();
    }
}