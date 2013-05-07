package org.richfaces.ui.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.tabPanel.model.SimpleBean;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
public class ITStaticTabTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:tabPanel")
    private WebElement tabPanel;

    @FindBy(className = "rf-tab-hdr-inact")
    private List<WebElement> tabs;

    @FindBy(id = "out")
    private WebElement out;

    @FindBy(id = "myForm:inputText")
    private WebElement inputText;

    @FindBy(id = "myForm:outputText")
    private WebElement outputText;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITStaticTabTest.class);
        deployment.archive().addClass(SimpleBean.class);

        addIndexPage(deployment);

        WebArchive archive = deployment.getFinalArchive();
        return archive;
    }

    /**
     * RF-12839
     */
    @Test
    @Category(Smoke.class)
    public void check_tab_switch() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        guardXhr(tabs.get(1)).click();
        Assert.assertTrue(out.getText().contains("begin"));
//        Assert.assertTrue(out.getText().contains("tabpanel_complete"));
//        Assert.assertTrue(out.getText().contains("beforedomupdate"));

        // Assert the oncomplete on the tab does work
        Assert.assertTrue(out.getText().contains("tab1_complete"));

    }

    @Test
    public void check_tab_execute() {
        browser.get(contextPath.toExternalForm() + "index.jsf");

        inputText.sendKeys("abcd");
        guardXhr(tabs.get(1)).click();
        Assert.assertEquals("abcd", outputText.getText());
    }


    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.body("<h:form id='myForm'>");
        p.body("<r:tabPanel id='tabPanel' ");
        p.body("               onbegin='$(\"#out\").append(\"begin \\n\")'");
        p.body("               oncomplete='$(\"#out\").append(\"tabpanel_complete \\n\")'");
        p.body("               onbeforedomupdate='$(\"#out\").append(\"beforedomupdate \\n\")'>");
        p.body("    <r:tab id='tab0' name='tab0' header='tab0 header' ");
        p.body("               oncomplete='$(\"#out\").append(\"tab0_complete \\n\")'>");
        p.body("        content of tab 1");
        p.body("    </r:tab>");
        p.body("    <r:tab id='tab1' name='tab1' header='tab1 header' ");
        p.body("               execute='inputText'");
        p.body("               oncomplete='$(\"#out\").append(\"tab1_complete \\n\")'>");
        p.body("        content of tab 2");
        p.body("        <h:outputText id = 'outputText' value='#{simpleBean.string}' />");
        p.body("    </r:tab>");
        p.body("</r:tabPanel> ");
        p.body("<h:inputText id = 'inputText' value='#{simpleBean.string}' />");
        p.body("<div id='out'></div>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}
