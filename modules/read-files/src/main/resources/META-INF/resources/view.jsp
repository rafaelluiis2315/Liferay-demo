<%@ include file="./init.jsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:actionURL var="harnessTestURL" name="<%=MVCCommandNames.READ_FILES_UPLOADE_URL %>">
    <portlet:param name="mvcPath" value="/result.jsp"/>
</portlet:actionURL>

<div class="container-fluid container-fluid-max-xl">

 	 <aui:form action="${harnessTestURL}" enctype="multipart/form-data">
            <aui:col width="75">
                <aui:fieldset>
                        <aui:input name="<%=MVCCommandNames.DATA_FILE %>" id="<%=MVCCommandNames.DATA_FILE %>" label="Arquivo para ler"
                                   type="file">
                            <aui:validator name="required"/>
                            <aui:validator errorMessage="Precisa ser um arquivo excel" name="custom">
                                function (val) {
                                  var extArquivo = val.split('.').pop().toLowerCase();

                                  if(extArquivo ==='xlsx' || extArquivo ==='xls') {
                                    return true;
                                  } else {
                                    return false;
                                  }
                                }
                            </aui:validator>
                        </aui:input>

                </aui:fieldset>
            </aui:col>

            <aui:row>
                <aui:col width="25">
                    <aui:fieldset>
                        <aui:button id="submitButton" type="submit" value="Enviar"/>
                    </aui:fieldset>
                </aui:col>
            </aui:row>
     </aui:form>

   </script>
</div>
