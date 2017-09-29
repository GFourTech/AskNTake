package Adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.askntake.admin.askntake.OnLoadMoreListener;
import com.askntake.admin.askntake.R;

import java.util.List;

import Pojo.CustomerReviewPojo;

/**
 * Created by on 02/06/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<CustomerReviewPojo> custreviewList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ReviewAdapter(List<CustomerReviewPojo> reviews, RecyclerView recyclerView) {
        custreviewList = reviews;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return custreviewList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cust_review_show, parent, false);

            vh = new ReviewViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ReviewViewHolder) {
            CustomerReviewPojo customerReviewPojo = (CustomerReviewPojo) custreviewList.get(position);

            ((ReviewViewHolder) holder).ratingbar.setRating((float) customerReviewPojo.getReview());

            double k=customerReviewPojo.getReview();

            if(k>0.0 && k<=1.0)
            {
                k=1.0;

                ((ReviewViewHolder) holder).ratings_count_desc.setText("" + k);
            }
            else if(k>1.0 && k<=2.0)
            {
                k=2.0;

                ((ReviewViewHolder) holder).ratings_count_desc.setText("" + k);
            }
            else if(k>2.0 && k<=3.0)
            {
                k=3.0;

                ((ReviewViewHolder) holder).ratings_count_desc.setText("" + k);
            }
            else if(k>3.0 && k<=4.0)
            {
                k=4.0;

                ((ReviewViewHolder) holder).ratings_count_desc.setText("" + k);
            }
            else if(k>4.0 && k<=5.0)
            {
                k=5.0;

                ((ReviewViewHolder) holder).ratings_count_desc.setText("" + k);
            }

            //((ReviewViewHolder) holder).ratings_count_desc.setText("" + customerReviewPojo.getReview());

            ((ReviewViewHolder) holder).reviewer_name.setText(customerReviewPojo.getName());

            ((ReviewViewHolder) holder).ratings_comments.setText(customerReviewPojo.getComment());

            ((ReviewViewHolder) holder).review_date.setText(customerReviewPojo.getDate());
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return custreviewList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView ratings_count_desc;

        public TextView reviewer_name;

        public TextView ratings_comments;

        public TextView review_date;

        public RatingBar ratingbar;

        public CustomerReviewPojo customerReviewPojo;

        public ReviewViewHolder(View v) {
            super(v);
            ratings_count_desc = (TextView) v.findViewById(R.id.ratings_count_desc);

            reviewer_name = (TextView) v.findViewById(R.id.reviewer_name);
            ratings_comments = (TextView) v.findViewById(R.id.ratings_comments);

            review_date = (TextView) v.findViewById(R.id.review_date);

            ratingbar = (RatingBar) v.findViewById(R.id.ratingbar);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*Toast.makeText(v.getContext(),
                            "OnClick :" + student.getName() + " \n " + customerReviewPojo.getEmailId(),
                            Toast.LENGTH_SHORT).show();*/

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
