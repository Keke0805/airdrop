package com.swufe.servlet;


import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.jupiter.api.Test;

class FileServletTest {

    @Test
    void getDiskFileItemFactory() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        System.out.println(factory);

    }

    @Test
    void getServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        System.out.println(upload);
    }

}