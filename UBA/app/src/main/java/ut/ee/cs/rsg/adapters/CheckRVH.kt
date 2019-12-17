package ut.ee.cs.rsg.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ut.ee.cs.rsg.R

class CheckRVH(itemView: View) : ViewHolder(itemView) {
    var quantity: TextView = itemView.findViewById<View>(R.id.quantity) as TextView
    var productName: TextView = itemView.findViewById<View>(R.id.product_name) as TextView
    var productPrice: TextView = itemView.findViewById<View>(R.id.product_price) as TextView
    var removeProduct: TextView = itemView.findViewById<View>(R.id.remove_from_cart) as TextView

}