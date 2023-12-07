package pollen.pollen_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InitResponse extends BaseEntity {

    private String areaNo;

    private int oak;

    private int pine;

    private int weeds;

    private int max;
}
