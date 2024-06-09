package me.dio.credit.application.system.service.impl

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repositories.ICustomerRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    lateinit var customerRepository: ICustomerRepository

    @InjectMockKs
    lateinit var customerService: CustomerService

    @Test
    fun `should create customer`() {
        //given
        val fakeCustomer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer
        //when
        val returnedCostumer: Customer = customerService.save(fakeCustomer)
        //then
        Assertions.assertThat(returnedCostumer).isNotNull
        Assertions.assertThat(returnedCostumer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should find customer by id`() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        //when
        val returnedOptionalCostumer: Optional<Customer> = customerService.findById(fakeId)
        //then
        Assertions.assertThat(returnedOptionalCostumer).isPresent
        Assertions.assertThat(returnedOptionalCostumer).get().isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should not find customer by id and return a empty optional`() {
        //given
        val fakeId: Long = Random.nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        //when
        val returnedOptionalCostumer: Optional<Customer> = customerService.findById(fakeId)
        //then
        Assertions.assertThat(returnedOptionalCostumer).isEmpty
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should delete customer by id`() {
        // given
        val fakeId: Long = Random.nextLong()
        every { customerRepository.deleteById(fakeId) } just runs
        //when
        customerService.delete(fakeId)
        //then
        verify(exactly = 1) { customerRepository.deleteById(fakeId) }
    }

    @Test
    fun `should returns costumers`() {
        //given
        every { customerRepository.findAll() } returns listOf(buildCustomer(), buildCustomer(), buildCustomer())
        //when
        val returnedCustomers = customerService.getAll()
        //then
        Assertions.assertThat(returnedCustomers).hasSize(3)
        returnedCustomers.forEach { Assertions.assertThat(it).isNotNull }
        returnedCustomers.forEach { Assertions.assertThat(it).isEqualTo(buildCustomer()) }
    }

    private fun buildCustomer(
        firstName: String = "Teste",
        lastName: String = "teste",
        cpf: String = "111.111.111-11",
        email: String = "teste@gmail.com",
        password: String = "12345",
        zipCode: String = "00000000",
        street: String = "rua any",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName, lastName = lastName, cpf = cpf, email = email, password = password, address = Address(
            street = street, zipCode = zipCode
        ), income = income, id = id
    )
}