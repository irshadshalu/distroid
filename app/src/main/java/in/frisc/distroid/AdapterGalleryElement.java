package in.frisc.distroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.frisc.distroid.GalleryElementFragment.OnListFragmentInteractionListener;
import in.frisc.distroid.dummy.DummyContent.FileItem;
import in.frisc.distroid.utils.VideoRequestHandler;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FileItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdapterGalleryElement extends RecyclerView.Adapter<AdapterGalleryElement.ViewHolder> {

    private final List<FileItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    VideoRequestHandler videoRequestHandler;
    Picasso picassoInstance;

    public AdapterGalleryElement(Context context, List<FileItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(context.getApplicationContext())
                .addRequestHandler(videoRequestHandler)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_galleryelement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTextView.setText(mValues.get(position).name);
        if(mValues.get(position).thumbnail.equals("NONE")) {
            Picasso.get()
                    .load(new File(mValues.get(position).path))
                    .into(holder.mImageView);
        }
        else{
            Picasso.get()
                    .load(new File(mValues.get(position).thumbnail))
                    .into(holder.mImageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;
        public FileItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.img_thumbnail);
            mTextView = (TextView) view.findViewById(R.id.text_fileName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
