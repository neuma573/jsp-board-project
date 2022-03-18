package com.lguplus.medialog.project.func;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lguplus.medialog.project.common.dto.Result;
import com.lguplus.medialog.project.common.utils.DownloadUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DownloadController {
	@Autowired
	private ApplicationContext context;

	@GetMapping("/api/download")
	public void download(@RequestParam String name, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String location = String.format("classpath:/sample/%s", name);
		Resource resource = context.getResource(location);

		DownloadUtils.download(resource.getFile(), request, response);
	}
	
	/**
	 * DTO 없이 직접 @RequestParam MultipartFile upfile 해도 된다.
	 * Dropzone, Plupload 같은 javascript lib.을 사용하면 chunk로 나눠서 올리므로 효율적이다.
	 */
	@PostMapping("/api/upload")
	@ResponseBody
	public Result<?> upload(UploadData data) throws IOException {
		if (data.getUpfile() != null) {
			upload(data.getUpfile());
		}
		
		if (data.getUpfiles() != null) {
			for (MultipartFile upfile : data.getUpfiles()) {
				upload(upfile);
			}
		}
		
		return new Result<Void>();
	}
	
	private void upload(MultipartFile upfile) throws IOException {
		// IE는 full path가 올라온다
		String name = FilenameUtils.getName(upfile.getOriginalFilename());
		log.debug("upload file name = [{}]", name);
		File save = new File("c:/Users/medialog/Downloads/tmp", name);
		DownloadUtils.upload(upfile, save);
	}

	@Data
	public static class UploadData {
		private MultipartFile upfile;
		private List<MultipartFile> upfiles;
		private String uploader;
	}

}
