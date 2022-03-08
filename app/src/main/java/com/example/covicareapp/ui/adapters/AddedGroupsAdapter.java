package com.example.covicareapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.models.AddedGroupsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AddedGroupsAdapter extends FirestoreRecyclerAdapter<AddedGroupsModel, AddedGroupsAdapter.AddedGroupsHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private AddedGroupsAdapter.OnItemClickListener listener;

    public AddedGroupsAdapter(@NonNull FirestoreRecyclerOptions<AddedGroupsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddedGroupsHolder holder, int position, @NonNull AddedGroupsModel model) {
        holder.groupName.setText(model.getGroupName());
        holder.groupDesc.setText((model.getGroupInfo()));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddedGroupsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_groups_added_card, parent, false);
        return new AddedGroupsHolder(view);
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class AddedGroupsHolder extends RecyclerView.ViewHolder {

        // UI Variables for Card
        TextView groupName, groupDesc;

        public AddedGroupsHolder(@NonNull View itemView) {
            super(itemView);

            // UI hooks for card
            groupName = itemView.findViewById(R.id.group_title);
            groupDesc = itemView.findViewById(R.id.group_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


        }
    }


}

