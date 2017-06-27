package com.test.service;

	import java.io.File;
	import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.model.Applicant;

import jxl.Cell;
	import jxl.CellType;
	import jxl.Sheet;
	import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;



	@Service
	public class ReadFromExcel {

	    private String inputFile;
	    
	    @Autowired
	    Applicant applicant;
	    
	    private final Logger logger = LoggerFactory.getLogger(ReadFromExcel.class);

	    public void setInputFile(String inputFile) {
	        this.inputFile = inputFile;
	    }
	    
	    String filename;

	    public Applicant read(String uId) throws IOException  {
	    	WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
	        Workbook w;
	        try {
	            w = Workbook.getWorkbook(new File(inputFile), wbSettings);
	            // Get the first sheet
	            Sheet sheet = w.getSheet(0);
	            // Loop over first 10 column and lines

	           /* for (int j = 0; j < sheet.getColumns(); j++) {
	                for (int i = 0; i < sheet.getRows(); i++) {*/
	                    Cell cell = sheet.getCell(0, 0);
	                    CellType type = cell.getType();
	                    if (type == CellType.LABEL) 
	                    {
	                        if (cell.getContents().equals("Applicant ID"))
	                        {
	                        	 for (int j = 0; j < sheet.getColumns(); j++){  
	                        	for (int i = 1; i < sheet.getRows(); i++)
	                        	  {
	                        	    cell = sheet.getCell(j, i);
	                        		if (cell.getContents().equals(uId))
	                        		{
	                        		logger.debug("found");
	                        		++j;
	                        		//name
	                        		cell= sheet.getCell(1,1);
	                        				applicant.setFname(cell.getContents());
	                        				logger.debug(cell.getContents());
	                        				j++;
	                        	    //city
	                        		cell =  sheet.getCell(j, i);
	                        		        applicant.setCity(cell.getContents());
	                        		        logger.debug(cell.getContents());
	                        		        j++;
	                        		 //phone number
	                        		  cell =  sheet.getCell(j, i);
	                        		        applicant.setPhone_num(cell.getContents());   
	                        		        logger.debug(cell.getContents());
	                        		        j++;
	                        		 
	                        		   cell = sheet.getCell(j, i);
	                        		   		applicant.setFile(cell.getContents());
	                        		   		logger.debug(cell.getContents());
	                        		   		
	                        	  	}
	                        	  }
	                        	 }
	                        }

	                     }
	            
	               }
	                    catch (BiffException e) {
	            e.printStackTrace();
	        }
			return applicant;
	    }
	    
	}    

	

