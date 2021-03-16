package com.greater.eastmoney.webconfig;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.greater.eastmoney.common.ResultCodeEnum;
import com.greater.eastmoney.exception.BizException;
import com.greater.eastmoney.superresponse.ResultModel;
import com.greater.eastmoney.user.LoginUser;
import com.greater.eastmoney.usercontext.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: yejj
 * @Date: 2021/3/16 10:36
 * @Description:
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  implements ResponseBodyAdvice {

    /**
     * 系统异常捕获
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultModel<Boolean> exceptionHandler(HttpServletRequest request, Exception exception) {

        LoginUser loginUser = UserContextUtil.getUserContext();
        String userId = loginUser != null ? loginUser.getUserId() : "";
        String token = loginUser != null ? loginUser.getToken() : "";
        if (exception instanceof org.apache.catalina.connector.ClientAbortException) {
            log.info("IO中断暂时不做处理，登录信息[thirdUserId:{},token:{}]", userId, token);
            return new ResultModel<Boolean>(true, null, null);
        }
        log.error("系统异常统一捕获,登录信息[thirdUserId:{},token:{}]", userId,token,exception);
        //HttpRequestMethodNotSupportedException: Request method 'GET' not supported
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return handleErrorInfo(request, ResultCodeEnum.SYSTEM_ERROR.getCode(),
                    exception.getMessage());
        }
        return handleErrorInfo(request, ResultCodeEnum.SYSTEM_ERROR.getCode(),
                ResultCodeEnum.SYSTEM_ERROR.getDesc());
    }

//    /**
//     * 外部服务调用异常捕获
//     *
//     * @param request
//     * @param exception
//     * @return
//     */
//    @ExceptionHandler(RpcException.class)
//    @ResponseBody
//    public ResultModel<Boolean> timeoutExceptionHandler(HttpServletRequest request, TimeoutException exception) {
//        LoginUser loginUser = UserContextUtil.getUserContext();
//        String thirdUserId = loginUser != null ? loginUser.getThirdUserId() : "";
//        String token = loginUser != null ? loginUser.getToken() : "";
//        log.error("外部服务调用异常统一捕获：[{}]，登录信息[thirdUserId:{},token:{}]", exception.getMessage(), thirdUserId, token, exception);
//        return handleErrorInfo(request, ResultCodeEnum.RPC_ERROR.getCode(),
//                ResultCodeEnum.RPC_ERROR.getDesc());
//    }

    /**
     * 业务异常捕获
     *
     * @param request
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResultModel<Boolean> bizExceptionHandler(HttpServletRequest request,
            BizException bizException) {
        LoginUser loginUser = UserContextUtil.getUserContext();
        String thirdUserId = loginUser != null ? loginUser.getUserId() : "";
        String token = loginUser != null ? loginUser.getToken() : "";
        // 业务异常日志，打印级别warn
        log.info("业务处理结果统一捕获：[{}-{}]，登录信息[thirdUserId:{},token:{}]", bizException.getCode(), bizException.getMessage(), thirdUserId, token, bizException);
        return handleErrorInfo(request, bizException.getCode(), bizException.getMessage());
    }

    /**
     * 请求参数校验异常
     *
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultModel<Boolean> methodArgumentNotValidExceptionHandler(HttpServletRequest request,
            MethodArgumentNotValidException validException) {
        BindingResult bindingResult = validException.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + ",";
        }
        errorMesssage = errorMesssage.substring(0, errorMesssage.length() - 1);
        LoginUser loginUser = UserContextUtil.getUserContext();
        String thirdUserId = loginUser != null ? loginUser.getUserId() : "";
        String token = loginUser != null ? loginUser.getToken() : "";
        // 请求参数校验异常日志，打印级别info
        log.error("参数校验异常：[{}-{}],登录信息[thirdUserId:{},token:{}]", errorMesssage, Throwables.getStackTraceAsString(validException),thirdUserId,token);
        return handleErrorInfo(request, ResultCodeEnum.PARAM_ILLEGAL.getCode(),
                errorMesssage);
    }

    /**
     * @param request
     * @param code
     * @param message
     * @return
     */
    private ResultModel<Boolean> handleErrorInfo(HttpServletRequest request, String code,
            String message) {
        return new ResultModel<Boolean>(false, code, message);
    }

    /**
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 对特殊的错误码做处理
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body && body instanceof ResultModel) {
            ResultModel<?> result = (ResultModel) body;

            log.info("系统调用返回结果：[{}]", StringUtils.abbreviate(JSON.toJSONString(result), 5000));
            // 对特殊的错误码做处理
            if (!result.isSuccess() && !result.getCode().startsWith("AAAA")) {
                if (result.getCode().equals("HHHHHHH")) {
                    result.setMsg("开户失败，请联系客服!" + "[" + result.getCode() + "]");
                }  else {
                    result.setMsg("系统繁忙!" + "[" + result.getCode() + "]");
                }

            }
            return result;
        }
        return body;
    }
}
