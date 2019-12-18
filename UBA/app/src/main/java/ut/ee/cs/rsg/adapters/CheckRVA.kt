package ut.ee.cs.rsg.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ut.ee.cs.rsg.entities.ProductObject
import ut.ee.cs.rsg.R

class CheckRVA(private val context: Context, private val mProductObject: List<ProductObject>) : RecyclerView.Adapter<CheckRVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckRVH {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.check_layout, parent, false)
        return CheckRVH(layoutView)
    }

    override fun onBindViewHolder(holder: CheckRVH, position: Int) { //get product quantity
        holder.quantity.text = "1"
        holder.productName.text = mProductObject[position].productName
        holder.productPrice.text = mProductObject[position].productPrice.toString() + " $"
        holder.removeProduct.setOnClickListener {

            holder.quantity.text = "0"
            holder.productName.text = "deleted"
            holder.productPrice.text = "0"
            mProductObject[position].productPrice=0.0
            }
    }

    override fun getItemCount(): Int {
        return mProductObject.size
    }

}