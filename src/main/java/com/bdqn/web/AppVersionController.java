package com.bdqn.web;

import com.bdqn.entity.AppInfo;
import com.bdqn.entity.AppVersion;
import com.bdqn.entity.DevUser;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.AppVersionService;
import com.bdqn.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
     *
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
                appVersion.setDownloadlink("/statics/uploadfiles/" + filename);
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


    /**
     * 进入修改 APP界面
     * 显示历史版本  和 当前版本信息反填
     *
     * @return
     */
    @RequestMapping(value = "/appversionmodify/{id}/{appid}")
    public String appversionmodify(Model model, @PathVariable(value = "id") Long id, @PathVariable(value = "appid") Long appid, AppVersion appVersion) {
        //获取当前 APP 历史版本
        List<AppVersion> appVersions = appVersionService.queryByAppid(appid);
        model.addAttribute("appVersionList", appVersions);


        //获取当前 APP版本信息
        AppVersion appVersion1 = appVersionService.queryById(id);
        model.addAttribute("appVersion", appVersion1);

        return "developer/appversionmodify";
    }

    /**
     * 修改最新版本信息
     *
     * @param session
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/appversionmodifysave")
    public String appversionmodifysave(HttpSession session, AppVersion appVersion, MultipartFile attach) {

        //文件上传
        if (!attach.isEmpty()) {
            String path = session.getServletContext().getRealPath("/statics/uploadfiles/");
            String filename = attach.getOriginalFilename();
            try {
                attach.transferTo(new File(path, filename));  //上传
            } catch (IOException e) {
                e.printStackTrace();
            }
            appVersion.setApkfilename(filename);
            appVersion.setApklocpath(path);
            appVersion.setDownloadlink("/statics/uploadfiles/" + filename);
        }else{
            appVersion.setDownloadlink("");
            appVersion.setApkfilename("");
            appVersion.setApklocpath("");
        }

        //获取当前操作人员
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");

        //填充修改者 和 修改时间
        appVersion.setModifyby(devUserSession.getId());
        appVersion.setModifydate(new Date());
        appVersion.setVersioninfo(appVersion.getVersioninfo().trim());//去除前后空格

        appVersionService.updateVersion(appVersion);

        return "redirect:/appInfoCon/appList";
    }


    /**
     * 删除 APK
     *
     * @param id
     * @param flag
     * @return
     */
    @RequestMapping(value = "/delfile")
    @ResponseBody
    public JsonResult delfile(Long id, String flag) {
        //删除图片
        if (flag.equals("apk")) {
            //根据 id查询数据
            AppVersion appVersion = appVersionService.queryById(id);

            File file = new File(appVersion.getApklocpath());
            try {
                file.delete();
                appVersion.setDownloadlink("");
                appVersion.setApkfilename("");
                appVersion.setApklocpath("");
                int i = appVersionService.updateVersion(appVersion);
                if (i > 0) {
                    return new JsonResult(true);
                } else {
                    return new JsonResult(false);
                }
            } catch (Exception e) {
                return new JsonResult(false);
            }
        }

        return new JsonResult(false);
    }
}
