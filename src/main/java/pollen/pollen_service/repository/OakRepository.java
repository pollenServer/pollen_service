package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pollen.pollen_service.domain.Oak;

public interface OakRepository extends JpaRepository<Oak, Long> {
    Oak findByAreaNo(String areaNo);
}
