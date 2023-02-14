package de.imedia24.shop.db.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @Column(name = "sku", nullable = false)
    val sku: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "price", nullable = false)
    val price: BigDecimal,

    @Column(name = "stock", nullable = false)
    val stock: Int,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime? = null
){
    constructor() : this("", "", null, BigDecimal.ZERO,0, ZonedDateTime.now(), ZonedDateTime.now())
}