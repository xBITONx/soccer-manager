package cz.muni.fi.pa165.soccermanager.service.facade;

import cz.muni.fi.pa165.soccermanager.dto.TeamDTO;
import cz.muni.fi.pa165.soccermanager.dto.CreateTeamDTO;
import cz.muni.fi.pa165.soccermanager.entity.League;
import cz.muni.fi.pa165.soccermanager.entity.Manager;
import cz.muni.fi.pa165.soccermanager.entity.Player;
import cz.muni.fi.pa165.soccermanager.entity.Team;
import cz.muni.fi.pa165.soccermanager.enums.NationalityEnum;
import cz.muni.fi.pa165.soccermanager.facade.TeamFacade;
import cz.muni.fi.pa165.soccermanager.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author 445720 Martin Hamernik
 * @version 11/16/2017.
 */

@Service
@Transactional
public class TeamFacadeImpl implements TeamFacade {


    private final TeamService teamService;

    private final LeagueService leagueService;

    private final ManagerService managerService;

    private final PlayerService playerService;

    private final BeanMappingService beanMappingService;

    @Inject
    public TeamFacadeImpl(TeamService teamService,
                          LeagueService leagueService,
                          ManagerService managerService,
                          PlayerService playerService,
                          BeanMappingService beanMappingService) {
        this.teamService = teamService;
        this.leagueService = leagueService;
        this.managerService = managerService;
        this.playerService = playerService;
        this.beanMappingService = beanMappingService;
    }

    @Override
    public Long createTeam(CreateTeamDTO team) {
        Team mappedTeam = beanMappingService.mapTo(team, Team.class);

        mappedTeam.setName(team.getName());
        mappedTeam.setOrigin(team.getOrigin());
        mappedTeam.setStadium(team.getStadium());
        mappedTeam.setLeague(leagueService.fetchById(team.getLeagueId()));
        mappedTeam.setPoints(0);
        mappedTeam.setGoalsConceded(0);
        mappedTeam.setGoalsScored(0);
        Team newTeam = teamService.create(mappedTeam);

        return newTeam.getId();
    }

    @Override
    public void deleteTeam(Long teamId) {
        teamService.delete(teamId);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return beanMappingService.mapTo(teamService.fetchAll(), TeamDTO.class);
    }

    @Override
    public List<TeamDTO> getTeamsByLeague(Long leagueId) {
        League league = leagueService.fetchById(leagueId);
        return beanMappingService.mapTo(teamService.fetchByLeague(league), TeamDTO.class);
    }

    @Override
    public List<TeamDTO> getTeamsByOrigin(NationalityEnum nationality) {
        return beanMappingService.mapTo(teamService.fetchByOrigin(nationality), TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamByManager(Long managerId) {
        Manager manager = managerService.fetchById(managerId);
        Team team = teamService.fetchByManager(manager);
        return (team == null) ? null : beanMappingService.mapTo(team, TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        if (id == null) {
            return null;
        }
        Team team = teamService.fetchById(id);
        return (team == null) ? null : beanMappingService.mapTo(team, TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamByName(String name) {
        Team team = teamService.fetchByName(name);
        return (team == null) ? null : beanMappingService.mapTo(team, TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamByPlayer(Long id) {
        Player player = playerService.fetchById(id);
        Team team = teamService.fetchByPlayer(player);
        return (team == null) ? null : beanMappingService.mapTo(team, TeamDTO.class);
    }

    @Override
    public void addPlayer(Long playerId, Long teamId) {
        teamService.addPlayer(playerService.fetchById(playerId), teamService.fetchById(teamId));
    }

    @Override
    public void removePlayer(Long playerId,Long teamId) {
        teamService.removePlayer(playerService.fetchById(playerId), teamService.fetchById(teamId));
    }

    @Override
    public void assignManager(Long managerId, Long teamId) {
        teamService.assignManager(managerService.fetchById(managerId), teamService.fetchById(teamId));
    }

    @Override
    public void removeManager(Long teamId) {
        teamService.removeManager(teamService.fetchById(teamId));
    }

    @Override
    public void joinLeague(Long leagueId, Long teamId) {
        teamService.joinLeague(leagueService.fetchById(leagueId), teamService.fetchById(teamId));
    }

    @Override
    public void leaveLeague( Long teamId) {
        teamService.leaveLeague(teamService.fetchById(teamId));
    }
}
