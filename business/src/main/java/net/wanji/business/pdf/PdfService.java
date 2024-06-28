package net.wanji.business.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import net.wanji.business.exercise.dto.evaluation.TrendChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-28 9:02 上午
 */
@Service
public class PdfService {
    @Autowired
    private ChartService chartService;

    public ByteArrayOutputStream generatePdf(List<TrendChange> trendChanges) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        //字体设置
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        //添加段落并设置字体样式
        Paragraph title = new Paragraph("长大季后赛")
                .setFont(font)
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));
        //生成图表
        ByteArrayOutputStream chartStream = chartService.generateChart("横向加速度", trendChanges);
        ImageData imageData = ImageDataFactory.create(chartStream.toByteArray());
        Image image = new Image(imageData);
        document.add(image);

        document.close();
        return outputStream;
    }
}
