package me.dio.credit.application.system.controllers.dtos

import me.dio.credit.application.system.enumerators.Status
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * DTO for {@link me.dio.credit.application.system.entity.Credit}
 */
data class CreditViewDto(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallment: Int,
    val status: Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?,
) : Serializable, RepresentationModel<CreditViewDto>()