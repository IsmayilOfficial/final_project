package ut.ee.cs.myshop.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ut.ee.cs.myshop.R

class CheckRecyclerViewHolder(itemView: View) : ViewHolder(itemView) {
    var quantity: TextView
    var productName: TextView
    var productPrice: TextView
    var removeProduct: TextView

    init {
        quantity = itemView.findViewById<View>(R.id.quantity) as TextView
        productName = itemView.findViewById<View>(R.id.product_name) as TextView
        productPrice = itemView.findViewById<View>(R.id.product_price) as TextView
        removeProduct = itemView.findViewById<View>(R.id.remove_from_cart) as TextView
    }
}