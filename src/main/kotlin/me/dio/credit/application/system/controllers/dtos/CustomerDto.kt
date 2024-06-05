package me.dio.credit.application.system.controllers.dtos

import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link me.dio.credit.application.system.entity.Customer}
 */
data class CustomerDto(
    @NotNull(message = "First name is null") val firstName: String,
    @NotNull(message = "Last name is null") val lastName: String,
    @NotNull(message = "CPF is null") val cpf: String,
    @NotNull(message = "Income is null") val income: BigDecimal,
    @NotNull(message = "E-mail is null") val email: String,
    @NotNull(message = "Password is null") val password: String,
    @NotNull(message = "Zip Code is null") val zipCode: String,
    @NotNull(message = "Street is null") val street: String
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