package read.files.portlet;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import read.files.constants.ReadFilesPortletKeys;

/**
 * @author Matheus Alves
 */
@Component(
        immediate = true, service = PanelApp.class,
        property = {
                "panel.app.order:Integer=310",
                "panel.category.key=" + PanelCategoryKeys.APPLICATIONS_MENU_APPLICATIONS_CONTENT
        }
)
public class ReadFilesPanelApp extends BasePanelApp {

    @Override
    public String getPortletId() {
        return ReadFilesPortletKeys.READFILES;
    }

    @Override
    @Reference(
            target = "(javax.portlet.name=" + ReadFilesPortletKeys.READFILES + ")",
            unbind = "-"
    )
    public void setPortlet(Portlet portlet) {
        super.setPortlet(portlet);
    }
}