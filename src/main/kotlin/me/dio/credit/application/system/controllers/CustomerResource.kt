package me.dio.credit.application.system.controllers

import me.dio.credit.application.system.controllers.dtos.CustomerDto
import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerResource(
    private val customerService: CustomerService
) {
     @PostMapping("/create")
     fun saveCustomer(@RequestBody customerDto: CustomerDto): String {
        val savedCustomer = this.customerService.save(customerDto.toEntity())
         return "Customer ${savedCustomer.email} saved!"
     }
}