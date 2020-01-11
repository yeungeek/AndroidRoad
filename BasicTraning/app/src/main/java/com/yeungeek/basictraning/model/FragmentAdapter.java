package com.yeungeek.basictraning.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.FragmentUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
