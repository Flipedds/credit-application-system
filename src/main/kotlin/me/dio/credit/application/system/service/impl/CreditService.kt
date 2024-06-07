package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.exception.CreditServiceException
import me.dio.credit.application.system.repositories.CreditRepository
import me.dio.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CreditService(
    private val creditRepository: CreditRepository, private val customerService: CustomerService
) : ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!).getOrNull()
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = (this.creditRepository.findByCreditCode(creditCode)
            ?: throw CreditServiceException("Credit code $creditCode not found"))
        return if (credit.customer?.id == customerId) credit
        else throw CreditServiceException("Contact admin")
    }
}