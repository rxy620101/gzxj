package com.ruoyi.scholarShip.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

//申诉表
@Data
public class AppealInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long appealId;
    private String sno;
    private String stuName;
    private String appealYear;
    private String appealTerm;
    private String appealReason;
    private String appealStatus;
    private String appealResult;
    private String comment;
    private String checkPerson;
    private String majorName;

}
