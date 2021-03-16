package com.greater.eastmoney.util.excel;

import com.greater.eastmoney.common.ResultCodeEnum;
import com.greater.eastmoney.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel工具类
 */
@SuppressWarnings("deprecation")
@Slf4j
public class MSExcelUtil {

    /**
     * 构建多sheet的excel文件
     * @param sheetNames 要初始化构建的sheet名称
     * @return
     */
    public static HSSFWorkbook createExcel(List<String> sheetNames) {
        try {
            HSSFWorkbook hssfworkbook = new HSSFWorkbook();
            if (sheetNames != null && sheetNames.size() > 0) {
                for (String sheetName : sheetNames) {
                    hssfworkbook.createSheet(sheetName);
                }
            }
            return hssfworkbook;
        } catch (Exception exception) {
            log.error("构建excel文件失败", exception);
            return null;
        }
    }

    /**
     * 构建单一sheet的excel文件
     * @param sheetName 要初始化构建的sheet名称
     * @return
     */
    public static HSSFWorkbook createExcel(String sheetName) {
        try {
            HSSFWorkbook hssfworkbook = new HSSFWorkbook();
            if (StringUtils.isNotBlank(sheetName)) {
                hssfworkbook.createSheet(sheetName);
            }
            return hssfworkbook;
        } catch (Exception exception) {
            log.error("数据写入excel失败，文件日期", exception);
            return null;
        }
    }

    /**
     * 构建文件头
     * @param hssfworkbook
     * @param sheetName sheet名称,如不存在，新建sheet
     * @param rows 合并行数
     * @param cols 合并列数
     * @return
     */
    public static void createTitle(HSSFWorkbook hssfworkbook, String sheetName, int rows, int cols,
                                   String headText) {
        HSSFSheet sheet = hssfworkbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = hssfworkbook.createSheet(sheetName);
        }
        HSSFCellStyle titleStyle = createCellStyle(hssfworkbook, "黑体", HSSFFont.BOLDWEIGHT_BOLD,
            (short) 12, new HSSFColor.GREY_50_PERCENT().getIndex());
        for (int i = 0; i < rows; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < cols; j++) {
                row.setHeight((short) 350);
                HSSFCell cell = row.createCell(j);//左侧空出一格
                cell.setCellStyle(titleStyle);
                cell.setCellValue(new HSSFRichTextString(headText));
            }
        }
        //合并单元格
        CellRangeAddress range = new CellRangeAddress(1, rows, 1, cols);
        sheet.addMergedRegion(range);
    }

    private static HSSFCellStyle createCellStyle(HSSFWorkbook hssfworkbook, String fontType,
                                                 short fontWeight, short fontSize,
                                                 short backgroundColor) {
        HSSFFont headfont = hssfworkbook.createFont();
        headfont.setFontName(fontType);
        headfont.setFontHeightInPoints(fontSize);
        headfont.setBoldweight(fontWeight);
        HSSFCellStyle headstyle = hssfworkbook.createCellStyle();
        headstyle.setFont(headfont);
        // 左右居中
        headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 上下居中
        headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headstyle.setLocked(true);
        // 自动换行
        headstyle.setWrapText(true);
        //边框
        headstyle.setBorderBottom((short) 1);
        headstyle.setBorderLeft((short) 1);
        headstyle.setBorderRight((short) 1);
        headstyle.setBorderTop((short) 1);
        headstyle.setFillForegroundColor(backgroundColor);
        headstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        return headstyle;
    }

    /**
     *
     * @param hssfworkbook
     * @param sheetName
     * @param list
     * @param metaData
     * @param headRows
     */
    public static void fillData(HSSFWorkbook hssfworkbook, String sheetName,
                                List<Map<String, String>> list, List<String> metaData, int headRows) {
        try {
            HSSFSheet sheet = hssfworkbook.getSheet(sheetName);
            HSSFDataFormat format = hssfworkbook.createDataFormat();
            if (sheet == null) {
                throw new BizException(ResultCodeEnum.PARAM_ILLEGAL, "无效的sheet名称");
            }
            if (list == null || list.size() == 0) {
                // 设置列宽
//                for (int col = 0; col < metaData.size(); col++) {
//                    sheet.setColumnWidth(col + 1, 5000);
//                }
                return;
            }
            // 设置头部风格
            HSSFCellStyle headStyle = createCellStyle(hssfworkbook, "宋体",
                    HSSFFont.COLOR_NORMAL, (short) 11, IndexedColors.GREEN.getIndex());
            HSSFPalette palette = hssfworkbook.getCustomPalette();
            Integer r = Integer.parseInt("18",16);
            Integer g = Integer.parseInt("84",16);
            Integer b = Integer.parseInt("FC",16);
            HSSFColor hssfColor = palette.findSimilarColor(r,g,b);
            headStyle.setFillForegroundColor(hssfColor.getIndex());
            HSSFFont hssfFont = hssfworkbook.createFont();
            hssfFont.setFontName("宋体");
            hssfFont.setFontHeightInPoints((short)11);
            hssfFont.setBoldweight(HSSFFont.COLOR_NORMAL);
            hssfFont.setColor(IndexedColors.WHITE.getIndex());
            headStyle.setFont(hssfFont);

            HSSFCellStyle dataStyle = createCellStyle(hssfworkbook, "宋体",
                HSSFFont.BOLDWEIGHT_NORMAL, (short) 11, new HSSFColor.WHITE().getIndex());
            dataStyle.setDataFormat(format.getFormat("@"));
            for (int rowNum = 0; rowNum < list.size(); rowNum++) {
                HSSFRow row = sheet.createRow(rowNum + headRows);//顶部空出0行
                // 设置行高
                row.setHeight((short) 350);
                // 创建第一列
                Map<String, String> rowMap = list.get(rowNum);
                for (int cellNum = 0; cellNum < metaData.size(); cellNum++) {
                    HSSFCell cell = row.createCell(cellNum);//左侧空出0格
                    if (rowNum == 0) {//第一行作为head
                        cell.setCellStyle(headStyle);
                    } else {
                        cell.setCellStyle(dataStyle);
                    }
                    cell.setCellValue(rowMap.get(metaData.get(cellNum)));
                }
            }
            setSizeColumn(hssfworkbook.getSheet(sheetName),metaData.size());
        } catch (Exception exception) {
            log.error("数据写入excel失败，文件日期", exception);
            throw new BizException(ResultCodeEnum.PARAM_ILLEGAL, "sheet不存在");
        }
    }

    private static void setSizeColumn(HSSFSheet sheet,int size) {
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0,last = sheet.getLastRowNum(); rowNum <= last; rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        String value = currentCell.getStringCellValue();
                        boolean res = containChinese(value);
                        int length = value.getBytes().length*(res ? 1 : 2);
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
    private static boolean containChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
