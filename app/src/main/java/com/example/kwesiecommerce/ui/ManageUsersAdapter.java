package com.example.kwesiecommerce.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwesiecommerce.Model.User;
import com.example.kwesiecommerce.R;

import java.util.List;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.UserViewHolder> {
    private Context context;
    private List<User> users;
    private OnUserClickListener onUserClickListener;

    public ManageUsersAdapter(Context context, List<User> users, OnUserClickListener onUserClickListener) {
        this.context = context;
        this.users = users;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manage_user_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName;
        private TextView textViewUserType;
        private ImageView buttonEditUser;
        private ImageView buttonDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textView_cart_product);
            textViewUserType = itemView.findViewById(R.id.date_added);
            buttonEditUser = itemView.findViewById(R.id.button_edit_user);
            buttonDeleteUser = itemView.findViewById(R.id.button_delete_cart);

            buttonEditUser.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = users.get(position);
                    onUserClickListener.onEditClick(user);
                }
            });

            buttonDeleteUser.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = users.get(position);
                    onUserClickListener.onDeleteClick(user);
                }
            });
        }

        public void bind(User user) {
            textViewUserName.setText(user.getUsername());
            textViewUserType.setText(user.getUsertype());
        }
    }

    public interface OnUserClickListener {
        void onEditClick(User user);
        void onDeleteClick(User user);
    }
}
