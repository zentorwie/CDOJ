package cn.edu.uestc.acmicpc.db;

import cn.edu.uestc.acmicpc.config.IntegrationTestContext;
import cn.edu.uestc.acmicpc.db.dao.iface.UserDao;
import cn.edu.uestc.acmicpc.db.dao.iface.UserSerialKeyDao;
import cn.edu.uestc.acmicpc.db.entity.User;
import cn.edu.uestc.acmicpc.db.entity.UserSerialKey;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.exception.FieldNotUniqueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for {@link UserSerialKey}.
 */
@ContextConfiguration(classes = {IntegrationTestContext.class})
public class UserSerialKeyDatabaseITTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private UserDao userDao;

  @Autowired
  private UserSerialKeyDao userSerialKeyDao;

  @Test
  public void testFindUserSerialKeyByUserName() throws FieldNotUniqueException, AppException {
    // TODO(fish): add test case and not assert null here.
    User user = (User) userDao.getEntityByUniqueField("userName", "administrator");
    Assert.assertEquals(user.getUserId(), Integer.valueOf(1));
    UserSerialKey userSerialKey =
        (UserSerialKey) userSerialKeyDao.getEntityByUniqueField("userId", user.getUserId(),
            null, true);
    Assert.assertNull(userSerialKey);
  }
}
