package cn.edu.neusoft.zyfypt617lzy.SignInFrags;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.ArticleActivity;
import cn.edu.neusoft.zyfypt617lzy.R;
import cn.edu.neusoft.zyfypt617lzy.TwareActivity;
import cn.edu.neusoft.zyfypt617lzy.VideoActivity;
import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;

public class ArticleListAdapter extends RecyclerView.Adapter {
    private List<ArticleBean> articleList;
    private String mod;
    public void setArticleList(List<ArticleBean> articleList,String mod){
        this.mod = mod;
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate(R.layout.article_item, parent, false);
        ArticleViewHolder holder =new ArticleViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ArticleBean article =
                articleList.get(position);


        ArticleViewHolder articleViewHolder =
                (ArticleViewHolder) holder;

        //绑定数据
        articleViewHolder.titleView
                .setText( article.getName() );

        articleViewHolder.desView
                .setText( article.getDescription() );

        articleViewHolder.updateTimeView
                .setText(article.getUpdate_time());

        //显示图片
        String url = "http://amicool.neusoft.edu.cn/Uploads/" + article.getThumb();
        Picasso.get().load(url).into(articleViewHolder.imageView);
        //

        articleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mod.equals("tware")){
                            Intent intent = new Intent(v.getContext(), TwareActivity.class);
                            intent.putExtra("article_id", article.getId());
                            intent.putExtra("mod", mod);
                            intent.putExtra("pdfattach", article.getPdfattach());
                            v.getContext().startActivity(intent);
                        }else if(mod.equals("video")){
                            Intent intent = new Intent(v.getContext(), VideoActivity.class);
                            intent.putExtra("article_id", article.getId());
                            intent.putExtra("mod", mod);
                            intent.putExtra("videopath", article.getVideopath());
                            v.getContext().startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
                            intent.putExtra("article_id", article.getId());
                            intent.putExtra("mod", mod);
                            v.getContext().startActivity(intent);
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {

        if (articleList == null){

            return 0;
        }
        return articleList.size();
    }
}
