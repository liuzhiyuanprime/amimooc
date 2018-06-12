package cn.edu.neusoft.zyfypt617lzy.SignInFrags;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.neusoft.zyfypt617lzy.R;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public View itemView;


    public ImageView imageView;
    public TextView titleView, desView, updateTimeView;

    public ArticleViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;

        imageView = itemView.findViewById(R.id.imageView);
        titleView = itemView.findViewById(R.id.textView_title);
        desView = itemView.findViewById(R.id.textView_des);
        updateTimeView = itemView.findViewById(R.id.textView_update_time);

    }
}
