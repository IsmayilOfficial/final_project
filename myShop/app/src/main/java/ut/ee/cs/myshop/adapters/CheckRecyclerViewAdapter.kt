package ut.ee.cs.myshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ut.ee.cs.myshop.R
import ut.ee.cs.myshop.entities.ProductObject

class CheckRecyclerViewAdapter(private val context: Context, private val mProductObject: List<ProductObject>) : RecyclerView.Adapter<CheckRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckRecyclerViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.check_layout, parent, false)
        return CheckRecyclerViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: CheckRecyclerViewHolder, position: Int) { //get product quantity
        holder.quantity.text = "1"
        holder.productName.text = mProductObject[position].productName
        holder.productPrice.text = mProductObject[position].productPrice.toString() + " $"
        holder.removeProduct.setOnClickListener { Toast.makeText(context, "Do you want to remove product from cart", Toast.LENGTH_LONG).show() }
    }

    override fun getItemCount(): Int {
        return mProductObject.size
    }

}