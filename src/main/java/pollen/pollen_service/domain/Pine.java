package pollen.pollen_service.domain;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pine {

    @Id
    @GeneratedValue
    private Long id;

    private String areaNo;

    @Nullable
    private int today;

    @Nullable
    private int tomorrow;

    @Nullable
    private int dayaftertomorrow;
}