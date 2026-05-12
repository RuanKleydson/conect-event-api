package dev.ruan.conectapi.repository;

import dev.ruan.conectapi.entities.Submission;
import dev.ruan.conectapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository
        extends JpaRepository<Submission, Long> {

    List<Submission> findByParticipant(User user);

    List<Submission> findByStatus(Submission.Status status);
}
