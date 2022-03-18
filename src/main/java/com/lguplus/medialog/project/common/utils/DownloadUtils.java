package com.lguplus.medialog.project.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class DownloadUtils {

	public static void download(File file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = file.getName();
		long fileSize = file.length();
		InputStream in = new FileInputStream(file);
		
		// response header
		setDownloadHeader(fileName, request, response);
		response.setHeader("Content-Length", String.valueOf(fileSize));

		// write to response
		OutputStream out = response.getOutputStream();
		try {
			IOUtils.copy(in, out);
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	public static void setDownloadHeader(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userAgent = request.getHeader("User-Agent");
		String fname = userAgent.contains("MSIE") || userAgent.contains("Trident")
				? URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20")
				: new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fname));
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/octet-stream");                
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
	}
	
	public static boolean upload(MultipartFile upfile, File savePath) throws IOException {
		return upload(upfile, savePath, false);
	}
	
	public static boolean upload(MultipartFile upfile, File savePath, boolean append) throws IOException {
		if (upfile == null || upfile.isEmpty())
			return false;
		
		String saveDir = PathUtils.getDirName(savePath.getAbsolutePath());
		PathUtils.mkDirs(saveDir);
//		String type = upfile.getContentType();
//		String name = upfile.getOriginalFilename();
//		long size = upfile.getSize();
//		log.debug("upload file name = [" + name + "], size = [" + size + "], type = [" + type + "]");
		
		if (!append) {
			upfile.transferTo(savePath);
		}
		else {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = upfile.getInputStream();
				out = new FileOutputStream(savePath, true);
				IOUtils.copy(in, out);
			}
			finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
		return true;
	}
}
