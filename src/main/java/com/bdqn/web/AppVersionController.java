package com.bdqn.web;

import com.bdqn.entity.AppInfo;
import com.bdqn.entity.AppVersion;
import com.bdqn.entity.DevUser;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/versionCon")
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private AppInfoService appInfoService;
    /**
     * 进入新增历史版本页面  显示历史版本
     * @param model
     * @return
     */
    @RequestMapping(value = "/appversionadd/{id}")
    public String appversionadd(HttpSession session, Model model, @PathVariable(value = "id") Long appid) {
        //获取当前 APP 历史版本
        List<AppVersion> appVersions = appVersionService.queryByAppid(appid);
        model.addAttribute("appVersionList", appVersions);

        //存储 当前 操作的 APP id
        session.setAttribute("appVersion", appid);
        return "developer/appversionadd";
    }

    /**
     * 新增当前 APP 版本
     *
     * @return
     */
    @RequestMapping(value = "/addversionsave")
    public String addversionsave(HttpSession session, Model model, AppVersion appVersion, MultipartFile a_downloadLink) throws IOException {

        /**
         * apk 上传
         */
        if (!a_downloadLink.isEmpty()) {
            String path = session.getServletContext().getRealPath("/statics/uploadfiles/");
            String filename = a_downloadLink.getOriginalFilename();
            a_downloadLink.transferTo(new File(path, filename));  //上传
            //获取当前操作人员
            DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");

            if (appVersion != null) {
                /**
                 * 新增版本
                 */
                appVersion.setCreatedby(devUserSession.getId());
                appVersion.setCreationdate(new Date());
                appVersion.setApkfilename(filename);
                appVersion.setApklocpath(path);
                appVersion.setDownloadlink("/statics/uploadfiles/"+filename);
                appVersionService.insertVersion(appVersion);

                //修改/添加  最新版本号
                AppInfo appInfo = new AppInfo();
                appInfo.setId(appVersion.getAppid());
                appInfo.setVersionid(appVersion.getId());
                appInfoService.updateApp(appInfo);
            }

        }

        return "redirect:/appInfoCon/appList";
    }
}
