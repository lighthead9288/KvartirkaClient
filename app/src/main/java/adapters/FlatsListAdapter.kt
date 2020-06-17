package adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.kvartirkaclient.R
import com.example.kvartirkaclient.databinding.FlatsListItemBinding
import models.Currency
import models.Flat

class FlatsListAdapter(val context:Context, val currency: Currency, val flatClickListener: FlatClickListener): ListAdapter<Flat, FlatsListAdapter.ViewHolder>(FlatsListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context, currency, flatClickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: FlatsListItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Flat, context: Context,  currency: Currency, flatClickListener: FlatClickListener) {
            binding.flat = item
            binding.flatCLickListener = flatClickListener
            val stringPrice = item.prices.day.toString()
            val currencyLabel = currency.label
            binding.priceText = "$stringPrice $currencyLabel"
            if (item.photo_default!=null) {
               // binding.flatPhotoIv.setImageURI(Uri.parse(item.photo_default.url))

                val defaultPhotoUrl = item.photo_default.url
                val urlForLoading = defaultPhotoUrl.replace("https://", "http://", false)

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
                    .into(binding.flatPhotoIv)
            }

            binding.executePendingBindings()
        }



        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FlatsListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }


}

class FlatsListDiffCallback: DiffUtil.ItemCallback<Flat>() {
    override fun areItemsTheSame(oldItem: Flat, newItem: Flat): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Flat, newItem: Flat): Boolean {
        return oldItem==newItem
    }

}

class FlatClickListener(val flatClickListener: (flat: Flat) -> Unit) {
    fun onClick(flat: Flat) = flatClickListener(flat)
}

