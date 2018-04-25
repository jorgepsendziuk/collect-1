package com.genesis.coletor.android;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.genesis.coletor.android.activities.MainActivityTest;
import com.genesis.coletor.android.utilities.PermissionsTest;
import com.genesis.coletor.android.utilities.TextUtilsTest;

/**
 * Suite for running all unit tests from one place
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        //Name of tests which are going to be run by suite
        MainActivityTest.class,
        PermissionsTest.class,
        TextUtilsTest.class
})

public class AllTestsSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
}
