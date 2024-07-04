package net.wanji.business.pdf;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputResult;
import net.wanji.business.exercise.dto.evaluation.IndexDetail;
import net.wanji.business.exercise.dto.evaluation.SceneDetail;
import net.wanji.business.exercise.dto.evaluation.TrendChange;
import net.wanji.business.pdf.enums.IndexTypeEnum;
import net.wanji.business.pdf.enums.PdfContentEnum;
import net.wanji.business.pdf.enums.PdfTitleEnum;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: jenny
 * @create: 2024-06-28 9:02 上午
 */
@Service
public class PdfService {
    @Autowired
    private ChartService chartService;

    private static final String formatTemplate = "{}: {}";

    public ByteArrayOutputStream generatePdf(CdjhsExerciseRecord record) throws IOException, NoSuchFieldException, IllegalAccessException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        //字体设置
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        //添加段落并设置字体样式
        PdfTitleEnum[] values = PdfTitleEnum.values();
        List<PdfTitleEnum> pdfTitles = Stream.of(values)
                .sorted(Comparator.comparingInt(PdfTitleEnum::getOrder))
                .collect(Collectors.toList());
        for(PdfTitleEnum pdfTitle: pdfTitles){
            Paragraph title = new Paragraph(pdfTitle.getTitle())
                    .setFont(font)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(title);
        }
        document.add(new Paragraph("\n"));
        Paragraph baseInfo = new Paragraph("一、测试基本信息")
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.LEFT)
                .setBold();
        document.add(baseInfo);
        PdfContentEnum[] contentEnums = PdfContentEnum.values();
        List<PdfContentEnum> baseContents = Stream.of(contentEnums)
                .sorted(Comparator.comparingInt(PdfContentEnum::getOrder))
                .collect(Collectors.toList());
        for(PdfContentEnum pdfContent: baseContents){
            String fieldName = pdfContent.getFieldName();
            String name = pdfContent.getName();
            Field field = record.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(record);
            String result = "";
            if(value instanceof String){
                result = value.toString();
            }else{
                Date date = (Date) value;
                if(Objects.nonNull(date)){
                    result = DateUtils.getDateString(date);
                }
            }
            String content = StringUtils.format(formatTemplate, name, result);
            Paragraph paragraph = new Paragraph(content)
                    .setFont(font)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(paragraph);
        }
        Paragraph sceneInfo = new Paragraph("二、试验场路段场景得分")
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.LEFT)
                .setBold();
        document.add(sceneInfo);

        //该练习关联用例下所有场景评分
        String evaluationOutput = record.getEvaluationOutput();
        EvaluationOutputResult evaluation = JSONObject.parseObject(evaluationOutput, EvaluationOutputResult.class);
        List<SceneDetail> sceneDetails = evaluation.getDetails();
        sceneDetails = sceneDetails.stream()
                .sorted(Comparator.comparingInt(SceneDetail::getSequence))
                .collect(Collectors.toList());
        float[] columnWidths = {4, 4, 4};
        for(SceneDetail sceneDetail: sceneDetails){
            //场景
            Integer sequence = sceneDetail.getSequence();
            String sceneName = sceneDetail.getSceneCategory();
            String scene = StringUtils.format(formatTemplate, "场景" + sequence, sceneName);
            document.add(new Paragraph(scene).setFont(font).setFontSize(16).setTextAlignment(TextAlignment.LEFT));
            //场景不同评价维度得分
            for(int i = 0; i < 3; i++){
                String evaluationDimension;
                List<IndexDetail> indexDetails;
                if(i == 0){
                    evaluationDimension = "效率指标得分:";
                    indexDetails = sceneDetail.getEfficencyIndexDetails();
                }else if(i == 1){
                    evaluationDimension = "安全指标得分:";
                    indexDetails = sceneDetail.getSecurityIndexDetails();
                }else{
                    evaluationDimension = "舒适指标得分";
                    indexDetails = sceneDetail.getComfortDetails().getComfortIndexDetails();
                }
                document.add(new Paragraph(evaluationDimension).setFont(font).setFontSize(16).setTextAlignment(TextAlignment.LEFT));
                Table table = new Table(UnitValue.createPointArray(columnWidths));
                table.addHeaderCell(new Cell().add(new Paragraph("指标").setFont(font).setFontSize(14)));
                table.addHeaderCell(new Cell().add(new Paragraph("扣分").setFont(font).setFontSize(14)));
                table.addHeaderCell(new Cell().add(new Paragraph("时长").setFont(font).setFontSize(14)));
                for(IndexDetail indexDetail: indexDetails){
                    String index = IndexTypeEnum.getIndexNameByType(indexDetail.getIndex());
                    table.addCell(new Cell().add(new Paragraph(index).setFont(font).setFontSize(14)));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(indexDetail.getDeductPoints())).setFont(font).setFontSize(14)));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(indexDetail.getDuration())).setFont(font).setFontSize(14)));
                }
                document.add(table);
            }
            //折线图
            List<IndexDetail> comfortIndexDetails = sceneDetail.getComfortDetails().getComfortIndexDetails();
            for(IndexDetail indexDetail: comfortIndexDetails){
                if(StringUtils.isNotEmpty(indexDetail.getTrendOfChange())){
                    String index = IndexTypeEnum.getIndexNameByType(indexDetail.getIndex());
                    List<TrendChange> trendChanges = indexDetail.getTrendOfChange();
                    ByteArrayOutputStream chartStream = chartService.generateChart(index, trendChanges);
                    ImageData imageData = ImageDataFactory.create(chartStream.toByteArray());
                    Image image = new Image(imageData);
                    document.add(image);
                }
            }
        }
        document.close();
        return outputStream;
    }
}
