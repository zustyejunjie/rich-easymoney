package com.greater.eastmoney.controller;

import com.google.common.collect.Maps;
import com.greater.eastmoney.util.excel.MSExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 下载excel
 * @Author: yejj
 * @Date: 2021/3/16 18:33
 * @Description:
 **/

@Controller
@RequestMapping("/download")
public class DownloadController {


    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel( HttpServletResponse response) throws Exception{
        String outFileName = "test.xls";
        String sheetName = "testSheet";

        // 创建excel文件 并且初始化一个sheet
        HSSFWorkbook hssfWorkbook = MSExcelUtil.createExcel(sheetName);

        // 写入数据
        MSExcelUtil.fillData(hssfWorkbook, sheetName, datalist(), metaList(), 0);

        // 设置属性
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(outFileName, "UTF-8"));

        // 把excel写入到ServletOutputStream
        hssfWorkbook.write(response.getOutputStream());
    }

    /**
     * 多个excel 然后打包下载
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/zip", method = RequestMethod.GET)
    public void excelZip( HttpServletResponse response) throws Exception{
        ZipOutputStream zipOutputStream = null;

        try{
            // 写入zip
            String zipName = "压缩包名称.zip";
            // 设置属性
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + URLEncoder.encode(zipName, "UTF-8"));

            zipOutputStream = new ZipOutputStream(response.getOutputStream());

            /** 第一个excel */
            String outFileName1 = "test1.xls";
            String sheetName1 = "testSheet1";
            // 创建excel文件 并且初始化一个sheet
            HSSFWorkbook hssfWorkbook1 = MSExcelUtil.createExcel(sheetName1);
            // 写入数据
            MSExcelUtil.fillData(hssfWorkbook1, sheetName1, datalist(), metaList(), 0);

            /** 第二个excel */
            String outFileName2 = "test2.xls";
            String sheetName2 = "testSheet2";
            // 创建excel文件 并且初始化一个sheet
            HSSFWorkbook hssfWorkbook2 = MSExcelUtil.createExcel(sheetName2);
            // 写入数据
            MSExcelUtil.fillData(hssfWorkbook2, sheetName2, datalist(), metaList(), 0);

            // 把两个excel写入到zip --第一个
            zipOutputStream.putNextEntry(new ZipEntry(outFileName1));
            ByteArrayOutputStream tempOut1 = new ByteArrayOutputStream();
            hssfWorkbook1.write(tempOut1);
            ByteArrayInputStream tempIn1 = new ByteArrayInputStream(tempOut1.toByteArray());
            int len;
            while((len = tempIn1.read()) != -1) {
                zipOutputStream.write(len);
            }
            zipOutputStream.flush();

            tempIn1.close();
            tempOut1.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(zipOutputStream!=null){
                zipOutputStream.close();
            }

        }

        // 把两个excel写入到zip --第二个
//        zipOutputStream.putNextEntry(new ZipEntry(outFileName2));
//        ByteArrayOutputStream tempOut2 = new ByteArrayOutputStream();
//        hssfWorkbook2.write(tempOut2);
//        ByteArrayInputStream tempIn2 = new ByteArrayInputStream(tempOut2.toByteArray());
//        int len2;
//        while((len2 = tempIn2.read()) != -1) {
//            zipOutputStream.write(len2);
//            zipOutputStream.flush();
//        }

    }



    private List<Map<String, String>> datalist(){
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String,String> map = Maps.newHashMap();
        map.put("name","yejunjie");
        map.put("id","yjj");
        dataList.add(map);

        Map<String,String> map1 = Maps.newHashMap();
        map1.put("name","yejunjie1");
        map1.put("id","yjj1");
        dataList.add(map1);
        return dataList;
    }

    public List<String> metaList(){
        return Lists.newArrayList("name","id");
    }


}
