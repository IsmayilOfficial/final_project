package ut.ee.cs.myshop.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ut.ee.cs.rsg.R

class ShopRVH(itemView: View) : ViewHolder(itemView) {
    var produceImage: ImageView = itemView.findViewById<View>(R.id.product_image) as ImageView
    var productName: TextView = itemView.findViewById<View>(R.id.product_name) as TextView

}