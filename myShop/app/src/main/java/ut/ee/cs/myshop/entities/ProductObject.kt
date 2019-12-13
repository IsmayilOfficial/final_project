package ut.ee.cs.myshop.entities

class ProductObject(val productId: Int, val productName: String, val productImage: Int, val productDescription: String, val productPrice: Double, val productSize: Int, val productColor: String) {

    override fun toString(): String {
        return "Product id and name: $productId $productName"
    }

}