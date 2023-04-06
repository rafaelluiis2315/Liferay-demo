package read.files.portlet.action;


import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.service.component.annotations.Component;
import read.files.constants.MVCCommandNames;
import read.files.constants.ReadFilesPortletKeys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;


@Component(
        property = {
                "javax.portlet.name=" + ReadFilesPortletKeys.READFILES,
                "mvc.command.name=" + MVCCommandNames.READ_FILES_UPLOADE_URL
        }, service = MVCResourceCommand.class
)
public class ReadFilesMVCResourceCommand implements MVCResourceCommand {
    private static final Log LOG = LogFactoryUtil.getLog(ReadFilesMVCResourceCommand.class);


    public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)  {
        InputStream file = null;
        try {
            file = resourceRequest.getPortletInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = resourceRequest.getProperty(MVCCommandNames.DATA_FILE);

        LOG.info(fileName);

        File testMapFile = new File("/tmp/" + fileName);

        try {


            Workbook workbook;

            if (testMapFile.getName().contains(".xls")) {
                 workbook = new XSSFWorkbook(file);
            }else {
                 workbook = new HSSFWorkbook(file);
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Sheet sheetSummary = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetSummary.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case BOOLEAN:
                            LOG.info(cell.getBooleanCellValue());
                            break;
                        case NUMERIC:
                            LOG.info(cell.getNumericCellValue());
                            break;
                        case STRING:
                            LOG.info(cell.getStringCellValue());
                            break;
                        case FORMULA:
                            CellValue cellValue = evaluator.evaluate(cell);

                            switch (cellValue.getCellType()) {
                                case BOOLEAN:
                                    System.out.println(cellValue.getBooleanValue());
                                    break;
                                case NUMERIC:
                                    System.out.println(cellValue.getNumberValue());
                                    break;
                                case STRING:
                                    System.out.println(cellValue.getStringValue());
                                    break;
                                default:
                                    LOG.info("Unsupported cell type");
                            }
                        default:
                            LOG.info("Unsupported cell type");
                    }



                }
            }

            return false;
        } catch (Exception e) {
            LOG.error("Error while processing action: " + e.getMessage(), e);
            return true;
        }
    }


}
