package com.test.tambolaticketgenerator.repository;

import com.test.tambolaticketgenerator.model.TicketNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketNodeRepository extends JpaRepository<TicketNodeEntity, Long> {
}
