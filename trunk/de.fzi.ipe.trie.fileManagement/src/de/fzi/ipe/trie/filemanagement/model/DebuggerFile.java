package de.fzi.ipe.trie.filemanagement.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple extension of file to represent RDF or Rule files in the context of the trie file management.
 *
 */
public class DebuggerFile {

	private File file; 
	
	private static final long serialVersionUID = 1L;


	public DebuggerFile(File file) {
		this.file = file;
	}
	
	
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
	
	public String getName() {
		return file.getName();
	}
	
	public boolean isTurtleFile() {
		return file.getPath().toLowerCase().endsWith(".turtle"); 
	}
	
	public boolean isRuleFile() {
		return file.getPath().toLowerCase().endsWith(".rule");
	}
	
	public InputStream openInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	public File getFile() {
		return file;
	}

	public void setContent(String string) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		try {
			writer.append(string);
		} finally {
			writer.close();
		}
	}
	
	public String getContent() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			StringBuilder builder = new StringBuilder();
			String temp = reader.readLine();
			while (temp != null) {
				builder.append(temp);
				builder.append("\n");
				temp = reader.readLine();
			}
			return builder.toString();
		} finally { 
			reader.close();
		}
	}

	
	
}
