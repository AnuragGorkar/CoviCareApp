package com.example.covicareapp.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsContract;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.TimeZone;

public class LocalUsersAdapter extends RecyclerView.Adapter<LocalUsersAdapter.LocalUsersViewHolder> {
    private final Context mContext;
    private Cursor mCursor;

    public LocalUsersAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public LocalUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custom_local_user_card, parent, false);
        return new LocalUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalUsersViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int nameIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.NAME);
        int genderIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.GENDER);
        int dOBIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.DATE_OF_BIRTH);
        String user_name = mCursor.getString(nameIndex);
        String user_gender = mCursor.getString(genderIndex);
        String user_dob = mCursor.getString(dOBIndex);

        holder.userName.setText(user_name);
        if (user_gender.equals("Male")) {
            holder.userGender.setTextColor(mContext.getColor(R.color.gender_male_dark));
            holder.userGender.setText("Male");
        } else if (user_gender.equals("Female")) {
            holder.userGender.setTextColor(mContext.getColor(R.color.gender_female_dark));
            holder.userGender.setText("Female");
        } else {
            holder.userGender.setTextColor(mContext.getColor(R.color.gender_other_dark));
            holder.userGender.setText("Other");
        }
        try {
            holder.userAge.setText(getAge(Long.parseLong(user_dob)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getAge(long dobSeconds) throws ParseException {
        long nowSeconds = Timestamp.now().getSeconds();

        LocalDate doB = LocalDateTime.ofInstant(Instant.ofEpochSecond(dobSeconds), TimeZone.getDefault().toZoneId()).toLocalDate();
        LocalDate dNow = LocalDateTime.ofInstant(Instant.ofEpochSecond(nowSeconds), TimeZone.getDefault().toZoneId()).toLocalDate();

        Period p = Period.between(doB, dNow);

        return String.valueOf(p.getYears());
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newMCursor) {
        if (mCursor != null)
            mCursor.close();
        mCursor = newMCursor;
        if (newMCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class LocalUsersViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userGender, userAge;


        public LocalUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userGender = itemView.findViewById(R.id.user_gender);
            userAge = itemView.findViewById(R.id.user_age);
        }
    }

}
