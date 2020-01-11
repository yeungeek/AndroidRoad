package com.yeungeek.modulartraning.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yeungeek.library.util.FragmentUtil;
import com.yeungeek.modulartraning.R;

import java.util.List;

/**
 * @date 2018/09/09
 */

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.FragmentViewHolder> {
    private List<FragmentInfo> fragments;
    private FragmentManager fragmentManager;
    private int layoutId;

    public FragmentAdapter(FragmentManager manager, int layoutId, List<FragmentInfo> fragments) {
        this.fragmentManager = manager;
        this.layoutId = layoutId;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FragmentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position) {
        final FragmentInfo fragmentInfo = fragments.get(position);
        if (null != fragmentInfo) {
            holder.title.setText(fragmentInfo.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentUtil.pushFragmentToBackStack(fragmentManager, fragmentInfo.getFragment(), layoutId);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null != fragments ? fragments.size() : 0;
    }

    static class FragmentViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public FragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
