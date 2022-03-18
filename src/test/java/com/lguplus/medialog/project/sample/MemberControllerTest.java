package com.lguplus.medialog.project.sample;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

// 전체 빈을 로딩한다
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
	private MockMvc mockMvc;
	@MockBean
	private MemberService memberService;

	@BeforeEach
    public void setUp(WebApplicationContext ctx) {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
        		.addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

	@Test
	public void list() throws Exception {
		List<Member> members = new ArrayList<>();
		members.add(Member.builder().name("John").build());
		given(memberService.list()).willReturn(members);

		mockMvc.perform(get("/api/member"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("John")));
	}

}
