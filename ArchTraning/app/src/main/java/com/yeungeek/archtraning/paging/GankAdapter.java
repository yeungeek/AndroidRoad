package com.yeungeek.archtraning.paging;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.api.GankData;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class GankAdapter extends PagedListAdapter<GankData, BaseViewHolder> {
    private Context context;

    protected GankAdapter(@NonNull DiffUtil.ItemCallback<GankData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BaseViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.gank_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        ImageView imageView = holder.getView(R.id.iv);

        GankData data = getItem(position);
        if (null == data) {
            return;
        }

        if (!TextUtils.isEmpty(data.getCreatedAt())) {
            tvName.setText(data.getCreatedAt());
        }
        Glide.with(context).load(data.getUrl()).into(imageView);
    }
}
