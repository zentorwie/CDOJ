package cn.edu.uestc.acmicpc.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.edu.uestc.acmicpc.config.TestContext;
import cn.edu.uestc.acmicpc.db.condition.base.Condition;
import cn.edu.uestc.acmicpc.db.condition.base.Condition.ConditionType;
import cn.edu.uestc.acmicpc.db.condition.base.Condition.Entry;
import cn.edu.uestc.acmicpc.db.condition.base.Condition.JoinedType;
import cn.edu.uestc.acmicpc.db.condition.impl.ProblemCondition;
import cn.edu.uestc.acmicpc.db.dao.iface.ProblemDao;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemDto;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemListDto;
import cn.edu.uestc.acmicpc.db.entity.Problem;
import cn.edu.uestc.acmicpc.service.iface.ProblemService;
import cn.edu.uestc.acmicpc.util.enums.ProblemType;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.helper.ObjectUtil;
import cn.edu.uestc.acmicpc.web.dto.PageInfo;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test cases for {@link ProblemService}.
 */
@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class ProblemServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  @Qualifier("realProblemService")
  private ProblemService problemService;

  @Autowired
  @Qualifier("mockProblemDao")
  private ProblemDao problemDao;

  @BeforeMethod
  public void init() {
    Mockito.reset(problemDao);
  }

  @Test
  public void testGetProblemDtoByProblemId() throws AppException {
    ProblemDto problemDto = ProblemDto.builder().build();
    when(problemDao.getDtoByUniqueField(eq(ProblemDto.class), Mockito.<ProblemDto.Builder>any(),
        eq("problemId"), eq(problemDto.getProblemId()))).thenReturn(problemDto);
    Assert.assertEquals(problemService.getProblemDtoByProblemId(problemDto.getProblemId()), problemDto);
    verify(problemDao).getDtoByUniqueField(eq(ProblemDto.class), Mockito.<ProblemDto.Builder>any(),
        eq("problemId"), eq(problemDto.getProblemId()));
  }

  @Test
  public void testCount() throws AppException {
    ProblemCondition problemCondition = mock(ProblemCondition.class);
    Condition condition = mock(Condition.class);
    when(problemCondition.getCondition()).thenReturn(condition);
    problemService.count(problemCondition);
    verify(problemDao).count(condition);
  }

  @Test
  public void testUpdateProblem() throws AppException {
    ProblemDto problemDto = ProblemDto.builder().build();
    Problem problem = new Problem();
    problem.setProblemId(problemDto.getProblemId());
    when(problemDao.get(problemDto.getProblemId())).thenReturn(problem);
    problemService.updateProblem(problemDto);
    ArgumentCaptor<Problem> captor = ArgumentCaptor.forClass(Problem.class);
    verify(problemDao).addOrUpdate(captor.capture());
    Assert.assertTrue(ObjectUtil.entityEquals(problemDto, captor.getValue()));
    verify(problemDao).get(problemDto.getProblemId());
  }

  @Test(expectedExceptions = AppException.class)
  public void testUpdateProblem_problemNotFound() throws AppException {
    ProblemDto problemDto = ProblemDto.builder().build();
    when(problemDao.get(problemDto.getProblemId())).thenReturn(null);

    problemService.updateProblem(problemDto);
    Assert.fail();
  }

  @Test(expectedExceptions = AppException.class)
  public void testUpdateProblem_problemFoundWithNullId() throws AppException {
    ProblemDto problemDto = ProblemDto.builder().build();
    Problem problem = mock(Problem.class);
    when(problemDao.get(problemDto.getProblemId())).thenReturn(problem);
    when(problem.getProblemId()).thenReturn(null);
    problemService.updateProblem(problemDto);
    Assert.fail();
  }

  @Test
  public void testCreateNewProblem() throws AppException {
    ArgumentCaptor<Problem> captor = ArgumentCaptor.forClass(Problem.class);
    when(problemDao.addOrUpdate(captor.capture())).thenAnswer(new Answer<Problem>() {
      @Override
      public Problem answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        Problem problem = (Problem) args[0];
        problem.setProblemId(1015);
        return null;
      }
    });
    Integer problemId = problemService.createNewProblem();
    Assert.assertEquals(problemId, Integer.valueOf(1015));
  }

  @Test
  public void testGetProblemListDtoList() throws AppException {
    ArgumentCaptor<Condition> captor = ArgumentCaptor.forClass(Condition.class);
    PageInfo pageInfo = PageInfo.create(300L, 20L, 10, 2L);
    problemService.getProblemListDtoList(new ProblemCondition(), pageInfo);
    verify(problemDao).findAll(eq(ProblemListDto.class),
        isA(ProblemListDto.Builder.class), captor.capture());
    Condition condition = captor.getValue();
    Assert.assertEquals(condition.getJoinedType(), JoinedType.AND);
    Assert.assertEquals(condition.getPageInfo(), pageInfo);
  }

  @Test
  public void testGetAllProblemIds() throws AppException {
    ArgumentCaptor<Condition> captor = ArgumentCaptor.forClass(Condition.class);
    problemService.getAllProblemIds(true, ProblemType.NORMAL);
    verify(problemDao).findAll(eq("problemId"), captor.capture());
    Condition condition = captor.getValue();
    List<Entry> entries = condition.getEntries();
    Assert.assertEquals(Entry.of("isVisible",
        ConditionType.STRING_EQUALS, "1"), entries.get(0));
    Assert.assertEquals(Entry.of("type",
        ConditionType.EQUALS, ProblemType.NORMAL.ordinal()), entries.get(1));
  }

}
