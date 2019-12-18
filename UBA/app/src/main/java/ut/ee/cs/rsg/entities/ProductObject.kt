package ut.ee.cs.rsg.entities

class ProductObject(val productId: Int, val productName: String, val productImage: Int, val productDescription: String, var productPrice: Double, val productSize: Int, val productColor: String) {

    override fun toString(): String {
        return "Product id and name: $productId $productName"
    }

}