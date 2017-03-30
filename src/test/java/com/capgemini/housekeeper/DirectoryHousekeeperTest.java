package com.capgemini.housekeeper;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.housekeeper.DirectoryHousekeeper;

import static org.junit.Assert.*;

public class DirectoryHousekeeperTest {

	private DirectoryHousekeeper bean;
	
	@Before
	public void setUp() {
		deleteTempFiles();
	}

	@After
	public void tearDown() {
		deleteTempFiles();
	}
	
	@Test
	public void testMatchOne() throws Exception {
		createTempFile("src/test/resources/temp/tempfile.xml");
		bean = new DirectoryHousekeeper("src/test/resources/temp", 0, ".*xml");
		bean.housekeep();
		assertEquals(0, countFiles());
	}
	
	@Test
	public void testMatchTwo() throws Exception {
		createTempFile("src/test/resources/temp/tempfile1.xml");
		createTempFile("src/test/resources/temp/tempfile2.xml");
		bean = new DirectoryHousekeeper("src/test/resources/temp", 0, ".*xml");
		bean.housekeep();
		assertEquals(0, countFiles());
	}
	
	@Test
	public void testMatchOneOfTwo() throws Exception {
		createTempFile("src/test/resources/temp/tempfile.xml");
		createTempFile("src/test/resources/temp/tempfile.csv");
		bean = new DirectoryHousekeeper("src/test/resources/temp", 0, ".*xml");
		bean.housekeep();
		assertEquals(1, countFiles());
	}
	
	@Test
	public void testMatchNone() throws Exception {
		createTempFile("src/test/resources/temp/tempfile1.xml");
		createTempFile("src/test/resources/temp/tempfile2.xml");
		bean = new DirectoryHousekeeper("src/test/resources/temp", 0, ".nosuchfile*");
		bean.housekeep();
		assertEquals(2, countFiles());
	}
	
	@Test
	public void testMatchNoneOnDate() throws Exception {
		createTempFile("src/test/resources/temp/tempfile1.xml");
		createTempFile("src/test/resources/temp/tempfile2.xml");
		bean = new DirectoryHousekeeper("src/test/resources/temp", 1, ".*xml");
		bean.housekeep();
		assertEquals(2, countFiles());
	}
	
	private void createTempFile(String path) throws Exception {
		File file = new File(path);
		if (!file.createNewFile()) {
			throw new RuntimeException("Unable to create file during test.");
		}
	}

	private void deleteTempFiles() {
		for (String dirString : new String[]{"src/test/resources/temp"}) {
			File localTempDir = new File(dirString);
			File[] files = localTempDir.listFiles();
			for (File filename : files) {
				if (filename.getName().endsWith(".xml") || filename.getName().endsWith(".csv")) {
					filename.delete();
				}
			}
		}
	}
	
	private int countFiles() {
		int i = 0;
		for (String dirString : new String[]{"src/test/resources/temp"}) {
			File localTempDir = new File(dirString);
			File[] files = localTempDir.listFiles();
			for (File filename : files) {
				if (filename.getName().endsWith(".xml") || filename.getName().endsWith(".csv")) {
					i++;
				}
			}
		}
		return i;
	}

}
