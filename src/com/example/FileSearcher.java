package com.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.insyde.base.UtilFile;

public class FileSearcher {

	List<String> result = new ArrayList<>();

	public void searchInDirectory(String directory, String keyword) {

		// get files from folder
		File[] files = UtilFile.dirList(directory);

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					// if file is directory, recursive
					searchInDirectory(file.getAbsolutePath(), keyword);
				} else {
					if (file.getName().contains(keyword)) { // Method chaining
						// if file with keyword, add to result arraylist
						result.add(file.getAbsolutePath());
					}
				}
			}
		}
	}

	public List<String> getResult() {
		return result;
	}

}
