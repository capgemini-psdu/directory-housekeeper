package com.capgemini.housekeeper;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;


public class DirectoryHousekeeper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryHousekeeper.class);
		
	private final long DAY_IN_MS = 86400000L; // Day in milliseconds 24L * 60L * 60L * 1000L
	
    private String directoryPath;
	
	private long daysBack;
	
	private String filenameRegex;
	
	public DirectoryHousekeeper(String directoryPath, long daysBack, String filenameRegex) {
		this.directoryPath = directoryPath;
		this.daysBack = daysBack;
		this.filenameRegex = filenameRegex;
	}
	
	/**
	 * Scheduler to housekeep a directory.
	 */
	@Scheduled(cron = "${directory.housekeeping.schedule}")
	public void housekeep() {
		 
		LOGGER.info("[Scheduler Task] Started housekeeping of directory {} for filename regex {}.", directoryPath, filenameRegex);
		
		final File directory = new File(directoryPath);
		
		if(!directory.exists()){
			LOGGER.info("[Scheduler Task] Housekeep of directory {} failed because directory does not exist.", directoryPath);
			return;
		}
		
		final File[] fileList = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.matches(filenameRegex);
			}
		});
		
		long purgeTime = System.currentTimeMillis() - (daysBack * DAY_IN_MS);
	
		for(File file : fileList){
			if(file.lastModified() < purgeTime){
				LOGGER.info("[Scheduler Task] Deleting file: {}", file.getName());
				file.delete();
			}
		}
	
		LOGGER.info("[Scheduler Task] Completed housekeeping of directory {} for filename regex {}.", directoryPath, filenameRegex);
	}
}
