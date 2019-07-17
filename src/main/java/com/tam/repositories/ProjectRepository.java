package com.tam.repositories;

import com.tam.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {
    public Optional<Project> findByProjectID(String id);
    public void deleteByProjectID(String projectID);
    Optional<List<Project>> findAllByCollaboratorsContaining(String username);
    Optional<List<Project>> findAllByModelsContaining(String modelID);
    Optional<List<Project>> findAllByInvitesContaining(String username);
}
