package com.greater.eastmoney.controller.download;

import com.google.common.collect.Maps;
import com.greater.eastmoney.util.excel.MSExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
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
@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController {


    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel( HttpServletResponse response) throws Exception{
        String outFileName = "test1.xls";
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
    public void excelZip( HttpServletRequest request,HttpServletResponse response) throws Exception{
        byte[] buf = new byte[2048];
        String downloadZipFileName = "测试zip名称"+ LocalDateTime.now().toString() +".zip";
        try{
            // 定义过滤流
            ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream());

            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                downloadZipFileName = new String(downloadZipFileName.getBytes("GB2312"),"ISO-8859-1");
            } else {
                downloadZipFileName = java.net.URLEncoder.encode(downloadZipFileName, "UTF-8");
                downloadZipFileName = new String(downloadZipFileName.getBytes("UTF-8"), "GBK");
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename="+downloadZipFileName);

            // 写入过滤流
            ByteArrayInputStream byteArrayInputStream = excelinputstream();
            String excelName = "123.xls";
            outputStream.putNextEntry(new ZipEntry(excelName));
            int len ;
            while ((len=byteArrayInputStream.read(buf))!=-1){
                outputStream.write(buf,0,len);
                outputStream.flush();
            }
            outputStream.closeEntry();

            byteArrayInputStream.close();


            // 写入第二个excel
            ByteArrayInputStream inputStream2 = excelinputstream2();
            String excelName2 = "345.xls";
            outputStream.putNextEntry(new ZipEntry(excelName2));
            int len2 ;
            while ((len2=inputStream2.read(buf))!=-1){
                outputStream.write(buf,0,len2);
                outputStream.flush();
            }
            outputStream.closeEntry();
            inputStream2.close();


            outputStream.close();
            log.info("导出成功");
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            log.info("处理结束");
        }
    }

    /**
     * 获取excel的输入流
     * @return
     * @throws Exception
     */
    private ByteArrayInputStream excelinputstream() throws Exception{
        String outFileName1 = "test1.xls";
        String sheetName1 = "testSheet1";
        // 创建excel文件 并且初始化一个sheet
        HSSFWorkbook hssfWorkbook1 = MSExcelUtil.createExcel(sheetName1);
        // 写入数据
        MSExcelUtil.fillData(hssfWorkbook1, sheetName1, datalist(), metaList(), 0);
        ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        hssfWorkbook1.write(tempOut);
        return new ByteArrayInputStream(tempOut.toByteArray());
    }


    /**
     * 获取excel的输入流
     * @return
     * @throws Exception
     */
    private ByteArrayInputStream excelinputstream2() throws Exception{
        String outFileName1 = "test2.xls";
        String sheetName1 = "testSheet2";
        // 创建excel文件 并且初始化一个sheet
        HSSFWorkbook hssfWorkbook1 = MSExcelUtil.createExcel(sheetName1);
        // 写入数据
        MSExcelUtil.fillData(hssfWorkbook1, sheetName1, datalist(), metaList(), 0);
        ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        hssfWorkbook1.write(tempOut);
        return new ByteArrayInputStream(tempOut.toByteArray());
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
