package adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.kvartirkaclient.R
import models.Photo


class FlatPhotosAdapter(val context: Context, val photos: List<Photo>): PagerAdapter() {

    @Override
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view==obj as ConstraintLayout
    }

    @Override
    override fun getCount(): Int {
        return photos.size
    }

    @Override
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //val view = LayoutInflater.from(context).inflate(R.layout.images_list_item, container, false)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.images_list_item, container, false)


        val imageView = view.findViewById<ImageView>(R.id.photoIv)

        val curPhotoUrl = photos[position].url
        val urlForLoading = curPhotoUrl.replace("https://", "http://", false)

        val options: RequestOptions = RequestOptions()
            .override(100, 100)
            .centerCrop()
            .placeholder(R.drawable.photo_preload)
            .error(R.drawable.noimage)

        Glide.with(context)
            .load(urlForLoading)
            .apply(options)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("TAG", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(imageView)

        (container as ViewPager).addView(view, 0)

        return view
        //return super.instantiateItem(container, position)
    }

    @Override
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
       // super.destroyItem(container, position, obj)
        container.removeView(obj as View)
    }

}