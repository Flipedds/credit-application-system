package me.dio.credit.application.system.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.controllers.CreditResourceTest.Companion
import me.dio.credit.application.system.controllers.dtos.CreditDto
import me.dio.credit.application.system.controllers.dtos.CustomerDto
import me.dio.credit.application.system.controllers.dtos.CustomerUpdateDto
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
    @Autowired
    private lateinit var customerRepository: ICustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/customers/"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun teardown() = customerRepository.deleteAll()


    @Test
    fun `should create a costumer and return 201 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto()
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        ) //then
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDto.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerDto.income))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.email))
    }

    @Test
    fun `should not save a costumer with same CPF and return 409 status`() {
        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto: CustomerDto = buildCustomerDto()
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        ) //then
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a costumer with firstName empty and return 400 status`() {
        val customerDto: CustomerDto = buildCustomerDto(firstName = "")
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        ) //then
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find a costumer by id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customer.income.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.email))
    }

    @Test
    fun `should not find a costumer with invalid id and return 404 status`() {
        //given
        val invalidId: Long = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL$invalidId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should get all customers and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(customer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(customer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].income").value(customer.income.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(customer.email))
    }

    @Test
    fun `should delete customer by id`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }


    @Test
    fun `should not delete a costumer with invalid id and return 404 status`() {
        //given
        val invalidId: Long = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL$invalidId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should update customer by id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto = buildCustomerUpdateDto()
        val customerUpdateDtoAsString = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.put("$URL${customer.id}").contentType(MediaType.APPLICATION_JSON)
                .content(customerUpdateDtoAsString)
        ) //then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerUpdateDto.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerUpdateDto.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerUpdateDto.income))
    }

    @Test
    fun `should not update customer by id and return 404 status`() {
        //given
        val invalidId: Long = 2L
        val customerUpdateDto = buildCustomerUpdateDto()
        val customerUpdateDtoAsString = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.put("$URL$invalidId").contentType(MediaType.APPLICATION_JSON)
                .content(customerUpdateDtoAsString)
        ) //then
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}


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

private fun buildCustomerUpdateDto(
    firstName: String = "Teste update",
    lastName: String = "teste update",
    income: BigDecimal = BigDecimal.valueOf(5000.0)
): CustomerUpdateDto = CustomerUpdateDto(
    firstName = firstName,
    lastName = lastName,
    income = income
)