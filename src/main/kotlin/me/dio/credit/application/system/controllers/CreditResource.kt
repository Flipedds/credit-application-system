package me.dio.credit.application.system.controllers

import jakarta.validation.Valid
import me.dio.credit.application.system.controllers.dtos.CreditDto
import me.dio.credit.application.system.controllers.dtos.CreditViewDto
import me.dio.credit.application.system.controllers.dtos.CreditViewListDto
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/credits")
class CreditResource(
    val creditService: CreditService
) {
    @PostMapping("/")
    fun saveCredit(@RequestBody @Valid credit: CreditDto): ResponseEntity<CreditViewDto> =
        ResponseEntity.status(HttpStatus.CREATED).body(this.creditService.save(credit.toEntity()).toViewDto())

    @GetMapping("/{customerId}")
    fun findAllByCustomer(@PathVariable("customerId") customerId: Long): ResponseEntity<List<CreditViewListDto>> =
        ResponseEntity.status(HttpStatus.OK)
            .body(this.creditService.findAllByCustomer(customerId).map { it.toListViewDto() })

    @GetMapping("/{customerId}/{creditCode}")
    fun findByCreditCode(
        @PathVariable("customerId") customerId: Long,
        @PathVariable("creditCode") creditCode: UUID
    ): ResponseEntity<CreditViewDto> =
        ResponseEntity.status(HttpStatus.OK)
            .body(this.creditService.findByCreditCode(customerId, creditCode).toViewDto())
}