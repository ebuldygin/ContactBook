package test.contactbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({CRUDGroupTest.class, CRUDPersonTest.class,
    PersonGroupRelationTest.class})
@RunWith(Suite.class)
public class AllTests { 
}
