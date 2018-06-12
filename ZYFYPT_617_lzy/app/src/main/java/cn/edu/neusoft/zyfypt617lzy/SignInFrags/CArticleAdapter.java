package cn.edu.neusoft.zyfypt617lzy.SignInFrags;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.CollectActivity;
import cn.edu.neusoft.zyfypt617lzy.R;
import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;
import cn.edu.neusoft.zyfypt617lzy.bean.CArticleBean;



public class CArticleAdapter extends RecyclerView.Adapter {

    private List<CArticleBean<ArticleBean>> articleList;//保存要显示的数据
    private String mod;
    private Context context;

    public CArticleAdapter(Context context, List<CArticleBean<ArticleBean>> list, String mod) {
        this.context = context;
        this.articleList = list;
        this.mod = mod;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate(R.layout.article_item, parent, false);
        ArticleViewHolder holder =new ArticleViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ArticleBean article =
                articleList.get(position).getBean();

        ArticleViewHolder articleViewHolder =
                (ArticleViewHolder) holder;

        //绑定数据
        articleViewHolder.titleView
                .setText(article.getName());

        articleViewHolder.desView
                .setText(article.getDescription());

        articleViewHolder.updateTimeView
                .setText(article.getUpdate_time());

        String url = "http://amicool.neusoft.edu.cn/Uploads/" + article.getThumb();
        Picasso.get().load(url).into(articleViewHolder.imageView);
        //

    }

    @Override
    public int getItemCount() {

        if (articleList == null){

            return 0;
        }
        return articleList.size();
    }

}
