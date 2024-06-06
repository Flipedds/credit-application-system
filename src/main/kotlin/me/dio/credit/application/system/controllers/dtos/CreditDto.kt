package me.dio.credit.application.system.controllers.dtos

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link me.dio.credit.application.system.entity.Credit}
 */
data class CreditDto(
    val creditValue: BigDecimal,
    val dayFirstInstallment: LocalDate,
    val numberOfInstallment: Int,
    val customerId: Long
) : Serializable {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        numberOfInstallment = this.numberOfInstallment,
        dayFirstInstallment = this.dayFirstInstallment,
        customer = Customer(id = this.customerId)
    )
}