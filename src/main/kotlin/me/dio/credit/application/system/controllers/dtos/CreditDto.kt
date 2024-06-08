package me.dio.credit.application.system.controllers.dtos

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.validators.ThreeMonths
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link me.dio.credit.application.system.entity.Credit}
 */
data class CreditDto(
    @field:NotNull @field:Min(1) val creditValue: BigDecimal,
    @field:Future @field:ThreeMonths val dayFirstInstallment: LocalDate,
    @field:Min(1) @field:Max(48) val numberOfInstallment: Int,
    @field:NotNull @field:Min(1) val customerId: Long
) : Serializable {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        numberOfInstallment = this.numberOfInstallment,
        dayFirstInstallment = this.dayFirstInstallment,
        customer = Customer(id = this.customerId)
    )
}