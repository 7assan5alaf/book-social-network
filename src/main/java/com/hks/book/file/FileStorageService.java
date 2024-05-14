package com.hks.book.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {

	@Value("${application.file.uplaod-file-path}")
	private String fileUploadPath;

	public String saveFile(@Nonnull MultipartFile file,@Nonnull Long id) {
		final String subFilePath = "users" + File.separator + id;

		return uploadFile(file, subFilePath);
	}

	private String uploadFile(@Nonnull MultipartFile file, @Nonnull String subFilePath) {
		final String finalPath = fileUploadPath + File.separator + subFilePath;
		File targetFile = new File(finalPath);
		if (!targetFile.exists()) {
			boolean createFile = targetFile.mkdirs();
			if (!createFile) {
				log.warn("File to create the target file");
				return null;
			}
		}
		final String fileExtention = getFileExtention(file.getOriginalFilename());
		final String targetFilePath = finalPath + File.separator + System.currentTimeMillis() + fileExtention;

		Path path = Paths.get(targetFilePath);
		try {
			Files.write(path, file.getBytes());
			log.info("Save file in " + targetFilePath);
			return targetFilePath;
		} catch (IOException e) {
			log.error("file not saved :" + e.getMessage());
		}

		return null;
	}

	private String getFileExtention(String filename) {
		if (filename.isEmpty() || filename == null)
			return "";

		int dotIndex = filename.indexOf(".");
		if (dotIndex == -1) {
			return "";
		}
		return "."+filename.substring(dotIndex+1).toLowerCase();
	}

}
