package ut.ee.cs.myshop.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import ut.ee.cs.rsg.ProductActivity
import ut.ee.cs.rsg.R
import ut.ee.cs.rsg.entities.ProductObject

class ShopRVA(private val context: Context, private val allProducts: List<ProductObject>) : RecyclerView.Adapter<ShopRVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopRVH {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.product_listing, parent, false)
        return ShopRVH(layoutView)
    }

    override fun onBindViewHolder(holder: ShopRVH, position: Int) {
        val singleProduct = allProducts[position]
        holder.productName.text = singleProduct.productName
        holder.produceImage.setImageResource(singleProduct.productImage)
        holder.produceImage.setOnClickListener {
            val productIntent = Intent(context, ProductActivity::class.java)
            val builder = GsonBuilder()
            val gson = builder.create()
            val stringObjectRepresentation = gson.toJson(singleProduct)
            productIntent.putExtra("PRODUCT", stringObjectRepresentation)
            context.startActivity(productIntent)
        }
    }

    override fun getItemCount(): Int {
        return allProducts.size
    }

    companion object {
        private val TAG = ShopRVA::class.java.simpleName
    }

}