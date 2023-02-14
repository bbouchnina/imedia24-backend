package de.imedia24.shop.controller

//import io.mockk.every
import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.service.ProductService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @MockBean
    lateinit var productService: ProductService
    val baseUrl = "/products"
    val getUrl = "/product"

    private lateinit var repository: ProductRepository
    @Nested
    @DisplayName("GET /products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetProductsBySkuIn {


        @Test
        fun `should return Not Found for given SKUS `() {
            // given
            val products = mutableListOf<ProductEntity>()
            products.add(ProductEntity("1", "Product 1", "description for product 1", BigDecimal.valueOf(25.30), 10))

            given(productService.findProductsBySkuSet(setOf("1","2"))).willReturn(listOf())
            // when/then
            mockMvc.get("${baseUrl}/skus=1,2")
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }

        @Test
        fun `should return PRODUCT LIST if given SKUS are valid products`() {

            // given
            val products = mutableListOf<ProductEntity>()
            products.add(ProductEntity("1", "Product 1", "description for product 1", BigDecimal.valueOf(25.30), 10))
            products.add(ProductEntity("2", "Product 2", "description for product 2", BigDecimal.valueOf(30.30), 20))

            given(productService.findProductsBySkuSet(setOf("1","2"))).willReturn(products)
            // when/then
            mockMvc.get("${baseUrl}?skus=1,2")
                .andDo { print() }
                .andExpect { status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON_UTF8)
                        jsonPath("$[0].sku") {
                            value("1")
                        }
                        jsonPath("$[1].sku") {
                            value("2")
                        }
                    }}

        }
    }
    @Nested
    @DisplayName("PATCH /products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingProduct {


//        @Test
//        fun `should update an existing product`() {
//
//
//            // given
//            val product = ProductEntity("1", "Product 1", "description for product 1", BigDecimal.valueOf(25.30), 10)
//            val editProduct = ProductEntity("1", "Product Updated", "description for product 1", BigDecimal.valueOf(25.30), 10)
//            //given(productService.findProductBySku("1")).willReturn(editProduct)
//            mockMvc.post(baseUrl) {
//                contentType = MediaType.APPLICATION_JSON_UTF8
//                accept = MediaType.APPLICATION_JSON_UTF8
//                content = objectMapper.writeValueAsString(product)
//            }
//                .andDo { print() }
//                .andExpect {
//                    status { isOk() }
//                }
//            // when/then
//            mockMvc.patch(baseUrl) {
//                contentType = MediaType.APPLICATION_JSON_UTF8
//                accept = MediaType.APPLICATION_JSON_UTF8
//                content = objectMapper.writeValueAsString(editProduct)
//            }
//                .andDo { print() }
//                .andExpect {
//                    status { isOk() }
//                    content {
//                        contentType(MediaType.APPLICATION_JSON_UTF8)
//                        json(objectMapper.writeValueAsString(editProduct))
//                    }
//                }
////            mockMvc.get("${getUrl}/${editProduct.sku}")
////                .andDo { print() }
////                .andExpect {
////                    status { isOk() }
////                    content {
////                        contentType(MediaType.APPLICATION_JSON_UTF8)
////                        json(objectMapper.writeValueAsString(editProduct))
////                    }
////                }
//        }

        @Test
        fun `should return NOT FOUND if given product is not a valid product`() {
            // given
            val product = ProductEntity("0", "Product 1", "description for product 1", BigDecimal.valueOf(25.30), 10)
            // when/then
            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(product)
            }
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}