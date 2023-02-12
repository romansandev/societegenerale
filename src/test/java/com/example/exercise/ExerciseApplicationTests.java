package com.example.exercise;

import com.example.exercise.configuration.SocieteGeneraleConfig;
import com.example.exercise.infrastructure.db.springdata.controllers.AccountController;
import com.example.exercise.infrastructure.db.springdata.controllers.ClientController;
import com.example.exercise.infrastructure.db.springdata.dtos.AccountDto;
import com.example.exercise.infrastructure.db.springdata.dtos.ClientDto;
import com.example.exercise.infrastructure.db.springdata.model.BankTransaction;
import com.example.exercise.infrastructure.db.springdata.model.OperationType;
import com.example.exercise.infrastructure.db.springdata.repositories.AccountRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.BankTransactionRepository;
import com.example.exercise.infrastructure.db.springdata.repositories.ClientRepository;
import com.example.exercise.infrastructure.db.springdata.services.BankTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExerciseApplication.class, SocieteGeneraleConfig.class})
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:clientandaccountdata.sql"})
class ExerciseApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BankTransactionService bankTransactionService;

	@Autowired
	BankTransactionRepository bankTransactionRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	ClientController clientController;

	@Autowired
	AccountController accountController;

	@After
	public void afterTests() {
		bankTransactionRepository.deleteAll();
		accountRepository.deleteAll();
		clientRepository.deleteAll();
	}

	@Test
	//Use case 1
	public void testDoDeposit() throws Exception {
		// Getting data ready for testing
		ClientDto client = clientController.getById(1L);
		AccountDto account = accountController.getAccountsByClient(client).get(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/account/" + account.getId() + "/transaction/" + 285 + "/" + OperationType.DEPOSIT.name()))
				.andExpect(status().isCreated());

		verify(bankTransactionService, times(1)).insert(any(BankTransaction.class));
	}

	@Test
	//Use case 2
	public void testDoWithdrawal() throws Exception {
		// Getting data ready for testing
		ClientDto client = clientController.getById(1L);
		AccountDto account = accountController.getAccountsByClient(client).get(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/account/" + account.getId() + "/transaction/" + 285 + "/" + OperationType.WITHDRAWAL.name()))
				.andExpect(status().isCreated());

		verify(bankTransactionService, times(1)).insert(any(BankTransaction.class));
	}

	@Test
	public void testNotSupportedTransaction() throws Exception {
		// Getting data ready for testing
		ClientDto client = clientController.getById(1L);
		AccountDto account = accountController.getAccountsByClient(client).get(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/account/" + account.getId() + "/transaction/" + 285 + "/" + "TEST"))
				.andExpect(status().isNotAcceptable());

		verify(bankTransactionService, times(0)).insert(any(BankTransaction.class));
	}

	@Test
	public void testAccountNotFound() throws Exception {
		// Getting data ready for testing
		ClientDto client = clientController.getById(1L);
		AccountDto account = accountController.getAccountsByClient(client).get(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/account/" + -12 + "/transaction/" + 285 + "/" + OperationType.WITHDRAWAL.name()))
				.andExpect(status().isNotAcceptable());

		verify(bankTransactionService, times(0)).insert(any(BankTransaction.class));
	}

	@Test
	public void testGetHistoric() throws Exception {
		ClientDto client = clientController.getById(1L);
		mockMvc.perform(MockMvcRequestBuilders.get("/client/transaction/historic")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(client)))
				.andExpect(status().isOk());
	}

	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			objectMapper.setDateFormat(DateFormat.getDateTimeInstance());
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
