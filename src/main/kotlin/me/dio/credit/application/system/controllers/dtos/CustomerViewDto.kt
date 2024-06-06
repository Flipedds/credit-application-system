package me.dio.credit.application.system.controllers.dtos

import me.dio.credit.application.system.entity.Credit
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link me.dio.credit.application.system.entity.Customer}
 */
data class CustomerViewDto(
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val income: BigDecimal = BigDecimal.ZERO,
    val email: String? = null,
    var credits: List<Credit> = mutableListOf(),
) : Serializable, RepresentationModel<CustomerViewDto>()