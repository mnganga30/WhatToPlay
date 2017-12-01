package com.j_hawk.whattoplay;


import com.j_hawk.whattoplay.services.TestFindGameByID;
import com.j_hawk.whattoplay.services.TestFindGamesByQuery;
import com.j_hawk.whattoplay.services.TestFindHotItems;
import com.j_hawk.whattoplay.services.TestImportCollection;
import com.j_hawk.whattoplay.services.TestSvcImportCollection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestFindGameByID.class, TestFindHotItems.class,
        TestSvcImportCollection.class, TestFindGamesByQuery.class, TestImportCollection.class,
        HomePageActivity.class})
public class InstrumentedTestSuite {}
