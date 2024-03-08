package com.example.hobby_airsoft;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.*;

public class ReportGenerator {
    public JasperReport compileJRXML(String jrxmlFilePath) throws JRException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(jrxmlFilePath);
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        return jasperReport;
    }
}