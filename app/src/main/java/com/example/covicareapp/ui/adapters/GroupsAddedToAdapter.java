package com.example.covicareapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.models.GroupsAddedToModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class GroupsAddedToAdapter extends FirestoreRecyclerAdapter<GroupsAddedToModel, GroupsAddedToAdapter.GroupsAddedToHolder> {

    private GroupsAddedToAdapter.OnItemClickListener listener;

    public GroupsAddedToAdapter(@NonNull FirestoreRecyclerOptions<GroupsAddedToModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupsAddedToAdapter.GroupsAddedToHolder holder, int position, @NonNull GroupsAddedToModel model) {
        holder.groupName.setText(model.getGroupName());
        holder.groupDesc.setText((model.getGroupInfo()));
        holder.groupAddedBy.setText((model.getCreatedBy()));
    }

    public void setOnItemClickListener(GroupsAddedToAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupsAddedToAdapter.GroupsAddedToHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_added_to_groups_card, parent, false);
        return new GroupsAddedToAdapter.GroupsAddedToHolder(view);
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public class GroupsAddedToHolder extends RecyclerView.ViewHolder {

        // UI Variables for Card
        TextView groupName, groupDesc, groupAddedBy;

        public GroupsAddedToHolder(@NonNull View itemView) {
            super(itemView);

            // UI hooks for card
            groupName = itemView.findViewById(R.id.group_title);
            groupDesc = itemView.findViewById(R.id.group_desc);
            groupAddedBy = itemView.findViewById(R.id.group_created_by);
        }
    }
}
