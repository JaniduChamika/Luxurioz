///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package model;
//
//import java.util.HashMap;
//import net.sf.jasperreports.engine.JREmptyDataSource;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
///**
// *
// * @author Janidu
// */
//public class Log4jInitialization {
//
//    public static void initializeLog4j() {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    PropertyConfigurator.configure("src/resuorse/log4j.properties");
//
//                    String pathStream = "initial/test.jasper";
//
//                    String outputPath = "initial/test.pdf";
//
//                    HashMap parameters = new HashMap();
//
//                    JREmptyDataSource dataSource = new JREmptyDataSource();
//                    JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
//
//                    JasperExportManager.exportReportToPdfFile(jp, outputPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        t.start();
//
//    }
//}
