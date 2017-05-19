package com.kf.data.crawler.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileReaderTools {

	public static List<String> loadUserWords(InputStream input) {
		String line;
		List<String> myWords = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"), 1024);
			while ((line = br.readLine()) != null) {
				line = line.trim().toLowerCase();
				myWords.add(line);
			}
			br.close();
		} catch (IOException e) {
			System.err.println("WARNING: cannot open user words list!");
		}
		return myWords;
	}

	public static void write(List<String> pingpai) {
		try {
			String path = FileReaderTools.class.getClassLoader().getResource("").toURI().getPath();
			FileOutputStream fos = new FileOutputStream(path + File.separator + "usedProxy.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for (String string : pingpai) {
				bw.write(string + "\n");
			}
			bw.flush();
			fos.close();
			osw.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
