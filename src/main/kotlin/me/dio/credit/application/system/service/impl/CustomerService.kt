package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repositories.ICustomerRepository
import me.dio.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
    private val customerRepository: ICustomerRepository
) : ICustomerService {

    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(id: Long): Optional<Customer> = this.customerRepository.findById(id)

    override fun delete(id: Long) = this.customerRepository.deleteById(id)

    override fun getAll(): List<Customer> = this.customerRepository.findAll()
}