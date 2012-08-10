package org.jboss.aerogear.todo.server.test;

import org.aerogear.todo.server.model.Project;
import org.aerogear.todo.server.rest.JaxRsActivator;
import org.aerogear.todo.server.rest.ProjectEndpoint;
import org.aerogear.todo.server.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Uses Arquilian to test the JAX-RS processing class for member registration.
 *
 * @author balunasj
 */
@RunWith(Arquillian.class)
public class ProjectManagementIT {

    @Inject
    private ProjectEndpoint projectEndpoint;

    private Project project;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Project.class.getPackage())
                .addClasses(ProjectEndpoint.class, Resources.class, JaxRsActivator.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("arquillian-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
        project = new Project("AeroGear", "fancy", null);
        project = projectEndpoint.create(project);
    }

    @Test
    public void testUpdate() throws Exception {
        project = projectEndpoint.findById(1L);
        project.setTitle("NewAeroGear");

        project = projectEndpoint.update(1L, project);
        assertEquals("Project should be updated", "NewAeroGear", project.getTitle());
    }

    @Test
    public void testDeleteById() throws Exception {

        try {
            projectEndpoint.deleteById(1L);
            project = projectEndpoint.findById(1L);
            project.getTitle();
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(true);
        }

    }
}
