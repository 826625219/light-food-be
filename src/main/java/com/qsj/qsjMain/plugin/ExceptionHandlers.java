package com.qsj.qsjMain.plugin;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.qsj.qsjMain.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.qsj.qsjMain.model.ResultCode.SERVICE_BUSY;
import static com.qsj.qsjMain.utils.JsonUtil.toJsonStr;


/**
 * Spring 全局异常处理器
 */
@RestControllerAdvice
public class ExceptionHandlers {

    @Value("${spring.profiles.active}")
    private String profile;

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(Throwable.class)
    public Message<?> handleThrowable(HttpServletRequest request, Throwable e) {
        printStackTrace(request, e);
        if("dev".equals(profile)){
            Throwable rootCause = Throwables.getRootCause(e);
            return Message.error(SERVICE_BUSY, rootCause);
        }
        return Message.error(SERVICE_BUSY);
    }

    /**
     * 打印异常堆栈和入参
     */
    private void printStackTrace(HttpServletRequest request, Throwable e) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, Object> map = Maps.transformValues(
                params, arr -> (arr == null || arr.length == 0) ? null : arr.length == 1 ? arr[0] : arr
        );
        // 屏蔽找不到handler的异常，这种一般是探测，不上报
        if(e.getClass().getName().equals("org.springframework.web.servlet.NoHandlerFoundException")) {
            logger.warn("handle:{},  msg: {}, params:{}", e.getClass().getName(), e.getMessage(), toJsonStr(map), e);
            return;
        }
        logger.error("handle:{},  msg: {}, params:{}", e.getClass().getName(), e.getMessage(), toJsonStr(map), e);
    }

}
