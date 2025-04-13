package com.tao.lmx.controller;


import com.tao.lmx.dto.ServerConfigDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfigController {

    @GetMapping("/config")
    public String configForm(Model model) {
        model.addAttribute("serverConfig", new ServerConfigDto());
        return "config-form";
    }

    @PostMapping("/config")
    public String submitConfig(ServerConfigDto serverConfig, Model model) {
        // 여기서 서버 설정 정보를 처리할 수 있습니다.
        model.addAttribute("message", "서버 설정이 성공적으로 제출되었습니다!");
        model.addAttribute("serverConfig", serverConfig);
        return "config-result";
    }
}
