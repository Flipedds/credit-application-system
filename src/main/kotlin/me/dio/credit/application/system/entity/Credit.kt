package me.dio.credit.application.system.entity

import jakarta.persistence.*
import me.dio.credit.application.system.controllers.dtos.CreditViewDto
import me.dio.credit.application.system.controllers.dtos.CreditViewListDto
import me.dio.credit.application.system.enumerators.Status
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "Credit")
data class Credit(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false, unique = true) val creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false) val creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) val dayFirstInstallment: LocalDate,
    @Column(nullable = false) val numberOfInstallment: Int = 0,
    @Enumerated val status: Status = Status.IN_PROGRESS,
    @ManyToOne() var customer: Customer? = null,
) {
    fun toListViewDto(): CreditViewListDto = CreditViewListDto(
        creditCode = this.creditCode, creditValue = this.creditValue, numberOfInstallment = this.numberOfInstallment
    )

    fun toViewDto(): CreditViewDto = CreditViewDto(
        creditCode = this.creditCode,
        creditValue = this.creditValue,
        numberOfInstallment = this.numberOfInstallment,
        status = this.status,
        emailCustomer = this.customer?.email,
        incomeCustomer = this.customer?.income
    )
}
