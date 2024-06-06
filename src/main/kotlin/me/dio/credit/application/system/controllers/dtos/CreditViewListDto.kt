package me.dio.credit.application.system.controllers.dtos

import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * DTO for {@link me.dio.credit.application.system.entity.Credit}
 */
data class CreditViewListDto(
    val creditCode: UUID = UUID.randomUUID(),
    val creditValue: BigDecimal = BigDecimal.ZERO,
    val numberOfInstallment: Int = 0
) : Serializable, RepresentationModel<CreditViewListDto>()