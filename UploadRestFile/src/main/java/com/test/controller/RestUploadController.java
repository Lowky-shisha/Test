package com.test.controller;

import com.test.model.Applicant;
import com.test.service.ReadFromExcel;
import com.test.service.WritetoExcel;

import jxl.write.WriteException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;



@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);
    
    @Autowired
    WritetoExcel wrt2xcel;
    
    @Autowired
    ReadFromExcel readfrmxcel;
    
    @Autowired 
    Applicant applicant;
    
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER;
    
    Properties prop = new Properties();

    //file upload
   @PostMapping("/api/upload/multi")
   // @RequestMapping(value="/upload", method=RequestMethod.POST)
            public ResponseEntity<?> uploadFile(
            @RequestParam("name") String name,
            @RequestParam("city") String city,
            @RequestParam("phNum") String phNum,
            @RequestParam("files") MultipartFile[] uploadfiles) throws WriteException {
	       int uId;
	       String uploadedlink;

        logger.debug("File upload!");
        /*logger.debug(extraField);
        logger.debug(extraField1);
        logger.debug(extraField2);
*/
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);}

        try {
            //save the upload file to destination and return the path
            uploadedlink= saveUploadedFiles(Arrays.asList(uploadfiles));
            
            //set fields to persist data in excel
        	wrt2xcel.setName(name);
        	wrt2xcel.setCity(city);
        	wrt2xcel.setPhNum(phNum);
        	wrt2xcel.setUplFile(uploadedlink);       	
        	wrt2xcel.setOutputFile(UPLOADED_FOLDER+"ApplicantDB.xls");
        	uId =wrt2xcel.write();
        	

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>("Successfully uploaded - "+new File(uploadedlink).getName() +" --  with unique id  " + uId ,HttpStatus.OK);

    }
    //save file
    private String saveUploadedFiles(List<MultipartFile> files) throws IOException {
    	String []uploadedlink = new String[2]; 
    	int n=1;
    	//get the file upload location form propertied file
    	prop.load(getClass().getResourceAsStream("/application.properties"));
    	UPLOADED_FOLDER  = prop.getProperty("upload_Folder");
    	 logger.debug("upload folder"+UPLOADED_FOLDER);
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; //next 
            }
            byte[] bytes = file.getBytes();
            logger.debug(file.getOriginalFilename());
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            uploadedlink[1] = path.toString();
            n=n+1;          
        }
        return uploadedlink[1];
    }
   
   @GetMapping("/downloadfile")
   public ResponseEntity<InputStreamResource> downloadfile(@RequestParam(value="id", defaultValue="109")String id) throws WriteException, FileNotFoundException, IOException {
	       String uId = id;
	   	   prop.load(getClass().getResourceAsStream("/application.properties"));
    	   UPLOADED_FOLDER  = prop.getProperty("upload_Folder");
	       logger.debug("in download"+uId);
	       HttpHeaders respHeaders = new HttpHeaders();
	       try {
	    	   readfrmxcel.setInputFile( UPLOADED_FOLDER +"ApplicantDB.xls" );
			  /* String flink = readfrmxcel.read(uId);
			   String  fileName = new File(flink).getName();*/
	    	   applicant= readfrmxcel.read(uId);
			   String  fileName = new File(applicant.getFile()).getName();
			   //logger.debug(applicant.getFile());
			    MediaType md = MediaType.MULTIPART_FORM_DATA;
			    respHeaders.setContentType(md);
			    respHeaders.setContentLength(12345678);
			    respHeaders.setContentDispositionFormData("attachment",fileName);
			    InputStreamResource isr = new InputStreamResource(new FileInputStream(applicant.getFile()));
			    
			    
			    respHeaders.set("name", applicant.getFname());
			    respHeaders.set("city", applicant.getCity());
			    respHeaders.set("phone number", applicant.getPhone_num());
			    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);

		} catch (IOException e) {
			// TODO Auto-generated catch block		
			e.printStackTrace();
			 return new ResponseEntity <InputStreamResource>(HttpStatus.BAD_REQUEST);
		}
	     
   
    
}
		   
}
