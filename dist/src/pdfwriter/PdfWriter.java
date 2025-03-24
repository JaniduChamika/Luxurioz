/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdfwriter;

/**
 *
 * @author Janidu
 */
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import model.StaticComponent;

public class PdfWriter {

    private LeaseDataModel leaseParameter;
    private String leaseid;

    public PdfWriter(LeaseDataModel leaseParameter) {
        this.leaseParameter = leaseParameter;
    }

    public void generateDoc(String leaseId) {
        try {
            this.leaseid = leaseId;
            // Load the existing Word document
            FileInputStream fis = new FileInputStream("doc/lease-template/Apartment-Lease-Agreement-Template.docx");
            XWPFDocument document = new XWPFDocument(fis);
            fis.close();

            // Edit the document content
            editDocumentContent(document);

            // Save the modified document
            String outputPath = "doc/lease-doc/" + leaseId + ".docx";
            FileOutputStream fos = new FileOutputStream(outputPath);
            document.write(fos);
            fos.close();
            StaticComponent.openPDF(outputPath);
//            System.out.println("Word document edited successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editDocumentContent(XWPFDocument document) {
        // Iterate through paragraphs in the document
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            // Iterate through runs in each paragraph
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                // Edit text content or apply formatting as needed
                String text = run.getText(0);
                if (text != null) {
                    if (text.contains("leasid")) {
                        run.setText(text.replace("leasid", this.leaseid), 0);

                    }
                    if (text.contains("registday")) {
                        run.setText(text.replace("registday", leaseParameter.getRegday()), 0);

                    }
                    if (text.contains("regmonth")) {
                        run.setText(text.replace("regmonth", leaseParameter.getRegmonth()), 0);

                    }
                    if (text.contains("regyear")) {
                        run.setText(text.replace("regyear", leaseParameter.getRegyear()), 0);

                    }
                    if (text.contains("customername")) {
                        run.setText(text.replace("customername", leaseParameter.getCustomername()), 0);

                    }
                    if (text.contains("cusnic")) {
                        run.setText(text.replace("cusnic", leaseParameter.getCusnic()), 0);

                    }
                    if (text.contains("apartid")) {
                        run.setText(text.replace("apartid", leaseParameter.getApartid()), 0);

                    }
                    if (text.contains("startdate")) {
                        run.setText(text.replace("startdate", leaseParameter.getStartdate()), 0);

                    }
                    if (text.contains("enddate")) {
                        run.setText(text.replace("enddate", leaseParameter.getEnddate()), 0);

                    }
                    if (text.contains("totrent")) {
                        run.setText(text.replace("totrent", leaseParameter.getTotrent()), 0);

                    }
                    if (text.contains("secupayment")) {
                        run.setText(text.replace("secupayment", leaseParameter.getSecupayment()), 0);

                    }
                }
            }
        }
    }

}
