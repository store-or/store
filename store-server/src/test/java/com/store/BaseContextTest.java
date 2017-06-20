package com.store;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: laizy
 * Date: 12-11-7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-store.xml"})
public class BaseContextTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

}
