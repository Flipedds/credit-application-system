package me.dio.credit.application.system.controllers.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link me.dio.credit.application.system.entity.Customer}
 */
data class CustomerDto(
    @field:NotEmpty val firstName: String,
    @field:NotEmpty val lastName: String,
    @field:NotEmpty @CPF val cpf: String,
    @field:NotNull val income: BigDecimal,
    @field:NotEmpty @Email val email: String,
    @field:NotEmpty val password: String,
    @field:NotEmpty val zipCode: String,
    @field:NotEmpty val street: String
) : Serializable {
    fun toEntity(): Customer  = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}