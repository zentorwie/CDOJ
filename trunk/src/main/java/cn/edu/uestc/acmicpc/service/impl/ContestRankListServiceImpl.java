package cn.edu.uestc.acmicpc.service.impl;

import cn.edu.uestc.acmicpc.db.condition.impl.ContestTeamCondition;
import cn.edu.uestc.acmicpc.db.condition.impl.StatusCondition;
import cn.edu.uestc.acmicpc.db.condition.impl.TeamUserCondition;
import cn.edu.uestc.acmicpc.db.dto.impl.contest.ContestDto;
import cn.edu.uestc.acmicpc.db.dto.impl.contest.ContestShowDto;
import cn.edu.uestc.acmicpc.db.dto.impl.contestproblem.ContestProblemSummaryDto;
import cn.edu.uestc.acmicpc.db.dto.impl.contestteam.ContestTeamListDto;
import cn.edu.uestc.acmicpc.db.dto.impl.status.StatusListDto;
import cn.edu.uestc.acmicpc.db.dto.impl.teamUser.TeamUserListDto;
import cn.edu.uestc.acmicpc.service.iface.ContestProblemService;
import cn.edu.uestc.acmicpc.service.iface.ContestRankListService;
import cn.edu.uestc.acmicpc.service.iface.ContestService;
import cn.edu.uestc.acmicpc.service.iface.ContestTeamService;
import cn.edu.uestc.acmicpc.service.iface.StatusService;
import cn.edu.uestc.acmicpc.service.iface.TeamUserService;
import cn.edu.uestc.acmicpc.util.enums.ContestRegistryStatusType;
import cn.edu.uestc.acmicpc.util.enums.ContestType;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.helper.ArrayUtil;
import cn.edu.uestc.acmicpc.web.rank.RankList;
import cn.edu.uestc.acmicpc.web.rank.RankListBuilder;
import cn.edu.uestc.acmicpc.web.rank.RankListStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description
 */
@Service
public class ContestRankListServiceImpl extends AbstractService implements ContestRankListService {

  private final Map<String, RankList> rankListPool = new HashMap<>();
  private final long FETCH_INTERVAL = 10 * 1000; // 10 seconds

  private ContestProblemService contestProblemService;
  private StatusService statusService;
  private ContestService contestService;
  private ContestTeamService contestTeamService;
  private TeamUserService teamUserService;

  @Autowired
  public ContestRankListServiceImpl(ContestProblemService contestProblemService,
      StatusService statusService,
      ContestService contestService,
      ContestTeamService contestTeamService,
      TeamUserService teamUserService) {
    this.contestProblemService = contestProblemService;
    this.statusService = statusService;
    this.contestService = contestService;
    this.contestTeamService = contestTeamService;
    this.teamUserService = teamUserService;
  }

  private List<StatusListDto> fetchStatusList(Integer contestId) throws AppException {
    StatusCondition statusCondition = new StatusCondition();
    statusCondition.contestId = contestId;
    statusCondition.isForAdmin = false;
    // Sort by time
    statusCondition.orderFields = "time";
    statusCondition.orderAsc = "true";
    return statusService.getStatusList(statusCondition);
  }

  private List<ContestTeamListDto> fetchTeamList(Integer contestId) throws AppException {
    ContestDto contestDto = contestService.getContestDtoByContestId(contestId);
    if (contestDto.getType() == ContestType.INHERIT.ordinal()) {
      contestDto = contestService.getContestDtoByContestId(contestDto.getParentId());
    }
    contestId = contestDto.getContestId();

    ContestTeamCondition contestTeamCondition = new ContestTeamCondition();
    contestTeamCondition.contestId = contestId;
    // Fetch accepted teams
    contestTeamCondition.status = ContestRegistryStatusType.ACCEPTED.ordinal();

    // Fetch all
    List<ContestTeamListDto> contestTeamList = contestTeamService.getContestTeamList(
        contestTeamCondition, null);

    if (contestTeamList.size() > 0) {
      List<Integer> teamIdList = new LinkedList<>();
      for (ContestTeamListDto team : contestTeamList) {
        teamIdList.add(team.getTeamId());
      }
      TeamUserCondition teamUserCondition = new TeamUserCondition();
      teamUserCondition.orderFields = "id";
      teamUserCondition.orderAsc = "true";
      teamUserCondition.teamIdList = ArrayUtil.join(teamIdList.toArray(), ",");
      // Search team users
      List<TeamUserListDto> teamUserList = teamUserService.getTeamUserList(teamUserCondition);

      // Put users into teams
      for (ContestTeamListDto team : contestTeamList) {
        team.setTeamUsers(new LinkedList<TeamUserListDto>());
        team.setInvitedUsers(new LinkedList<TeamUserListDto>());
        for (TeamUserListDto teamUserListDto : teamUserList) {
          if (team.getTeamId().compareTo(teamUserListDto.getTeamId()) == 0) {
            // Put users
            if (teamUserListDto.getAllow()) {
              team.getTeamUsers().add(teamUserListDto);
            }
          }
        }
      }
    }

    return contestTeamList;
  }

  @Override
  public synchronized RankList getRankList(Integer contestId,
      Integer contestType,
      Boolean frozen, Integer frozenTime) throws AppException {
    String rankListName = contestId.toString() + ":" + frozen;
    RankList lastModified = rankListPool.get(rankListName);
    if (lastModified == null ||
        (System.currentTimeMillis() - lastModified.lastFetched.getTime()) > FETCH_INTERVAL) {
      ContestShowDto contestShowDto = contestService.getContestShowDtoByContestId(contestId);
      if (contestShowDto == null) {
        throw new AppException("No such contest.");
      }

      // Fetch problem list
      List<ContestProblemSummaryDto> contestProblemList = contestProblemService.
          getContestProblemSummaryDtoListByContestId(contestId);

      // Fetch status list
      List<StatusListDto> statusList = fetchStatusList(contestId);

      RankListBuilder rankListBuilder = new RankListBuilder();
      // Set problem
      for (ContestProblemSummaryDto problem : contestProblemList) {
        rankListBuilder.addRankListProblem(problem.getProblemId().toString());
      }

      if (contestType == ContestType.INVITED.ordinal()) {
        // Invited type contest, should include team information
        rankListBuilder.enableTeamMode();

        for (ContestTeamListDto team : fetchTeamList(contestId)) {
          rankListBuilder.addRankListTeam(team);
        }
      }

      // Set status
      for (StatusListDto status : statusList) {
        if (contestShowDto.getStartTime().after(status.getTime()) ||
            contestShowDto.getEndTime().before(status.getTime())) {
          // Out of time.
          continue;
        }
        Boolean isFrozen = false;
        if (frozen
            && contestShowDto.getEndTime().getTime() - status.getTime().getTime() <= frozenTime) {
          isFrozen = true;
        }
        rankListBuilder.addStatus(new RankListStatus(
            1, // Total tried
            status.getReturnTypeId(), // Return type id
            status.getProblemId().toString(), // Problem id
            status.getUserName(), // User name
            status.getNickName(), // Nick name
            status.getEmail(), // Email
            status.getName(),
            status.getTime().getTime() - contestShowDto.getStartTime().getTime()),
            isFrozen); // Time
      }

      RankList result = rankListBuilder.build();

      rankListPool.put(rankListName, result);
      return result;
    } else {
      return lastModified;
    }
  }
}
