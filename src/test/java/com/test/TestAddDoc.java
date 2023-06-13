package com.test;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import help.utils.DateTimeUtils;

/**
 * Unit test for simple App.
 */
public class TestAddDoc
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(TestAddDoc.class);
    private static final String DEMO_2022 = "demo2023a";
    public Configuration configure=null;

    @Test
    public void AppTest(){
        readFileName("D:\\demo").forEach( fileName -> {
            LOGGER.info("FileName: " + fileName);
            createDir(fileName);
            String path = String.format("D:\\demo\\%s.xlsx", fileName);
            Workbook wb = getWb(path);
            List<List<String>> list = getExcelRows(getSheet(wb, 0), 1, -1);
            list.forEach(x->{
                Map<String, Object> dataMap=new HashMap<String, Object>();
                dataMap.put("className", x.get(0));
                dataMap.put("name", x.get(1));
                dataMap.put("dateTime", x.get(2));

                dataMap.put("d1", x.get(3).equals("NULL")? "--":getSznl(x.get(3), x.get(4)));
                dataMap.put("d2", getValue(x, 5));
                dataMap.put("d3", getValue(x, 6));
                dataMap.put("d4", getValue(x, 7));
                dataMap.put("d5", getValue(x, 8));

                dataMap.put("s1", getValue(x, 9));
                dataMap.put("s2", getValue(x, 10));
                dataMap.put("a1", getValue(x, 11));

                dataMap.put("d6", x.get(12).equals("NULL")? "--":getSznl(x.get(12), x.get(13)));
                dataMap.put("d7", getValue(x, 14));
                dataMap.put("d8", getValue(x, 15));
                dataMap.put("d9", getValue(x, 16));
                dataMap.put("d0", getValue(x, 17));

                dataMap.put("s3", getValue(x, 18));
                dataMap.put("s4", getValue(x, 19));
                dataMap.put("a2", getValue(x, 20));

                createDoc(dataMap, DEMO_2022,String.format("D:\\demo\\class\\%s\\%s.doc",fileName, dataMap.get("name")));
            });
        });
    }

    private String getValue(List<String> list, int index){
        return list.get(index).equals("NULL")? "--":list.get(index).replace(".0", "");
    }

    private String getSznl(String nian, String yue){
        String sznl;
        if(yue.equals("0")){
            sznl = nian.replace(".0", "")+"周岁";
        }else {
            sznl = nian.replace(".0", "") +"岁"+ yue.replace(".0", "") +"月";
        }
        return sznl;
    }

    private void createDir(String fileName){
        File file = new File(String.format("D:\\demo\\class\\%s", fileName));
        if(!file.exists()){
            file.mkdirs();
        }

    }

    private List<String> readFileName(String path){
        File dir = new File(path);
        File[] files = dir.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                   if (f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx")){
                       fileNames.add(f.getName().replace(".xlsx",""));
                   }
                } else if (f.isDirectory()) {
                    System.out.println(f.getAbsolutePath());
                }
            }
        }
        return fileNames;
    }

    public void createDoc(Map<String,Object> dataMap, String downloadType, String savePath){
        try {
            //加载需要装填的模板
            Template template=null;
            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
            //加载模板文件，放在testDoc下
            configure.setClassForTemplateLoading(this.getClass(), "/testdata");
            //设置对象包装器
//            configure.setObjectWrapper(new DefaultObjectWrapper());
            //设置异常处理器
            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            //定义Template对象，注意模板类型名字与downloadType要一致
            template=configure.getTemplate(downloadType+".xml");
            File outFile=new File(savePath);
            Writer out=null;
            out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            template.process(dataMap, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public TestAddDoc(){
        configure=new Configuration(Configuration.VERSION_2_3_22);
        configure.setDefaultEncoding("utf-8");
    }

    public void createDocAll(Map<String,Object> dataMap, String downloadType, String savePath){
        try {
            //加载需要装填的模板
            Template template=null;
            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
            //加载模板文件，放在testDoc下
            configure.setClassForTemplateLoading(this.getClass(), "/testdata");
            //设置对象包装器
//            configure.setObjectWrapper(new DefaultObjectWrapper());
            //设置异常处理器
            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            //定义Template对象，注意模板类型名字与downloadType要一致
            template=configure.getTemplate(downloadType+".xml");
            File outFile=new File(savePath);
            Writer out=null;
            out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            template.process(dataMap, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public static Workbook getWb(String path) {
        try {
            return WorkbookFactory.create(new File(path));
        } catch (Exception e) {
            throw new RuntimeException("读取EXCEL文件出错", e);
        }
    }

    public static Sheet getSheet(Workbook wb, int sheetIndex) {
        if (wb == null) {
            throw new RuntimeException("工作簿对象为空");
        }
        int sheetSize = wb.getNumberOfSheets();
        if (sheetIndex < 0 || sheetIndex > sheetSize - 1) {
            throw new RuntimeException("工作表获取错误");
        }
        return wb.getSheetAt(sheetIndex);
    }

    public static List<List<String>> getExcelRows(Sheet sheet, int startLine, int endLine) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 如果开始行号和结束行号都是-1的话，则全表读取
        if (startLine == -1)
            startLine = 0;
        if (endLine == -1) {
            endLine = sheet.getLastRowNum() + 1;
        } else {
            endLine += 1;
        }
        for (int i = startLine; i < endLine; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                System.out.println("该行为空，直接跳过");
                continue;
            }
            int rowSize = row.getLastCellNum();
            List<String> rowList = new ArrayList<String>();
            for (int j = 0; j < rowSize; j++) {
                Cell cell = row.getCell(j);
                String temp = "";
                if (cell == null) {
                    System.out.println("该列为空，赋值双引号");
                    temp = "0";
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            temp = cell.getStringCellValue().trim();
                            temp = StringUtils.isEmpty(temp) ? "NULL" : temp;
                            break;
                        case BOOLEAN:
                            temp = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            temp = String.valueOf(cell.getCellFormula().trim());
                            break;
                        case NUMERIC:
                            short format = cell.getCellStyle().getDataFormat();
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = null;
                                //System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
                                if (format == 20 || format == 32) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    double value = cell.getNumericCellValue();
                                    Date date = org.apache.poi.ss.usermodel.DateUtil
                                            .getJavaDate(value);
                                    temp = sdf.format(date);
                                }else {// 日期
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                }
                                try {
                                    temp = sdf.format(cell.getDateCellValue());// 日期
                                } catch (Exception e) {
                                    try {
                                        throw new Exception("exception on get date data !".concat(e.toString()));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }finally{
                                    sdf = null;
                                }
                            }  else {
                                temp = String.valueOf(cell.getNumericCellValue());// 数值 这种用BigDecimal包装再获取plainString，可以防止获取到科学计数值
                            }
                            break;
                        case BLANK:
                            temp = "NULL";
                            break;
                        case ERROR:
                            temp = "ERROR";
                            break;
                        default:
                            temp = cell.toString().trim();
                            break;
                    }
                }
                rowList.add(temp);
            }
            list.add(rowList);
        }
        return list;
    }
}
