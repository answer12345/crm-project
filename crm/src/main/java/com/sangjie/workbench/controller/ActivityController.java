package com.sangjie.workbench.controller;

import com.sangjie.setting.domain.User;
import com.sangjie.setting.service.UserService;
import com.sangjie.util.DateTimeUtil;
import com.sangjie.util.UUIDUtil;
import com.sangjie.vo.PageInationVo;
import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.ActivityRemark;
import com.sangjie.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/workbench/activity/getUserList.do", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList() {

        List<User> list =  userService.getUserList();
        return list;
    }

    @RequestMapping(value = "/workbench/activity/save.do")
    @ResponseBody
    public Map saveActivity(Activity activity, HttpServletRequest request) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        int res = activityService.save(activity);
        System.out.println(res);
        Map map = new HashMap();
        if (res == 1) {
            map.put("success", true);
        }
        return map;
    }

    @RequestMapping("/workbench/activity/pageList.do")
    @ResponseBody
    public PageInationVo pageList(HttpServletRequest req) {
        PageInationVo vo = new PageInationVo();
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算掠过的记录
        int pagePast = (pageNo - 1) * pageSize;
        Map map = new HashMap();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pagePast", pagePast);
        map.put("pageSize", pageSize);

        List<Activity> dataList = activityService.getActivityList(map);
        int total = activityService.getTotalActivity(map);

        vo.setDataList(dataList);
        vo.setTotal(total);
        //System.out.println(vo);
        return vo;
    }

    @RequestMapping(value = "/workbench/activity/delete.do")
    @ResponseBody
    public Map deleteActivity(String[] id) {
        //System.out.println(id);
        Map map = new HashMap();
        boolean flag = activityService.deleteList(id);
        if (flag) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/workbench/activity/getUserListAndActivity.do")
    @ResponseBody
    public Map getUserListAndActivity(String id) {
        Map map = new HashMap();
        List<User> uList = userService.getUserList();
        Activity a = activityService.getActivityById(id);
        System.out.println(a);

        map.put("uList", uList);
        map.put("a", a);

        return map;
    }

    @RequestMapping(value = "/workbench/activity/update.do")
    @ResponseBody
    public Map updateActivity(Activity activity, HttpServletRequest request) {
        Map map = new HashMap();

        User user = (User) request.getSession().getAttribute("user");

        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(user.getName());
        System.out.println(activity);

        int i= activityService.updateActivity(activity);

        if (i == 1) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping("/workbench/activity/detail.do")
    public ModelAndView showDetail(String id){
        Activity activity = activityService.showDetail(id);

        ModelAndView mv = new ModelAndView();
        mv.addObject("a", activity);

        mv.setViewName("forward:/workbench/activity/detail.jsp");
        return mv;
    }


    @RequestMapping(value = "/workbench/activity/showRemarkList.do")
    @ResponseBody
    public List<ActivityRemark> showRemarkList(String activityId) {
        List<ActivityRemark> activityRemarks = activityService.showRemarkList(activityId);

        //System.out.println(activityRemarks);
        return activityRemarks;

    }

    @RequestMapping(value = "/workbench/activity/removeRemarkL.do")
    @ResponseBody
    public Map removeRemark(String id) {
        Map map = new HashMap();

        boolean flag = activityService.removeRemark(id);

        if(flag) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;

    }

}
