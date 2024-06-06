package me.dio.credit.application.system.service

import me.dio.credit.application.system.entity.Customer
import java.util.*

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Optional<Customer>
    fun delete(id: Long)
    fun getAll(): List<Customer>
}