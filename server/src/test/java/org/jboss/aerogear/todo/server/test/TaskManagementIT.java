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
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TaskManagementIT {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Project.class.getPackage())
                .addPackage(ProjectEndpoint.class.getPackage())
                .addClasses(Resources.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("arquillian-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testCreate() throws Exception {
        //TODO: to be implemented
    }

    @Test
    public void testDeleteById() throws Exception {
        //TODO: to be implemented
    }

    @Test
    public void testFindById() throws Exception {
        //TODO: to be implemented
    }

    @Test
    public void testListAll() throws Exception {
        //TODO: to be implemented
    }

    @Test
    public void testUpdate() throws Exception {
        //TODO: to be implemented
    }
}
