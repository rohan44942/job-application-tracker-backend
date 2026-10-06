package com.jobtracker.repository;

import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.JobType;
import com.jobtracker.entity.User;
import com.jobtracker.entity.WorkMode;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {
    Page<JobApplication> findByUser(User user, Pageable pageable);
    Optional<JobApplication> findByIdAndUser(Long id, User user);
    long countByUser(User user);
    long countByUserAndStatus(User user, ApplicationStatus status);
    long countByStatus(ApplicationStatus status);
    long countByAppliedDate(LocalDate appliedDate);

    @Query("""
            select a from JobApplication a
            where a.user = :user
            and (:company is null or lower(a.companyName) like lower(concat('%', :company, '%')))
            and (:jobTitle is null or lower(a.jobTitle) like lower(concat('%', :jobTitle, '%')))
            and (:status is null or a.status = :status)
            and (:location is null or lower(a.location) like lower(concat('%', :location, '%')))
            and (:workMode is null or a.workMode = :workMode)
            and (:jobType is null or a.jobType = :jobType)
            and (:fromDate is null or a.appliedDate >= :fromDate)
            and (:toDate is null or a.appliedDate <= :toDate)
            """)
    Page<JobApplication> search(User user, String company, String jobTitle, ApplicationStatus status, String location, WorkMode workMode, JobType jobType, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
