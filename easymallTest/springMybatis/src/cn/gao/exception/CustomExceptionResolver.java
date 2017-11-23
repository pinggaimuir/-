//package cn.gao.exception;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 自定义异常处理器
// * Created by tarena on 2016/10/8.
// */
//@Component
//public class CustomExceptionResolver implements HandlerExceptionResolver {
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
//                              HttpServletResponse httpServletResponse, Object o, Exception e) {
//
//    CustomException customException=null;
//        if(e instanceof CustomException){
//            customException=(CustomException)e;
//        }else{
//            customException=new CustomException("发生未知错误！");
//        }
//        ModelAndView modelAndView=new ModelAndView();
//        modelAndView.addObject("message",customException.getMessage());
//        modelAndView.setViewName("error");
//        return modelAndView;
//    }
//}
