package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pollen.pollen_service.domain.Pine;

public interface PineRepository extends JpaRepository<Pine, String> {
    Pine findByAreaNo(String areaNo);
}
