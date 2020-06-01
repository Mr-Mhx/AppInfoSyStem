package com.bdqn.web;

import com.bdqn.entity.DevUser;
import com.bdqn.service.DevUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/dev")
public class DevUserController {
    @Resource
    private DevUserService devUserService;

    @PostMapping(value = "/login")
    public String login(HttpSession session, Model model, String devcode, String devpassword) {
        DevUser login = devUserService.login(devcode, devpassword);
        if (login != null) {
            session.setAttribute("devUserSession", login);
            return "redirect:/jsp/developer/main.jsp";
        }
        model.addAttribute("error", "用户名或密码错误，请重新登录");
        return "devlogin";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //销毁所有 session信息
        return "redirect:/index.jsp";
    }
}
