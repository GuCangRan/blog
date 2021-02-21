package com.gcr.myException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by GuaiWenWo on 2021/2/21 15:48
 */
@RestController
public class testException_Controller {

    @GetMapping(value = "ex1")
    public Object query1() {
        throw new BusinessException(ErrorEnum.NO_PERMISSION.getErrorCode(),
                ErrorEnum.NO_PERMISSION.getErrorMsg());

    }

    @GetMapping(value = "ex")
    public Object query() {
        int dd = 2 / 0;
        return null;
    }

}
