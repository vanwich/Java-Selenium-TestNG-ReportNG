package com.test;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelUnmarshaller;
import help.utils.excel.io.entity.area.table.TableRow;
import rest.service.persona.PersonaInfoModel;

public class TestExcel {
    public final static Logger LOGGER = LoggerFactory.getLogger(TestExcel.class);
    private final static String ROOT_PATH = "src/test/resources";
    private final static String TESTDATA_FOLDER = FilenameUtils.separatorsToSystem(ROOT_PATH + "/testdata/");
    private final static String TEST_FILE = TESTDATA_FOLDER + "testExcel.xlsx";


    @Test
    public void testExcel(){
        ExcelFile excelFile = new ExcelFile(new File(TEST_FILE));

        String  name = excelFile.getSheet("Demo1").getTable(1, false).getRows().get(0).getCell("姓名").getStringValue();
        LOGGER.info("姓名 ： " + name);

        TableRow tableRow = excelFile.getSheet("Demo1").getTable(1, false).getRow(1);
        LOGGER.info("信息： " + tableRow.getValues());

        ExcelUnmarshaller excel = new ExcelUnmarshaller(new File(TEST_FILE));
        List<PersonaInfoModel>  personaInfoModels =  excel.unmarshalRows(PersonaInfoModel.class);
        personaInfoModels.forEach(personaInfoModel -> {
            LOGGER.info(personaInfoModel.toString());
        });

    }
}
