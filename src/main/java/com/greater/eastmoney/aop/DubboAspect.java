package com.greater.eastmoney.aop;

import com.alibaba.fastjson.JSON;
import com.greater.eastmoney.common.BaseRequest;
import com.greater.eastmoney.common.ResultCodeEnum;
import com.greater.eastmoney.exception.BizException;
import com.greater.eastmoney.superresponse.ResultModel;
import com.greater.eastmoney.util.ValidateBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 * 用于dubbo接口注解， 打印参数，返回值，异常时统一处理，封装返回数据
 * Dubbo服务切面
 */
@Component
@Aspect
@Slf4j
public class DubboAspect {

	@Pointcut("@annotation(dubbo)")
	private void pointCutMethod(Dubbo dubbo) {
	}

	@Around("pointCutMethod(dubbo)")
	public Object doAround(ProceedingJoinPoint pjp, Dubbo dubbo) {
		Object object = null;

		try {
			// 获取请求参数
			Object[] args = pjp.getArgs();
			// 参数需要继承baseRequest
			BaseRequest baseRequest = (BaseRequest) args[0];

			// 上下文存储UUID  用于日志全链路追踪
			if (StringUtils.isNotEmpty(baseRequest.getCorrelationId())) {
				MDC.put("correlationId", baseRequest.getCorrelationId());
			} else {
				MDC.put("correlationId", UUID.randomUUID().toString());
			}

			// 打印请求日志
			log.info("[{}]请求报文:{}", dubbo.value(), JSON.toJSONString(args[0]));

			// 统一做参数校验，而不是在每个接口中处理
			// notEmpty 使用hebernate中的注解。
			// 校验参数合法性
			ValidateBeanUtils.validateAnnotation(args[0]);

			// 参数逻辑校验
			String errorMsg = baseRequest.validateLogic();
			if (StringUtils.isNotBlank(errorMsg)) {
				throw new BizException(ResultCodeEnum.PARAM_ILLEGAL, errorMsg);
			}

			// 执行具体的方法
			object = pjp.proceed();

		} catch (BizException exception) {
			// 打印请求日志
			log.info("[{}]异常：{},{}", dubbo.value(), exception.getCode(), exception.getMessage());
			object = handleBizException(exception);
		} catch (Exception exception) {
			log.error("[{}]系统异常：", dubbo.value(), exception);
			object = handleSystemException();
		} catch (Throwable exception) {
			log.error("[{}]未知异常：", dubbo.value(), exception);
			object = handleSystemException();
		} finally {
			// 打印请求日志
			log.info("[{}]返回报文：{}",dubbo.value(), JSON.toJSONString(object));

			// 清除上下文内容
			MDC.clear();
		}

		return object;
	}

	/**
	 * 处理业务异常
	 *
	 * @param
	 *
	 * @param bizException
	 * @return
	 */
	public ResultModel<Object> handleBizException(BizException bizException) {
		return new ResultModel<Object>(bizException.getCode(), bizException.getMessage());
	}

	/**
	 * 处理系统异常
	 * 
	 * @return
	 */
	public ResultModel<Object> handleSystemException() {
		return new ResultModel<Object>(ResultCodeEnum.SYSTEM_ERROR.getCode(),
				ResultCodeEnum.SYSTEM_ERROR.getDesc());
	}
}
