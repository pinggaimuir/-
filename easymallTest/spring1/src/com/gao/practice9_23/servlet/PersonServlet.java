package com.gao.practice9_23.servlet;

import com.gao.practice9_23.Exception.XXXException;
import com.gao.practice9_23.service.PersonService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/23.
 */
@Component("personServlet")
public class PersonServlet {
    @Resource(name="personService")
    private PersonService personService;


    public void save() throws XXXException {
        personService.addUser();
//        throw new XXXException("servlet中抛出了异常");
    }

    public void del(){
        personService.delPerson();
    }

    public void find(String str){
        personService.find(str);
    }
}
