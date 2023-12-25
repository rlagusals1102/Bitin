package com.example.community.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.community.R;
import com.example.community.activity.BlogDetailActivity;
import com.example.community.domain.CommunityModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    ArrayList<CommunityModel> list;

    public CommunityAdapter(ArrayList<CommunityModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
    public void filter_list(ArrayList<CommunityModel> filter_list){
        list = filter_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunityModel model = list.get(position);
        holder.title.setText(model.getTittle());
        holder.date.setText(model.getDate());
        //holder.share_count.setText(model.getShare_count());
        holder.author.setText(model.getAuthor());

        Glide.with(holder.author.getContext()).load(model.getImg()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.author.getContext(), BlogDetailActivity.class);
                intent.putExtra("id", model.getId());
                holder.author.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.author.getContext());
                builder.setTitle("어떤 작업을 하시겠습니까?");
                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog u_dialog = new Dialog(holder.author.getContext());
                        u_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        u_dialog.setCancelable(false);
                        u_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        u_dialog.setContentView(R.layout.update_dialog);
                        u_dialog.show();

                        EditText title = u_dialog.findViewById(R.id.b_tittle);
                        EditText desc = u_dialog.findViewById(R.id.b_desc);

                        title.setText(model.getTittle());
                        desc.setText(model.getDesc());

                        TextView dialogbutton = u_dialog.findViewById(R.id.btn_publish);
                        dialogbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (title.getText().toString().equals("")) {
                                    title.setError("이 필드는 필수입니다!");
                                }
                                else if (desc.getText().toString().equals("")) {
                                    desc.setError("이 필드는 필수입니다!");
                                }
                                else {


                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("tittle", title.getText().toString());
                                    map.put("desc", desc.getText().toString());
                                    map.put("author", model.getAuthor());

                                    FirebaseFirestore.getInstance().collection("Blogs").document(model.getId()).update(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        u_dialog.dismiss();
                                                    }
                                                }
                                            });

                                }
                            }

                        });

                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builders = new AlertDialog.Builder(holder.author.
                                getContext());
                        builders.setTitle("정말 글을 삭제하시겠습니까?");
                        builders.setPositiveButton("삭제하기", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseFirestore.getInstance().collection("Blogs").
                                                document(model.getId()).delete();
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialogs = builders.create();
                        dialogs.show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView date, title, share_count, author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imageView3);
            date = itemView.findViewById(R.id.t_date);
            title = itemView.findViewById(R.id.textView9);
            share_count = itemView.findViewById(R.id.textView10);
            author = itemView.findViewById(R.id.textView8);

        }
    }
}