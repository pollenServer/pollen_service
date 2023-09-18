package pollen.pollen_service.domain;

import lombok.Getter;
import pollen.pollen_service.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Oak extends BaseEntity {

    @Id
    private String areaNo;

    @Column
    private int today;

    @Column
    private int tomorrow;

    @Column
    private int dayaftertomorrow;

    @Column
    private int twodaysaftertomorrow;
}