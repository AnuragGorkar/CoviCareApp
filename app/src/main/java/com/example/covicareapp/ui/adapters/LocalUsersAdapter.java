package com.example.covicareapp.ui.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsContract;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.ui.activities.LocalUserVitalsInfoActivity;
import com.example.covicareapp.ui.activities.addedGroups.GroupAddedInfoActivity;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.TimeZone;

public class LocalUsersAdapter extends RecyclerView.Adapter<LocalUsersAdapter.LocalUsersViewHolder> {
    private final Context mContext;
    private final String mActivityFragment;
    // Group Variables
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers, email, password;
    ArrayList<String> groupOnlineUsersList = new ArrayList<String>();
    private Cursor mCursor;
    ArrayList<String> groupOfflineUsersList = new ArrayList<String>();


    public LocalUsersAdapter(Context context, Cursor cursor, String activityFragment) {
        mContext = context;
        mCursor = cursor;
        mActivityFragment = activityFragment;
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
        int luidIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.LUID);
        int _idIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers._ID);
        int nameIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.NAME);
        int genderIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.GENDER);
        int dOBIndex = mCursor.getColumnIndex(VitalsContract.LocalUsers.DATE_OF_BIRTH);

        String user_luid = mCursor.getString(luidIndex);
        long user_id = mCursor.getLong(_idIndex);
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivityFragment.equals("SelectExsitingLocalUserActivity")) {
                    Log.i("Item Clicked", "SelectExsitingLocalUserActivity for " + user_id + user_luid);
                    addLocalUserToGroup(user_luid);

                } else if (mActivityFragment.equals("AddedGroupLocalUsersFragment")) {
                    Log.i("Item Clicked", "AddedGroupLocalUsersFragment for " + user_id + user_luid);
                    goToLocalUserInfoActicity(user_id, user_luid);
                }
            }
        });

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

    public void goToLocalUserInfoActicity(long user_id, String user_luid) {
        Intent intent = new Intent(mContext, LocalUserVitalsInfoActivity.class);
        intent.putExtra("local_ID", user_id);
        intent.putExtra("local_luid", user_luid);

        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupDateCreated", groupDateCreated);
        intent.putExtra("groupDescription", groupDescription);
        intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
        intent.putExtra("groupOnlineUsers", groupOnlineUsers);
        intent.putExtra("groupOfflineUsers", groupOfflineUsers);
        intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);

        mContext.startActivity(intent);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupInfo(String groupId, String groupName, String groupDateCreated, String groupDescription, String groupOfflineUsers, String groupOnlineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDateCreated = groupDateCreated;
        this.groupDescription = groupDescription;
        this.groupOfflineUsers = groupOfflineUsers;
        this.groupOnlineUsers = groupOnlineUsers;
        this.groupOfflineUsersList = groupOfflineUsersList;
        this.groupOnlineUsersList = groupOnlineUsersList;
    }

    public void addLocalUserToGroup(String user_luid) {
        Log.i("Add local user with userid ", user_luid);
        Log.i("GroupId to add user to ", this.groupId);
        VitalsSQLiteHelper vitalsSQLiteHelper = new VitalsSQLiteHelper(mContext);
        if (vitalsSQLiteHelper.addExistingUserLocal(groupId, user_luid)) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
            Intent intent = new Intent(mContext, GroupAddedInfoActivity.class);


            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", groupName);
            intent.putExtra("groupDateCreated", groupDateCreated);
            intent.putExtra("groupDescription", groupDescription);
            intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
            intent.putExtra("groupOnlineUsers", groupOnlineUsers);
            intent.putExtra("groupOfflineUsers", groupOfflineUsers);
            intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
            mContext.startActivity(intent, options.toBundle());
        } else {
            // Not Added
            Log.i("Error adding existing user ", "Error Check Kar re kay zhalay te");
        }
    }

    public interface OnLocalUserClickListener {
        void onLocalUserClick(int position);
    }

    public class LocalUsersViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userGender, userAge;
        CardView cardView;

        public LocalUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userGender = itemView.findViewById(R.id.user_gender);
            userAge = itemView.findViewById(R.id.user_age);
            cardView = itemView.findViewById(R.id.local_user_card_view);
        }
    }


}
