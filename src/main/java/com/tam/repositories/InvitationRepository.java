package com.tam.repositories;

import com.tam.model.InvitationResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends MongoRepository<InvitationResource, String> {
    Optional<List<InvitationResource>> findAllByUsername(String id);
    Optional<InvitationResource> findByInvitationID(String invitationID);
    Optional<InvitationResource> deleteByInvitationID(String invitationID);
    void deleteAllByProjectID(String projectID);
}
