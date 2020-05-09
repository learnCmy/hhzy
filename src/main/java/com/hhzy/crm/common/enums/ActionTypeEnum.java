package com.hhzy.crm.common.enums;

import lombok.Getter;

@Getter
public enum ActionTypeEnum  implements CodeEnum{


    INSERT(1,"添加"),
    UPDATE(2,"更新"),
    DELETE(3,"删除"),
    EXPORT(4,"导出"),
    IMPORT(5,"导入");

    private Integer code;

    private String message;

    ActionTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
