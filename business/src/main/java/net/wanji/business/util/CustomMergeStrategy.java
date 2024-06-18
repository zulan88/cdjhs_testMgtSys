package net.wanji.business.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import net.wanji.business.annotion.CustomMerge;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/6 18:43
 * @Descriptoin:
 */

public class CustomMergeStrategy implements RowWriteHandler {

    private int titleIndex;
    /**
     * 主键下标集合
     */
    private List<Integer> pkColumnIndex = new ArrayList<>();

    /**
     * 需要合并的列的下标集合
     */
    private List<Integer> needMergeColumnIndex = new ArrayList<>();

    /**
     * DTO数据类型
     */
    private Class<?> elementType;

    public CustomMergeStrategy(Class<?> elementType, int titleIndex) {
        this.elementType = elementType;
        this.titleIndex = titleIndex;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        // 如果是标题,则直接返回
        if (isHead) {
            return;
        }

        // 获取当前sheet
        Sheet sheet = writeSheetHolder.getSheet();
        if (pkColumnIndex.isEmpty()) {
            this.lazyInit(writeSheetHolder);
        }
        // 判断是否需要和上一行进行合并 不能和标题合并，只能数据行之间合并
        Row lastRow = sheet.getRow(row.getRowNum() - 1);
        if (ObjectUtils.isEmpty(lastRow)) {
            return;
        }
        // 将本行和上一行是同一类型的数据(通过主键字段进行判断)，则需要合并
        for (int i = 0; i < pkColumnIndex.size(); i++) {
            Integer pkIndex = pkColumnIndex.get(i);
            Integer nextPkIndex = i == pkColumnIndex.size() - 1 ? Integer.MAX_VALUE : pkColumnIndex.get(i + 1);
            String lastKey = lastRow.getCell(pkIndex).getCellType() == CellType.STRING ? lastRow.getCell(pkIndex).getStringCellValue() : String.valueOf(lastRow.getCell(pkIndex).getNumericCellValue());
            String currentKey = row.getCell(pkIndex).getCellType() == CellType.STRING ? row.getCell(pkIndex).getStringCellValue() : String.valueOf(row.getCell(pkIndex).getNumericCellValue());
            if (!StringUtils.equalsIgnoreCase(lastKey, currentKey)) {
                break;
            }
            for (int j = 0; j < needMergeColumnIndex.size(); j++) {
                Integer needMerIndex = needMergeColumnIndex.get(j);
                if (needMerIndex < pkIndex || needMerIndex >= nextPkIndex) {
                    continue;
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
                        needMerIndex, needMerIndex);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }

    /**
     * 初始化主键下标和需要合并字段的下标
     */
    private void lazyInit(WriteSheetHolder writeSheetHolder) {

        // 获取当前sheet
        Sheet sheet = writeSheetHolder.getSheet();
        // 获取DTO的类型
        Class<?> eleType = this.elementType;

        // 获取DTO所有的属性
        Field[] fields = eleType.getDeclaredFields();

        // 遍历所有的字段，因为是基于DTO的字段来构建excel，所以字段数 >= excel的列数
        for (int i = 0; i < fields.length; i++) {
            Field theField = fields[i];
            // 获取@ExcelProperty注解，用于获取该字段对应在excel中的列的下标
            ExcelProperty easyExcelAnno = theField.getAnnotation(ExcelProperty.class);
            // 为空,则表示该字段不需要导入到excel,直接处理下一个字段
            if (null == easyExcelAnno) {
                continue;
            }
            // 获取自定义的注解，用于合并单元格
            CustomMerge customMerge = theField.getAnnotation(CustomMerge.class);

            // 没有@CustomMerge注解的默认不合并
            if (null == customMerge) {
                continue;
            }

            // 判断是否有主键标识
            if (customMerge.isPk()) {
                pkColumnIndex.add(i);
            }

            // 判断是否需要合并
            if (customMerge.needMerge()) {
                needMergeColumnIndex.add(i);
            }
        }
        // 没有指定主键，则异常
        if (pkColumnIndex.isEmpty()) {
            throw new IllegalStateException("使用@CustomMerge注解必须指定主键");
        }
    }
}
