package au.edu.jcu.uploadtofiredb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// ImageAdapter to all load book images into a recycler view
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final Context myContext;
    private final List<Upload> myUploads;

    public ImageAdapter (Context context, List<Upload> uploads) {
        myContext = context;
        myUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Loop through image list to add them into view
        Upload uploadCurrent = myUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getImageName());
        // Using third party tool 'Picasso' to modify image size and display style
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
    }

    // return size of image list
    @Override
    public int getItemCount() {
        return myUploads.size();
    }

    // Set book image as image view following the book name as text view
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
