package com.lguplus.medialog.project.common.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class PathUtils {

	/**
	 * @return d:/Downloads/aaa.txt --> d:/Downloads
	 */
	public static String getDirName(String path) {
		String name = FilenameUtils.getName(path);
		int idx = path.indexOf(name);
		return path.substring(0, idx > 0 ? idx - 1 : 0);
	}

	public static File mkDirs(String dir) {
		File file = new File(dir);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file;
	}

}
