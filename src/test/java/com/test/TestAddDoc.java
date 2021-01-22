package com.test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Unit test for simple App.
 */
public class TestAddDoc
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(TestAddDoc.class);
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

                dataMap.put("d1", getSznl(x.get(3), x.get(4)));
                dataMap.put("d2", x.get(5));
                dataMap.put("d3", x.get(6));
                dataMap.put("d4", x.get(7));
                dataMap.put("d5", x.get(8));

                dataMap.put("s1", x.get(9).equals("NULL")? "  --":x.get(9));
                dataMap.put("s2", x.get(10).equals("NULL")? "  --":x.get(10));
                dataMap.put("a1", x.get(11).equals("NULL")? "  --":x.get(11));

                dataMap.put("d6", getSznl(x.get(12), x.get(13)));
                dataMap.put("d7", x.get(14));
                dataMap.put("d8", x.get(15));
                dataMap.put("d9", x.get(16));
                dataMap.put("d0", x.get(17));

                dataMap.put("s3", x.get(18).equals("NULL")? "  --":x.get(18));
                dataMap.put("s4", x.get(19).equals("NULL")? "  --":x.get(19));
                dataMap.put("a2", x.get(20).equals("NULL")? "  --":x.get(20));

                createDoc(dataMap,"demo",String.format("D:\\demo\\class\\%s\\%s.doc",fileName, dataMap.get("name")));
            });
        });
    }

    private String getSznl(String nian, String yue){
        String sznl;
        if(yue.equals("0")){
            sznl = nian+"周岁";
        }else {
            sznl = nian +"岁"+ yue +"月";
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
//                        case Cell.CELL_TYPE_NUMERIC:
//                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                                temp = DateUtil.parseToString(cell.getDateCellValue(),
//                                        DateUtil.FORMAT_DATE);
//                            } else {
//                                temp = new DecimalFormat("#.######").format(cell.getNumericCellValue());
//                            }
//                            break;
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
