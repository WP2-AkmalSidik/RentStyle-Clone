import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentstyle.R
import com.example.rentstyle.model.Product

@Suppress("DEPRECATION")
class ProductAdapter(private var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var listener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(position: Int, image: ImageView)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_image_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        private val productRating: TextView = itemView.findViewById(R.id.tv_product_rating)
        private val productLocation: TextView = itemView.findViewById(R.id.tv_product_location)
        private val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            productName.text = product.productName
            productPrice.text = "Rp. ${product.rentPrice}"
            productRating.text = product.avgRating.toString()
            productLocation.text = "Jakarta" // Placeholder, ubah sesuai data
            // Set image dan data lain yang sesuai
            itemView.setOnClickListener {
                listener?.onClick(adapterPosition, productImage)
            }
        }
    }
}
