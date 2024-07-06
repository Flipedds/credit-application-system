package me.dio.credit.application.system.repository

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repositories.ICreditRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IcreditRepositoryTest {

    @Autowired
    lateinit var creditRepository: ICreditRepository

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer

    private lateinit var creditOne: Credit

    @BeforeEach
    fun setup() {
        customer = testEntityManager.persist(buildCustomer())
        creditOne = testEntityManager.persist(
            buildCredit(
                customer = customer,
                creditCode = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8")
            )
        )
    }

    @Test
    fun `should find credit by credit code`() {
        //given
        val creditCodeOne = UUID.fromString("5c158da0-e68d-4c4d-a9f4-57f53ba164a8")
        //when
        val fakeCredit = creditRepository.findByCreditCode(creditCodeOne)
        //then
        Assertions.assertThat(fakeCredit).isNotNull
        Assertions.assertThat(fakeCredit).isSameAs(creditOne)
    }

    @Test
    fun `should find all credits by customer id`(){
        //given
        val customerId: Long? = customer.id
        //when
        val creditList: List<Credit> = creditRepository.findAllByCustomerId(customerId!!)
        //then
        Assertions.assertThat(creditList).isNotNull
        Assertions.assertThat(creditList.size).isEqualTo(1)
        creditList.forEach { Assertions.assertThat(it).isEqualTo(creditOne) }
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
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            street = street,
            zipCode = zipCode
        ), income = income
    )


    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500),
        numberOfInstallment: Int = 5,
        creditCode: UUID,
        dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        creditCode = creditCode,
        numberOfInstallment = numberOfInstallment,
        dayFirstInstallment = dayFirstInstallment,
        customer = customer
    )
}