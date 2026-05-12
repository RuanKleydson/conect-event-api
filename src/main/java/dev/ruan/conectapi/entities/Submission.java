package dev.ruan.conectapi.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tb_submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Instant submissionDate;

    private Instant approvalDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;


    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Instant getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Instant approvalDate) {
        this.approvalDate = approvalDate;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
}
