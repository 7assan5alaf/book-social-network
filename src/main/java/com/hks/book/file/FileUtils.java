package com.hks.book.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

	public static byte[] readFileFromLocatin(String fileUrl) {
		if (StringUtils.isBlank(fileUrl))
			return null;

		try {
			Path path = new File(fileUrl).toPath();
			return Files.readAllBytes(path);
		} catch (IOException e) {
			log.error("No file found in the path{}" + e.getMessage());
		}
		return null;

	}
//
//	public static boolean deleteFileFromLoction(String fileUrl) {
//		if (StringUtils.isBlank(fileUrl))
//			return false;
//		try {
//			Path path = new File(fileUrl).toPath();
//			return Files.deleteIfExists(path);
//		} catch (IOException e) {
//			log.error("No file found in the path{}" + e.getMessage());
//		}
//		return false;
//	}

}
