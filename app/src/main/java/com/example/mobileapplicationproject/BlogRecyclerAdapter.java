package com.example.mobileapplicationproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row,viewGroup,false);


        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        String imageUrl = null;
        holder.txtTitle.setText(blog.getTitle());
        holder.txtDescription.setText(blog.getDesc());
        holder.txtTime.setText(blog.getTimestamp());

        //To format the string data to java date format
        java.text.DateFormat dateformat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateformat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());

        holder.txtTime.setText(formatedDate);
        imageUrl = blog.getImage();
        //Use Picasso library to load images
        Picasso.get().load(imageUrl).into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtTime;
        String userid;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            context = ctx;
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView)itemView.findViewById(R.id.txtDescription);
            txtTime = (TextView)itemView.findViewById(R.id.txtTime);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);

            userid = null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
