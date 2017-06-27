package com.test.service;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.controller.RestUploadController;


import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service
public class WritetoExcel {
	    private final Logger logger = LoggerFactory.getLogger(WritetoExcel.class);
        private WritableCellFormat timesBoldUnderline;
	    private WritableCellFormat times;
	    
	 
	    private String uplFileLink;
	    private String Name;
	    private String City;
	    private String phNum;
	    String outputFile;
	    private static int counter = 1;
	    private static int uniqueId = 101;
	    
	    
	    public String getUplFile() {
			return uplFileLink;
		}

		public void setUplFile(String uploadedlink) {
			this.uplFileLink = uploadedlink;
		}

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public String getCity() {
			return City;
		}

		public void setCity(String city) {
			City = city;
		}
		
		
	   public String getPhNum() {
			return phNum;
		}

		public void setPhNum(String phNum) {
			this.phNum = phNum;
		}

	public void setOutputFile(String outputFile) {
	    this.outputFile = outputFile;
	    }

	    public int write() throws IOException, WriteException {
	        File file = new File(outputFile);
	        WorkbookSettings wbSettings = new WorkbookSettings();

	        wbSettings.setLocale(new Locale("en", "EN"));
	        
	        if (!file.exists())
	        {
	        	WritableWorkbook workbook =Workbook.createWorkbook(file, wbSettings);
	        	workbook.createSheet("Report", 0);
	 	        WritableSheet excelSheet = workbook.getSheet(0);
	 	        createLabel(excelSheet);
	 	        createContent(excelSheet);

	 	        workbook.write();
	 	        workbook.close();
	        }
	        else 
	        {
	        	 try {
	        		 logger.debug("Trying to find the existing excel sheet");
	        		 logger.debug(file.toString());
	        		 Workbook workbookget =Workbook.getWorkbook(new File(outputFile), wbSettings);
	        		 WritableWorkbook workbookcopy = Workbook.createWorkbook(new File(outputFile), workbookget);
	        		 workbookcopy.getSheet("Report");
		 	         WritableSheet excelSheet =  workbookcopy.getSheet(0);
		 	         //createLabel(excelSheet);
		 	         createContent(excelSheet);
		 	         workbookcopy.write();
		 	         workbookcopy.close();
				} catch (BiffException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	     
	        }
	        
	        return uniqueId;
	    }

	    private void createLabel(WritableSheet sheet)
	            throws WriteException {
	        // Lets create a times font
	        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
	        // Define the cell format
	        times = new WritableCellFormat(times10pt);
	        // Lets automatically wrap the cells
	        times.setWrap(true);

	        // create create a bold font with unterlines
	        WritableFont times10ptBoldUnderline = new WritableFont(
	                WritableFont.TIMES, 10, WritableFont.BOLD, false,
	                UnderlineStyle.SINGLE);
	        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	        // Lets automatically wrap the cells
	        timesBoldUnderline.setWrap(true);

	        CellView cv = new CellView();
	        cv.setFormat(times);
	        cv.setFormat(timesBoldUnderline);
	        cv.setAutosize(true);

	        // Write a few headers
	        addCaption(sheet, 0, 0, "Applicant ID");
	        addCaption(sheet, 1, 0, "Name");
	        addCaption(sheet, 2, 0, "City");
	        addCaption(sheet, 3, 0, "Phone Number");
	        addCaption(sheet, 4, 0, "Resume Link");

	    }

	    private void createContent(WritableSheet sheet) throws WriteException,
	            RowsExceededException {
	            
	            //Add the details to row and increment counter.
	    	
	    	    uniqueId++; 
	            counter++;
	       
	            // First column
	            addNumber(sheet, 0, counter, uniqueId);
	            
	            // Second column
	            addLabel(sheet, 1, counter, Name);
	            
	            //Third column
	            addLabel(sheet,2,counter,City);
	            
	            //Fourth Column
	            addLabel(sheet,3,counter,phNum);
	            
	            
	            //5th Column (only path mentioned)
	            addLabel(sheet,4,counter,uplFileLink);
	            
	           /* if (!uplFileLink[2].equals(null))
	            {
	            //6th Column (only path mentioned)
	            addLabel(sheet,5,i,uplFileLink[2]);
	            }*/

	    }

	    private void addCaption(WritableSheet sheet, int column, int row, String s)
	            throws RowsExceededException, WriteException {
	        Label label;
	        label = new Label(column, row, s, timesBoldUnderline);
	        sheet.addCell(label);
	    }

	    private void addNumber(WritableSheet sheet, int column, int row,
	            Integer integer) throws WriteException, RowsExceededException {
	        Number number;
	        number = new Number(column, row, integer, times);
	        sheet.addCell(number);
	    }

	    private void addLabel(WritableSheet sheet, int column, int row, String s)
	            throws WriteException, RowsExceededException {
	        Label label;
	        label = new Label(column, row, s, times);
	        sheet.addCell(label);
	    }

	   /* public static void main(String[] args) throws WriteException, IOException {
	        WriteExcel test = new WriteExcel();
	        test.setOutputFile("c:/temp/lars.xls");
	        test.write();
	        System.out
	                .println("Please check the result file under c:/temp/lars.xls ");
	    }*/
	}


