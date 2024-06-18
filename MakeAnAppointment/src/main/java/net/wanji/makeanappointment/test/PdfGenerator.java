package net.wanji.makeanappointment.test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfGenerator {

    public static void main(String[] args) {
        try {

            // 加载现有的PDF文件
            PdfReader reader = new PdfReader("C:\\Users\\wanji\\Desktop\\测试计划申请表-1204-1210.pdf");
            PdfWriter writer = new PdfWriter("C:\\Users\\wanji\\Desktop\\modified" + System.currentTimeMillis() + ".pdf");
            PdfDocument pdf = new PdfDocument(reader, writer);
            Document document = new Document(pdf);

            // 移动到文档的末尾
            document.add(new Paragraph("\n")); // 添加空白行

            // 添加文本
            document.add(new Paragraph("Line 1"));
            document.add(new Paragraph("Line 2"));
            document.add(new Paragraph("Line 3"));

            // 关闭文档
            document.close();

            System.out.println("Text added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
