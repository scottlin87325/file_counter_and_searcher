package com.example;

import java.io.File;

import com.insyde.base.UtilFile;

public class FileCounter {

	int count = 0;

	public void countFiles(String dirPath) {

		// get files from folder
		File[] files = UtilFile.dirList(dirPath);

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					// if file is directory, recursive
					countFiles(file.getAbsolutePath());
				} else {
					// if is file, count++
					count++;
				}
			}
		}
	}

	public int getResult() {
		return count;
	}
}
