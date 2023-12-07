package pollen.pollen_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pollen.pollen_service.domain.Pine;

import java.util.List;

public interface PineRepository extends JpaRepository<Pine, String> {
    Pine findByAreaNo(String areaNo);

    @Query("select p from Pine p where p.areaNo= '1100000000' or p.areaNo= '2600000000' or p.areaNo= '2700000000' or p.areaNo= '2900000000' or p.areaNo= '3000000000' or p.areaNo= '4215000000' or p.areaNo= '5000000000'")
    List<Pine> findInitData();
}
