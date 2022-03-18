package com.lguplus.medialog.project.sample;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lguplus.medialog.project.config.SecurityConfig;

// Controller와 관련된 빈만 로딩하여 빠르지만 security 관련 설정이 필요하다.
// http://blog.devenjoy.com/?p=524
@WebMvcTest(
	    value = MemberController.class,
	    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
	    excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MemberControllerOnlyTest {
	private MockMvc mockMvc;
	@MockBean
	private MemberService memberService;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
    public void setUp(WebApplicationContext ctx) {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
        		.addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

	@Test @Disabled
	public void list() throws Exception {
		List<Member> members = new ArrayList<>();
		members.add(Member.builder().name("John").build());
		given(memberService.list()).willReturn(members);

		mockMvc.perform(get("/api/member"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("John")));
	}
	
	@Test //@Disabled
	public void select() throws Exception {
		Member member = Member.builder().id(1).name("푸우").build();
		given(memberService.select(1)).willReturn(member);

		mockMvc.perform(get("/api/member/1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("푸우")));
	}

	@Test //@Disabled
	public void detail() throws Exception {
		// url로 파라미터 전달
		String url = "/api/member/detail?id=1&name=푸우&register=20191214063420&update=20191225052034";
		
		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andReturn();
		System.out.println(result.getResponse().getContentAsString());
		
		// Map으로 파라미터 전달
		MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
		req.add("id", "2");
		req.add("name", "바아");
		req.add("register", "20201122172344");
		req.add("update", "20210812154322");

		mockMvc.perform(get("/api/member/detail")
				.params(req))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test @Disabled
	void insert() throws Exception {
		Map<String, String> req = new HashMap<>();
		req.put("id", "2");
		req.put("name", "바아");
		req.put("register", "20201122172344");
		req.put("update", "20210812154322");
		String content = objectMapper.writeValueAsString(req);

		mockMvc.perform(post("/api/member")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
}
