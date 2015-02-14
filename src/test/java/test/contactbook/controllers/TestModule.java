package test.contactbook.controllers;

import com.google.inject.AbstractModule;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import controllers.GroupController;
import controllers.PersonController;
import controllers.transactional.GroupTransactionalController;
import controllers.transactional.PersonTransactionalController;
import controllers.handmode.GroupHandModeController;
import controllers.handmode.PersonHandModeController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestModule {

    private static final String persistenceUnitName = "test";

    public static AbstractAllControllers testControllers() {
        //return getHandModeController();
        return getTransactionalController();      
    }

    private static AbstractAllControllers getHandModeController() {
        Module handModeModule = new AbstractModule() {

            @Override
            protected void configure() {
                EntityManagerFactory factory =
                        Persistence.createEntityManagerFactory(persistenceUnitName);
                bind(EntityManagerFactory.class).toInstance(factory);
                bind(GroupController.class).to(GroupHandModeController.class);
                bind(PersonController.class).to(PersonHandModeController.class);
            }
        };
        return Guice.createInjector(handModeModule)
                .getInstance(HandModeAllController.class);
    }

    private static AbstractAllControllers getTransactionalController() {
        Module transactionalModule = new AbstractModule() {

            @Override
            protected void configure() {
                bind(GroupController.class).to(GroupTransactionalController.class);
                bind(PersonController.class).to(PersonTransactionalController.class);
            }
        };
        return Guice.createInjector(transactionalModule, new JpaPersistModule(persistenceUnitName))
                .getInstance(TransactionalAllController.class);
    }   
}
