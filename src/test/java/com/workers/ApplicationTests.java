package com.workers;

import com.workers.business.common.scheduler.DraftOrderScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class ApplicationTests {

	@MockBean
	private DraftOrderScheduler draftOrderScheduler;

	@Test
	void contextLoads() {
	}

}
