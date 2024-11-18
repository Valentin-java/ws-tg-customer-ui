package ru.helper.worker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.helper.worker.business.common.scheduler.DraftOrderScheduler;

@SpringBootTest
@ActiveProfiles("dev")
class WorkerApplicationTests {

	@MockBean
	private DraftOrderScheduler draftOrderScheduler;

	@Test
	void contextLoads() {
	}

}
