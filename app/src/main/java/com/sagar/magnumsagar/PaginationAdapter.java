package com.sagar.magnumsagar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    private List<Item> ItemResults;
    private Context mContext;
    private PaginationAdapterCallback mCallback;




    public PaginationAdapter(Context mContext) {
        this.mContext = mContext;
        this.mCallback = (PaginationAdapterCallback) mContext;
        ItemResults = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.footer_retry, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.itemlist, parent, false);
        viewHolder = new ItemVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Item item = ItemResults.get(position);
        switch (getItemViewType(position)) {
            case ITEM:

                final ItemVH itemVH = (ItemVH) holder;

                itemVH.textView.setText(item.getLogin());
               Glide
                        .with(mContext)
                        .load(item.getAvatar_url())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .into(itemVH.imageView);

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mContext.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return ItemResults == null ? 0 : ItemResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == ItemResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Item r) {
        ItemResults.add(r);
        notifyItemInserted(ItemResults.size() - 1);
    }

    public void addAll(List<Item> moveResults) {
        for (Item item : moveResults) {
            add(item);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Item());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = ItemResults.size() - 1;
        Item item = getItem(position);

        if (item != null) {
            ItemResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Item getItem(int position) {
        return ItemResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(ItemResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class ItemVH extends RecyclerView.ViewHolder{


        private ImageView imageView;
        private TextView textView;

        public ItemVH(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.avatar);
            textView = (TextView)itemView.findViewById(R.id.login);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;


        public LoadingVH(@NonNull View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }

        }
    }
}
