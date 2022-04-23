package com.sangjie.workbench.controller;

import com.sangjie.dto.ClueRemarkAndClueDto;
import com.sangjie.dto.PageListDto;
import com.sangjie.exception.ConvertException;
import com.sangjie.setting.domain.User;
import com.sangjie.util.DateTimeUtil;
import com.sangjie.util.UUIDUtil;
import com.sangjie.workbench.domain.Activity;
import com.sangjie.workbench.domain.Clue;
import com.sangjie.workbench.domain.Tran;
import com.sangjie.workbench.service.ActivityService;
import com.sangjie.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {

    @Resource
    private ClueService clueService;

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/workbench/clue/selectUserList.do")
    @ResponseBody
    public List<User> selectUserList() {
        List<User> nameList = clueService.selectUserList();
        return nameList;

    }


    @RequestMapping(value = "/workbench/clue/saveClue.do")
    @ResponseBody
    public Map<String, Boolean> addClue(Clue clue, HttpServletRequest request) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);

        boolean flag = clueService.addClue(clue);

        Map<String, Boolean> map  = new HashMap();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/workbench/clue/pageList.do")
    @ResponseBody
    public List<Clue> pageList(PageListDto pageListDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", pageListDto.getFullname());
        map.put("company", pageListDto.getCompany());
        map.put("owner", pageListDto.getOwner());
        map.put("mphone", pageListDto.getMphone());
        map.put("phone", pageListDto.getPhone());
        map.put("state", pageListDto.getState());
        map.put("source", pageListDto.getSource());
        int pageNo = Integer.valueOf(pageListDto.getPageNo());
        int pageSize = Integer.valueOf(pageListDto.getPageSize());
        int pagePass = (pageNo-1) * pageSize;
        map.put("pagePass", pagePass);
        map.put("pageSize", pageSize);



        List<Clue> clues = clueService.pageList(map);

        return clues;
    }

    @RequestMapping(value = "/workbench/clue/detail.do")
    @ResponseBody
    public void showDetail(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException {
        Clue clue = clueService.getClue(id);
        request.setAttribute("c", clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    @RequestMapping(value = "/workbench/clue/showRemarkList.do")
    @ResponseBody
    public List<ClueRemarkAndClueDto> showRemark(String clueId) {

        List<ClueRemarkAndClueDto> l = clueService.showRemark(clueId);

        return l;
    }

    @RequestMapping(value = "/workbench/clue/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> showActivityList(String clueId) {
        List<Activity> l = clueService.showActivityList(clueId);
        return l;
    }

    @RequestMapping(value = "/workbench/clue/unbund.do")
    @ResponseBody
    public Map<String, Boolean> unbund(String id) {
        int res = clueService.deleteRelation(id);
        Map<String, Boolean> map = new HashMap<>();
        if (res == 1) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/workbench/clue/convert.do")
    @ResponseBody
    public void convert(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String clueId = request.getParameter("clueId");
        Tran tran = null;
        String flag = request.getParameter("flag");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)) {
            tran = new Tran();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            tran.setId(id);
            tran.setMoney(money);
            tran.setExpectedDate(expectedDate);
            tran.setName(name);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
        }

        boolean convertResult = false;
        try {
            convertResult = clueService.convert(clueId, tran, createBy);
            if (convertResult) {
                response.sendRedirect(request.getContextPath()+ "/workbench/clue/index.jsp");
            }
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/workbench/clue/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> findActivityByName(String aname, String clueId) {
        Map map = new HashMap();
        map.put("aname", aname);
        map.put("clueId", clueId);
        List<Activity> l = clueService.getActivityListAndNoRelation(map);
        return l;
    }

    @RequestMapping(value = "/workbench/clue/bund.do")
    @ResponseBody
    public Map<String, Boolean> bund(HttpServletRequest request) {
        Map<String, Boolean> map = new HashMap<>();
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        boolean res = clueService.bund(cid, aids);
        if (res) {
            map.put("success", true);
        } else  {
            map.put("success", false);
        }
        return map;
    }

}
