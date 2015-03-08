package cn.edu.uestc.acmicpc.testing;

import cn.edu.uestc.acmicpc.config.IntegrationTestContext;
import cn.edu.uestc.acmicpc.db.dto.impl.user.UserDto;
import cn.edu.uestc.acmicpc.service.testing.TestUtil;
import cn.edu.uestc.acmicpc.service.testing.UserProvider;
import cn.edu.uestc.acmicpc.util.exception.AppException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

/**
 * Basic integration test using real java beans and real database.
 */
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@ContextConfiguration(classes = {IntegrationTestContext.class})
public class PersistenceITTest extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  protected UserProvider userProvider;

  @BeforeMethod
  protected void beforeMethod() throws Exception {
    setUp();
  }

  public void setUp() throws Exception {
  }

  protected Integer getTestUserId() throws AppException {
    UserDto user = userProvider.createUser("testUser" + TestUtil.getUniqueId());
    return user.getUserId();
  }
}