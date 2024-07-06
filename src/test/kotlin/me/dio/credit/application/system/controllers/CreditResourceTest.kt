package me.dio.credit.application.system.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.controllers.dtos.CreditDto
import me.dio.credit.application.system.controllers.dtos.CustomerDto
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.enumerators.Status
import me.dio.credit.application.system.repositories.ICreditRepository
import me.dio.credit.application.system.repositories.ICustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var customerRepository: ICustomerRepository

    @Autowired
    private lateinit var creditRepository: ICreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/credits/"
    }

    @BeforeEach
    fun setup() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @AfterEach
    fun teardown() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(customerId = customer.id!!)
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDto.creditValue.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(creditDto.numberOfInstallment))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income.toInt()))
    }

    @Test
    fun `should not create a credit the dayFirstInstallment is in past and return 400 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(
            customerId = customer.id!!, dayFirstInstallment =
            LocalDate.now().minusDays(10)
        )
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details['dayFirstInstallment']").value("must be a future date")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should find all credits by customer id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(
            customerId = customer.id!!
        )
        val credit: Credit = creditRepository.save(creditDto.toEntity())
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL${customer.id}")
                .accept(MediaType.APPLICATION_JSON)

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(creditDto.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallment").value(creditDto.numberOfInstallment))
    }

    @Test
    fun `should find credit by customer id and credit code and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(
            customerId = customer.id!!
        )
        val credit: Credit = creditRepository.save(creditDto.toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL${customer.id}/${credit.creditCode}")
                .accept(MediaType.APPLICATION_JSON)

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDto.creditValue.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(creditDto.numberOfInstallment))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income.toInt()))
    }

    @Test
    fun `should not find credit by invalid customer id and valid credit code and return 400 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(
            customerId = customer.id!!
        )
        val credit: Credit = creditRepository.save(creditDto.toEntity())
        val fakeId: Int = 0
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL${fakeId}/${credit.creditCode}")
                .accept(MediaType.APPLICATION_JSON)

        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.CreditServiceException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details['null']").value("Contact admin")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not find credit by valid customer id and invalid credit code and return 400 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val creditDto: CreditDto = buildCreditDto(
            customerId = customer.id!!
        )
        creditRepository.save(creditDto.toEntity())
        val fakeUUID: UUID = UUID.randomUUID()
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL${customer.id}/${fakeUUID}")
                .accept(MediaType.APPLICATION_JSON)

        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.CreditServiceException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details['null']").value("Credit code $fakeUUID not found")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }
}

private fun buildCreditDto(
    creditValue: BigDecimal = BigDecimal.valueOf(1000),
    dayFirstInstallment: LocalDate = LocalDate.now().plusDays(10),
    numberOfInstallment: Int = 10,
    customerId: Long
) = CreditDto(
    creditValue = creditValue,
    dayFirstInstallment = dayFirstInstallment,
    numberOfInstallment = numberOfInstallment,
    customerId = customerId,
)


private fun buildCustomerDto(
    firstName: String = "Teste",
    lastName: String = "teste",
    cpf: String = "111.111.111-11",
    email: String = "teste@gmail.com",
    password: String = "12345",
    zipCode: String = "00000000",
    street: String = "rua any",
    income: BigDecimal = BigDecimal.valueOf(1000.0),
) = CustomerDto(
    firstName = firstName,
    lastName = lastName,
    cpf = cpf,
    email = email,
    password = password,
    street = street,
    zipCode = zipCode,
    income = income
)