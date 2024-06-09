package me.dio.credit.application.system.service.impl

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.CreditServiceException
import me.dio.credit.application.system.repositories.ICreditRepository
import me.dio.credit.application.system.service.ICustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: ICreditRepository

    @MockK
    lateinit var customerService: ICustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create a credit request`() {
        //given
        val fakeCredit: Credit = buildCredit()
        every { customerService.findById(1) } returns Optional.of(fakeCredit.customer!!)
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        //when
        val returnedCredit: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(returnedCredit).isNotNull
        Assertions.assertThat(returnedCredit).isSameAs(fakeCredit)
        verify(exactly = 1) { customerService.findById(1) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should returns credits of a specify customer`() {
        //given
        val fakeId: Long = Random.nextLong()
        every { creditRepository.findAllByCustomerId(fakeId) } returns listOf(
            buildCredit(customerId = fakeId), buildCredit(customerId = fakeId)
        )
        //when
        val returnedCredits: List<Credit> = creditService.findAllByCustomer(fakeId)
        //then
        Assertions.assertThat(returnedCredits).isNotNull
        Assertions.assertThat(returnedCredits).hasSize(2)
        returnedCredits.forEach { Assertions.assertThat(it).isNotNull }
        returnedCredits.forEach {
            Assertions.assertThat(it).isEqualTo(
                buildCredit(customerId = fakeId)
            )
        }
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeId) }
    }

    @Test
    fun `should return specific credit of a specific customer by creditCode`() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCreditCode: UUID = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8")
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns buildCredit(customerId = fakeId)
        //when
        val returnedCredit: Credit = creditService.findByCreditCode(fakeId, fakeCreditCode)
        //then
        Assertions.assertThat(returnedCredit).isNotNull
        Assertions.assertThat(returnedCredit).isEqualTo(buildCredit(customerId = fakeId))
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should not return specific credit of a specific customer by creditCode and return Exception { creditCode not found }`() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCreditCode: UUID = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8")
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null
        //when
        //then
        Assertions.assertThatExceptionOfType(CreditServiceException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId, fakeCreditCode) }
            .withMessage("Credit code $fakeCreditCode not found")
    }

    @Test
    fun `should not return specific credit of a specific customer by creditCode and return Exception { Contact admin }`() {
        //given
        val fakeId: Long = 100
        val fakeCreditCode: UUID = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8")
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns buildCredit()
        //when
        //then
        Assertions.assertThatExceptionOfType(CreditServiceException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId, fakeCreditCode) }.withMessage("Contact admin")
    }

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(1200),
        numberOfInstallment: Int = 10,
        dayFirstInstallment: LocalDate = LocalDate.now(),
        customerId: Long = 1
    ): Credit = Credit(
        creditCode = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8"),
        creditValue = creditValue,
        numberOfInstallment = numberOfInstallment,
        dayFirstInstallment = dayFirstInstallment,
        customer = Customer(id = customerId)
    )
}