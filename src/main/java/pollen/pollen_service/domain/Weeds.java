package pollen.pollen_service.domain;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Weeds {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String areaNo;

    private int today;

    private int tomorrow;

    private int dayaftertomorrow;
}