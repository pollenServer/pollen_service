package pollen.pollen_service.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
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

    public Oak() {}

    public Oak(String areaNo) {
        this.areaNo = areaNo;
    }
}