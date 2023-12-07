package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pollen.pollen_service.domain.Oak;

import java.util.List;

public interface OakRepository extends JpaRepository<Oak, String> {
    Oak findByAreaNo(String areaNo);

    @Query("select o from Oak o where o.areaNo= '1100000000' or o.areaNo= '2600000000' or o.areaNo= '2700000000' or o.areaNo= '2900000000' or o.areaNo= '3000000000' or o.areaNo= '4215000000' or o.areaNo= '5000000000'")
    List<Oak> findInitData();
}
