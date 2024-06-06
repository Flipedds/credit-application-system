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
    @NotNull @NotEmpty val firstName: String,
    @NotNull @NotEmpty val lastName: String,
    @NotNull @NotEmpty @CPF val cpf: String,
    @NotNull @NotEmpty val income: BigDecimal,
    @NotNull @NotEmpty @Email val email: String,
    @NotNull @NotEmpty val password: String,
    @NotNull @NotEmpty val zipCode: String,
    @NotNull @NotEmpty val street: String
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