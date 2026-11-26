package com.agh;

import com.agh.app.StoreFacadeTest;
import com.agh.model.BranchTest;
import com.agh.model.LaptopTest;
import com.agh.model.StoreManagerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        BranchTest.class,
        LaptopTest.class,
        StoreManagerTest.class,
        StoreFacadeTest.class
})
public class ProjectTestSuite {
}
