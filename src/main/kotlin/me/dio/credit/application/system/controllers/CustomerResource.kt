package me.dio.credit.application.system.controllers

import jakarta.validation.Valid
import me.dio.credit.application.system.controllers.dtos.CustomerDto
import me.dio.credit.application.system.controllers.dtos.CustomerUpdateDto
import me.dio.credit.application.system.controllers.dtos.CustomerViewDto
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.ICustomerService
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = ["*"])
class CustomerResource(
    private val customerService: ICustomerService
) {
    @PostMapping("/")
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<CustomerViewDto> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(this.customerService.save(customerDto.toEntity()).toViewDto().apply {
                add(linkTo(methodOn(CustomerResource::class.java).getCustomer(this.id!!)).withSelfRel())
            })

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val customerOptional: Optional<Customer> = customerService.findById(id)
        if (customerOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found")
        }
        return ResponseEntity.status(HttpStatus.OK).body(
            customerOptional.get().toViewDto()
                .add(linkTo(methodOn(CustomerResource::class.java).getAllCustomers()).withSelfRel())
        )
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val customerOptional: Optional<Customer> = customerService.findById(id)
        if (customerOptional.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found")
        }
        customerService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping("/")
    fun getAllCustomers(): ResponseEntity<List<CustomerViewDto>> =
        ResponseEntity.status(HttpStatus.OK).body(customerService.getAll().map {
            it.toViewDto().add(linkTo(methodOn(CustomerResource::class.java).getCustomer(it.id!!)).withSelfRel())
        })

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable("id") id: Long, @RequestBody customerUpdateDto: CustomerUpdateDto): ResponseEntity<Any> {
        val customer = this.customerService.findById(id)
        if (customer.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found")
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.customerService.save(customerUpdateDto.toEntity(customer.get())).toViewDto().apply {
            add(linkTo(methodOn(CustomerResource::class.java).getCustomer(id)).withSelfRel()) })
    }
}