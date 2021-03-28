package com.greater.eastmoney.service.favor;

import com.greater.eastmoney.common.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @Author: yejj
 * @Date: 2021/3/26 15:42
 * @Description:
 **/
@Data
public class favorRequest extends BaseRequest {

    @NotEmpty(message = "用户id不能为空")
    private String userId;

    @NotNull(message = "用户姓名不能为空")
    private String userName;


    @Override
    public String validateLogic() {
        return null;
    }
}
