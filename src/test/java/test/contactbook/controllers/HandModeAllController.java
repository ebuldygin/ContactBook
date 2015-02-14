package test.contactbook.controllers;

import com.google.inject.Inject;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;

public class HandModeAllController extends AbstractAllControllers {

    private EntityManagerFactory emf;

    @Inject
    public HandModeAllController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void close() {
        emf.close();
        log.log(Level.FINE, "DB closed");
    }
    
    
}
