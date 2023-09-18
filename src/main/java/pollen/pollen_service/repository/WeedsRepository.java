package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pollen.pollen_service.domain.Weeds;

public interface WeedsRepository extends JpaRepository<Weeds, String> {
    Weeds findByAreaNo(String areaNo);
}
