package com.example.covicareapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.models.OnlineUserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.TimeZone;

public class OnlineUserAdapter extends FirestoreRecyclerAdapter<OnlineUserModel, OnlineUserAdapter.UserHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnlineUserAdapter.OnItemClickListener listener;

    public OnlineUserAdapter(@NonNull FirestoreRecyclerOptions<OnlineUserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OnlineUserAdapter.UserHolder holder, int position, @NonNull OnlineUserModel model) {
        holder.userName.setText(model.getFullName());
        holder.userEmail.setText((model.getEmail()));
        try {
            holder.userAge.setText(getAge(model));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (model.getGender().equals("Male")) {
            holder.userGender.setImageResource(R.drawable.ic_baseline_male_24);
            holder.userGender.setColorFilter(R.color.gender_male_dark);
        } else if (model.getGender().equals("Female")) {
            holder.userGender.setImageResource(R.drawable.ic_baseline_female_24);
            holder.userGender.setColorFilter(R.color.gender_female_dark);
        } else {
            holder.userGender.setImageResource(R.drawable.ic_baseline_gender_24);
            holder.userGender.setColorFilter(R.color.gender_other_dark);
        }

    }

    public String getAge(OnlineUserModel model) throws ParseException {
        long dobSeconds = model.getDateOfBirth().getSeconds();
        long nowSeconds = Timestamp.now().getSeconds();

        LocalDate doB = LocalDateTime.ofInstant(Instant.ofEpochSecond(dobSeconds), TimeZone.getDefault().toZoneId()).toLocalDate();
        LocalDate dNow = LocalDateTime.ofInstant(Instant.ofEpochSecond(nowSeconds), TimeZone.getDefault().toZoneId()).toLocalDate();

        Period p = Period.between(doB, dNow);

        return String.valueOf(p.getYears());
    }

    public void setOnItemClickListener(OnlineUserAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OnlineUserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_online_users_card, parent, false);
        return new OnlineUserAdapter.UserHolder(view);
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class UserHolder extends RecyclerView.ViewHolder {

        // UI Variables for Card
        TextView userName, userEmail, userAge;
        ImageView userGender;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            // UI Hooks for Card
            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email_id);
            userAge = itemView.findViewById(R.id.user_age);
            userGender = itemView.findViewById(R.id.user_gender);


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