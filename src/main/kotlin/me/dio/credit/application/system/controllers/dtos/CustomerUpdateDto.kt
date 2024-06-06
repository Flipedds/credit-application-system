package me.dio.credit.application.system.controllers.dtos

import me.dio.credit.application.system.entity.Customer
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link me.dio.credit.application.system.entity.Customer}
 */
data class CustomerUpdateDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val income: BigDecimal? = null,
    val zipCode: String? = null,
    val street: String? = null
) : Serializable {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = firstName ?: customer.firstName
        customer.lastName = lastName ?: customer.lastName
        customer.income = income ?: customer.income
        customer.address.zipCode = zipCode ?: customer.address.zipCode
        customer.address.street = street ?: customer.address.street
        return customer
    }
}