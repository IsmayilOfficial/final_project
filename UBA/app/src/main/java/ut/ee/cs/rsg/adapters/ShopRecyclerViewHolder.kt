package ut.ee.cs.myshop.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ut.ee.cs.rsg.R

class ShopRecyclerViewHolder(itemView: View) : ViewHolder(itemView) {
    var produceImage: ImageView
    var productName: TextView

    init {
        produceImage = itemView.findViewById<View>(R.id.product_image) as ImageView
        productName = itemView.findViewById<View>(R.id.product_name) as TextView
    }
}