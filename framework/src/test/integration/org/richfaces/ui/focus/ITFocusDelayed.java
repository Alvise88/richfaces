/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.ui.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITFocusDelayed {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:submit")
    private WebElement submit;

    @FindBy(id = "form:ajax")
    private WebElement ajax;

    @FindBy(id = "form:input1")
    private WebElement input1;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFocusDelayed.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_initial_request() {
        browser.get(contextPath.toExternalForm());
        assertEquals(null, getFocusedElement());
    }

    @Test
    public void test_delayed_focus_can_be_applied_using_client_side_api() {
        browser.get(contextPath.toExternalForm());

        ((JavascriptExecutor) browser).executeScript("RichFaces.$('form:focus').applyFocus(); ");
        waitGui().until(new ElementIsFocused(input1));
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_form_submit_postback() {
        browser.get(contextPath.toExternalForm());

        guardHttp(submit).click();
        assertEquals(null, getFocusedElement());
    }

    @Test
    public void when_focus_is_delayed_then_it_is_not_applied_on_ajax_postback() {
        browser.get(contextPath.toExternalForm());

        guardAjax(ajax).click();
        assertEquals(null, getFocusedElement());
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<h:form id='form'>");
        p.body("    <r:focus id='focus' delayed='true' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
