package de.fzi.ipe.trie.evaluationlogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import org.eclipse.jface.dialogs.IDialogSettings;



public class FileLogger extends Observable implements LoggerListener {

	private static FileLogger instance;
	
	private String filePath;
	private File currentFile;
	private PrintWriter out; 
	
	private FileLogger() {
		loadFilepath();
		LoggerImpl.getLogger().addListener(this);
		newFile();
	}
	
	public static FileLogger getInstance() {
		if (instance == null) instance = new FileLogger();
		return instance;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		saveFileLocations();
		setChanged();
		notifyObservers();
	}
	
	public String getFileName() {
		return currentFile.getName();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void event(Logger logger, String... data) {
		StringBuilder builder = new StringBuilder();
		for (String s: data) builder.append(s+", ");
		out.println(builder.toString());
		out.flush();
	}
	
	public void newFile() {
		try {
			if (out != null) out.close();
			currentFile = makeNewFile();
			out = new PrintWriter(new FileWriter(currentFile));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}

	private File makeNewFile() {
		for (int i=0;i<10000;i++) {
			String fileNameToTest = makeFileName(i);
			File file = new File(filePath + "\\"+fileNameToTest);
			if (!file.exists()) return file; 
		}
		return null;
	}
	
	private String makeFileName(int i) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder fileName = new StringBuilder();
		fileName.append(format.format(date));
		fileName.append("_"+i+".log");
		return fileName.toString();
	}
	
	
	private void loadFilepath() {
		IDialogSettings favoritesSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileLogger");
		if (wizardSettings != null) {
			String filePath = wizardSettings.get("filePath");
			if (filePath != null) this.filePath = filePath;
		}
	}
	
	public void saveFileLocations() { 
		IDialogSettings favoritesSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileLogger");
		if (wizardSettings == null) {
			wizardSettings = favoritesSettings.addNewSection("fileLogger");
		}
		wizardSettings.put("filePath",filePath);
	}
	
}
