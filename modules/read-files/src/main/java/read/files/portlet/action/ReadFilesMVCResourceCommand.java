package read.files.portlet.action;


import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;
import read.files.constants.MVCCommandNames;
import read.files.constants.ReadFilesPortletKeys;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;



@Component(
        property = {
                "javax.portlet.name=" + ReadFilesPortletKeys.READFILES,
                "mvc.command.name=" + MVCCommandNames.READ_FILES_UPLOADE_URL
        }, immediate = true, service = MVCActionCommand.class
)
public class ReadFilesMVCResourceCommand extends BaseMVCActionCommand {
    private static final Log LOG = LogFactoryUtil.getLog(ReadFilesMVCResourceCommand.class);

    @Override
    public void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse)  {
        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
        String fileName = uploadPortletRequest.getFileName(MVCCommandNames.DATA_FILE);

        LOG.info(fileName);

        File testMapFile = new File("/tmp/" + fileName);
        LOG.info("Test Map File ===> " + testMapFile);


        InputStream inputStream = null;
        try {
            inputStream = uploadPortletRequest.getFileAsStream(MVCCommandNames.DATA_FILE);

            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheetSummary = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetSummary.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getCellType() == CellType.valueOf("STRING")) LOG.info(cell.getStringCellValue());


                    if (cell.getCellType() == CellType.FORMULA) {
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        CellValue cellValue = evaluator.evaluate(cell);
                        switch (cellValue.getCellType()) {
                            case BOOLEAN:
                                LOG.info(cellValue.getBooleanValue());
                                break;
                            case NUMERIC:
                                LOG.info(cellValue.getNumberValue());
                                break;
                            case STRING:
                                LOG.info(cellValue.getStringValue());
                                break;
                            default:
                                LOG.info("Unsupported cell type");
                        }
                    }


                }
            }


        } catch (Exception e) {
            LOG.error("Error while processing action: " + e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
