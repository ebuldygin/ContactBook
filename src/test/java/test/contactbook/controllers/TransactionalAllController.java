package test.contactbook.controllers;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import java.util.logging.Level;

public class TransactionalAllController extends AbstractAllControllers {

    private PersistService service;

    @Inject
    public TransactionalAllController(PersistService service) {
        this.service = service;
        service.start();
    }

    @Override
    public void close() {
        service.stop();
        log.log(Level.FINE, "DB closed");
    }
}
