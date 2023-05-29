package com.climpoche.user.controller;

import com.climpoche.user.dto.UserDTO;
import com.climpoche.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.io.Files;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.*;
import java.nio.file.Paths;

@RestController
public class User {

    @Autowired
    UserService userService;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @GetMapping(value = "/user")
    public String get(){
        //save one user for everyget to check
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Sameesh");
        userDTO.setRoleName("support");
        userDTO.setStatus("ACTIVE");
        return userService.createUserDTO(userDTO).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(gridFsTemplate.getResource(filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    //new expirimentation
    @GetMapping(value = "/getFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    private ResponseEntity<byte[]> getFile() throws IOException {
        File tempDir = Files.createTempDir();
        File file = Paths.get(tempDir.getAbsolutePath(), "fileName.txt").toFile();
        String data = "Some data"; //
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(data).flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //save on grid fs
        ObjectId fileId = gridFsTemplate.store(new FileInputStream(file), "file.txt");
        byte[] zippedData = toByteArray(new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(ContentDisposition.builder("attachment").filename("file.txt").build());
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(zippedData.length);
        return ResponseEntity.ok().headers(httpHeaders).body(zippedData);
    }


    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[in.available()];
        int len;
        // read bytes from the input stream and store them in buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
    //new expirimentation ends


    @GetMapping(value = "/zip")
    public void downloadZip(HttpServletResponse response) {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=download.zip");
        response.setStatus(HttpServletResponse.SC_OK);

        List<String> fileNames = new ArrayList<>();
        fileNames.add("build.gradle");//service.getFileName();

        System.out.println("############# file size ###########" + fileNames.size());

        try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (String file : fileNames) {
                FileSystemResource resource = new FileSystemResource(file);
                //File file1= null;
                //FileInputStream in = new FileInputStream(file1);
                //in
                ZipEntry e = new ZipEntry(resource.getFilename());
                // Configure the zip entry, the properties of the file
                e.setSize(resource.contentLength());
                e.setTime(System.currentTimeMillis());
                // etc.
                zippedOut.putNextEntry(e);
                // And the content of the resource:
                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (Exception e) {
            // Exception handling goes here
        }
    }

}
