package com.bdqn.web;

import com.bdqn.entity.AppInfo;
import com.bdqn.entity.DevUser;
import com.bdqn.service.AppCategoryService;
import com.bdqn.service.AppInfoService;
import com.bdqn.service.DataDictionaryService;
import com.bdqn.utils.JsonResult;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "/appInfoCon")
public class AppInfoController {

    @Resource
    private AppInfoService appInfoService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private DataDictionaryService dataDictionaryService;


    /**
     * 01
     * APP列表
     *
     * @param session
     * @param model
     * @param pageIndex
     * @return
     */
    @RequestMapping("/appList")
    public String appList(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                          String softwarename,
                          @RequestParam(required = false, defaultValue = "0") Long status,
                          @RequestParam(required = false, defaultValue = "0") Long flatformid,
                          @RequestParam(required = false, defaultValue = "0") Long categorylevel1,
                          @RequestParam(required = false, defaultValue = "0") Long categorylevel2,
                          @RequestParam(required = false, defaultValue = "0") Long categorylevel3) {
        // 获取登录用户信息
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        model.addAttribute("devUserSession", devUserSession);

        AppInfo appInfo = new AppInfo();
        appInfo.setSoftwarename(softwarename);
        appInfo.setStatus(status);
        appInfo.setFlatformid(flatformid);
        appInfo.setCategorylevel1(categorylevel1);
        appInfo.setCategorylevel2(categorylevel2);
        appInfo.setCategorylevel3(categorylevel3);

        PageInfo<AppInfo> pageInfo = appInfoService.queryAppInfo(devUserSession.getId(), pageIndex, appInfo);
        model.addAttribute("appInfos", pageInfo);


        //填充状态
        model.addAttribute("statusList", dataDictionaryService.queryByCode("APP_STATUS"));
        //填充所属平台
        model.addAttribute("flatFormList", dataDictionaryService.queryByCode("APP_FLATFORM"));
        //填充一级分类
        model.addAttribute("categoryLevel1List", appCategoryService.queryAppCategory(null));

        //二、三级数据
        if (categorylevel1 != null) {
            model.addAttribute("queryCategoryLevel2List", appCategoryService.queryAppCategory(categorylevel1));
        }
        if (categorylevel2 != null) {
            model.addAttribute("queryCategoryLevel3List", appCategoryService.queryAppCategory(categorylevel2));
        }
        //反填数据
        model.addAttribute("querySoftwareName", softwarename);
        model.addAttribute("queryStatus", status);
        model.addAttribute("queryFlatformId", flatformid);
        model.addAttribute("queryCategoryLevel1", categorylevel1);
        model.addAttribute("queryCategoryLevel2", categorylevel2);
        model.addAttribute("queryCategoryLevel3", categorylevel3);

        return "developer/appinfolist";
    }


    /**
     * 02
     * APP上传
     *
     * @param session
     * @param appInfo
     * @param a_logopicpath
     * @return
     */
    @PostMapping("/insertApp")
    public String insertApp(HttpSession session, AppInfo appInfo, MultipartFile a_logopicpath) {

        //上传的位置
        String server_path = session.getServletContext().getRealPath("/statics/uploadfiles/");
        //文件名称
        String filename = a_logopicpath.getOriginalFilename();
        try {
            //文件上传
            a_logopicpath.transferTo(new File(server_path, filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        appInfo.setDevid(devUserSession.getId());
        appInfo.setCreatedby(devUserSession.getId());
        appInfo.setCreationdate(new Date());
        appInfo.setUpdatedate(new Date());
        appInfo.setLogopicpath("/statics/uploadfiles/" + filename);
        appInfo.setLogolocpath(server_path + filename);
        appInfoService.insertApp(appInfo);
        return "redirect:/appInfoCon/appList";
    }


    /**
     * 03
     * 删除
     * @param session
     * @param id
     * @param flag
     * @param a_logopicpath
     * @return
     */
    @GetMapping("/delfile")
    @ResponseBody
    public JsonResult delfile(HttpSession session, Long id, String flag, MultipartFile a_logopicpath) {
        //删除图片
        if (flag.equals("logo")) {
            //根据 id查询数据
            AppInfo appInfo = appInfoService.queryById(id);

            File file = new File(appInfo.getLogolocpath());
            try {
                file.delete();
                appInfo.setLogolocpath("");
                appInfo.setLogopicpath("");
                appInfoService.updateApp(appInfo);
                return new JsonResult(true);
            } catch (Exception e) {
                return new JsonResult(false);
            }
        }

        return new JsonResult(false);
    }


    /**
     * 04
     * 修改
     * @param session
     * @param attach
     * @param appInfo
     * @return
     */
    @PostMapping("/appinfomodifysave")
    public String appinfomodifysave(HttpSession session, MultipartFile attach, AppInfo appInfo) {
        if (!attach.isEmpty()){
            try {
                //上传的位置
                String server_path = session.getServletContext().getRealPath("/statics/uploadfiles/");
                //文件名称
                String filename = attach.getOriginalFilename();
                //文件上传
                attach.transferTo(new File(server_path, filename));
                appInfo.setLogopicpath("/statics/uploadfiles/" + filename);
                appInfo.setLogolocpath(server_path + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DevUser devUserSession = (DevUser) session.getAttribute("devUserSession");
        appInfo.setUpdatedate(new Date());
        appInfo.setDevid(devUserSession.getId());
        appInfo.setModifyby(devUserSession.getId());
        appInfo.setModifydate(new Date());
        appInfoService.updateApp(appInfo);
        return "redirect:/appInfoCon/appList";
    }





    /**
     * 验证APK
     *
     * @return
     */
    @GetMapping("/apkexist")
    @ResponseBody
    public JsonResult apkname(String apkname) {
        AppInfo appInfo = appInfoService.queryApkexist(apkname);
        if (appInfo == null) {
            return new JsonResult(true);
        }

        return new JsonResult(false);
    }

    @RequestMapping("/appinfomodify/{id}")
    public String appinfomodify(Model model, @PathVariable(value = "id") Long id) {
        AppInfo appInfo = appInfoService.queryById(id);
        model.addAttribute("appInfo", appInfo);
        return "developer/appinfomodify";
    }

}
