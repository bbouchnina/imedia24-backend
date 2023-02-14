package de.imedia24.shop.controller

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/product/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product.toProductResponse())
        }
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findProductsBySkus(
        @RequestParam("skus") skus: Set<String>
    ): ResponseEntity<List<ProductEntity>> {
        val products = productService.findProductsBySkuSet(skus)
        return if (products.isNullOrEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } else {
            ResponseEntity.ok().body(products)
        }
    }
    @PostMapping("/products", produces = ["application/json;charset=utf-8"])
    fun saveProduct(@RequestBody product:ProductEntity):ResponseEntity<ProductResponse>{
        val created : ProductEntity = productService.saveProduct(product)
        return ResponseEntity.ok(created.toProductResponse())
    }

    @PatchMapping("/products",produces = ["application/json;charset=utf-8"])
    fun updateProduct(@RequestBody product:ProductEntity):ResponseEntity<ProductResponse>{
        if(product.sku.isNullOrEmpty())
            return ResponseEntity.notFound().build()
        val oldProduct = productService.findProductBySku(product.sku) ?:
            return ResponseEntity.notFound().build()

       return ResponseEntity.ok(productService.saveProduct(ProductEntity(
           product.sku,
           if(product.name != oldProduct.name) product.name else oldProduct.name,
           if(product.description != oldProduct.description) product.description else oldProduct.description,
           if(product.price != oldProduct.price) product.price else oldProduct.price,
           if(product.stock != oldProduct.stock) product.stock else oldProduct.stock,
           oldProduct.createdAt,
           ZonedDateTime.now()
       )).toProductResponse())
    }

}
