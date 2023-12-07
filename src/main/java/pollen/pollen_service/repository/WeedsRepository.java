package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pollen.pollen_service.domain.Weeds;

import java.util.List;

public interface WeedsRepository extends JpaRepository<Weeds, String> {
    Weeds findByAreaNo(String areaNo);

    @Query("select w from Weeds w where w.areaNo= '1100000000' or w.areaNo= '2600000000' or w.areaNo= '2700000000' or w.areaNo= '2900000000' or w.areaNo= '3000000000' or w.areaNo= '4215000000' or w.areaNo= '5000000000'")
    List<Weeds> findInitData();
}
