package com.store.exception;

import com.core.json.JsonResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by laizy on 2017/5/23.
 */
@Component
public class CustomExceptionHandler implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            ResponseBody responseBody = ((HandlerMethod) handler).getMethod().getAnnotation(ResponseBody.class);
            if (responseBody != null) {
                handlerResponseBody(response, ex);
            } else {
                ModelAndView mav = new ModelAndView("/WEB-INF/content/exception/error.jsp");
                mav.addObject("exception", ex);
                return mav;
            }
        }
        return null;
    }

    private void handlerResponseBody(HttpServletResponse response, Exception ex) {
        logger.error("error.", ex);
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=UTF-8");
        try {
            printWriter = response.getWriter();
            printWriter.write(new JsonResponse(JsonResponse.CODE_FAILURE, ex.getMessage()).toString());
        } catch (IOException e) {
            logger.error("EXCEPTION_HANDLE_FAILED;", e);
        } finally {
            IOUtils.closeQuietly(printWriter);
        }
    }
}
