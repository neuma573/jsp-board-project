package com.lguplus.medialog.project.sample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MemberController {
	
	private MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/member")
	public ResponseEntity<List<Member>> list() {
		List<Member> response = memberService.list();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/member/{id}")
	public ResponseEntity<Member> select(@PathVariable("id") int id) throws Exception {
		log.debug("....... select id = [{}]", id);
		Member member = memberService.select(id);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@GetMapping("/member/detail")
	public ResponseEntity<Member> detail(Member req) throws Exception {
		log.debug("....... detail req = [{}]", req);
		return new ResponseEntity<>(req, HttpStatus.OK);
	}
	
	@PostMapping("/member")
	public ResponseEntity<?> insert(@RequestBody Member member) throws Exception {
		log.debug("....... insert member = [{}]", member);
		int response = memberService.insert(member);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/member")
	public ResponseEntity<?> update(@RequestParam("id") int id, @RequestParam("name") String name) throws Exception {
		Member member = Member.builder().id(id).name(name).build();
		int response = memberService.update(member);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/member/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) throws Exception {
		int response = memberService.delete(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
