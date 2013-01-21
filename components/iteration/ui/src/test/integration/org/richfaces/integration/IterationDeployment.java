package org.richfaces.integration;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.deployment.Deployment;

import java.io.File;

public class IterationDeployment extends Deployment {

    public IterationDeployment(Class<?> testClass) {
        super(testClass);
        
        this.addMavenDependency(
                "org.richfaces.core:richfaces-core-api",
                "org.richfaces.core:richfaces-core-impl",
                "org.richfaces.ui.common:richfaces-ui-common-api",
                "org.richfaces.ui.common:richfaces-ui-common-ui",
                "org.richfaces.ui.core:richfaces-ui-core-api",
                "org.richfaces.ui.core:richfaces-ui-core-ui",
                "org.richfaces.ui.iteration:richfaces-ui-iteration-api");

        JavaArchive iteration = ShrinkWrap.create(JavaArchive.class, "richfaces-ui-iteration-ui.jar");
        iteration.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("target/classes/").as(GenericArchive.class),
                "/", Filters.includeAll());
        archive().addAsLibrary(iteration);
        archive().addAsWebInfResource(new File("src/test/resources/beans.xml"));
        
    }
}
