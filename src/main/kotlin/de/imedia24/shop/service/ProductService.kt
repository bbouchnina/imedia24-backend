package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductEntity? {
        val product:ProductEntity? =  productRepository.findBySku(sku)
        if(product != null) {
            return product
        }
        return null
    }


    fun findProductsBySkuSet(skuList:Set<String>):List<ProductEntity>?{
        return productRepository.findAllBySkuIn(skuList).toList()
    }

    fun saveProduct(product:ProductEntity):ProductEntity{

        return productRepository.save(product)
    }
}
