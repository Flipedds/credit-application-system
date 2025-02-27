package me.dio.credit.application.system.entity

import jakarta.persistence.*
import me.dio.credit.application.system.controllers.dtos.CustomerViewDto
import java.math.BigDecimal

@Entity
@Table(name = "Customer")
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) var firstName: String = "",
    @Column(nullable = false) var lastName: String = "",
    @Column(nullable = false, unique = true) var cpf: String = "",
    @Column(nullable = false) var income: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(nullable = false) var password: String = "",
    @Column(nullable = false) @Embedded var address: Address = Address(),
    @Column(nullable = false) @OneToMany(
        fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "customer"
    ) var credits: List<Credit> = mutableListOf(),
) {
    fun toViewDto(): CustomerViewDto =
        CustomerViewDto(this.id, this.firstName, this.lastName, this.income, this.email, this.credits)
}
