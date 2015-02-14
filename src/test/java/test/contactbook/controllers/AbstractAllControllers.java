package test.contactbook.controllers;

import com.google.inject.Inject;
import controllers.GroupController;
import controllers.PersonController;
import java.util.logging.Logger;

public abstract class AbstractAllControllers {

    protected static final Logger log = Logger
            .getLogger(AbstractAllControllers.class.getName());
    @Inject
    private PersonController personCtrl;
    @Inject
    private GroupController groupCtrl;    

    public GroupController getGroupCtrl() {
        return groupCtrl;
    }

    public void setGroupCtrl(GroupController groupCtrl) {
        this.groupCtrl = groupCtrl;
    }

    public PersonController getPersonCtrl() {
        return personCtrl;
    }

    public void setPersonCtrl(PersonController personCtrl) {
        this.personCtrl = personCtrl;
    }
    
    public abstract void close();
}
