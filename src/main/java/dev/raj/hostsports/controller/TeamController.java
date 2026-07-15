package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.team.AddPlayerRequest;
import dev.raj.hostsports.dto.team.TeamRequest;
import dev.raj.hostsports.dto.team.TeamResponse;
import dev.raj.hostsports.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Team creation and roster managment")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request,
                                                   @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request,currentUser));
    }

    @PostMapping("/{teamId}/players")
    public ResponseEntity<TeamResponse> addPlayer(@PathVariable Long teamId, @Valid
                                                  @RequestBody AddPlayerRequest request,
                                                  @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(teamService.addPlayer(teamId, request, currentUser));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long temaId){
        return ResponseEntity.ok(teamService.getTeamById(temaId));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TeamResponse>> getMyTeams(@AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(teamService.getMyTeams(currentUser));
    }
}
