package com.sangjie.workbench.controller;

import com.sangjie.exception.CreateException;
import com.sangjie.setting.domain.User;
import com.sangjie.setting.service.UserService;
import com.sangjie.util.DateTimeUtil;
import com.sangjie.util.UUIDUtil;
import com.sangjie.workbench.domain.Tran;
import com.sangjie.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TranController {

    @Autowired
    private TranService tranService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/workbench/transaction/detail.do")
    public void showDetail(String id, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        Tran tran = tranService.getTran(id);
        String stage = tran.getStage();
        ServletContext application = request.getServletContext();
        Map<String, String> map = (Map<String, String>) application.getAttribute("pMap");

        String possibility = map.get(stage);
        tran.setPossibility(possibility);

        request.setAttribute("t", tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    @RequestMapping(value = "/workbench/transaction/changeStage.do")
    @ResponseBody
    public Map<String, Object> changeStage(HttpServletRequest req) {
        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        Map<String, Object> map = new HashMap<>();

        Tran tran1 = new Tran();
        tran1.setId(id);
        tran1.setExpectedDate(expectedDate);
        tran1.setMoney(money);
        tran1.setStage(stage);
        tran1.setEditTime(DateTimeUtil.getSysTime());
        String userName = ((User) req.getSession().getAttribute("user")).getName();
        tran1.setEditBy(userName);

        Map<String, String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran1.setPossibility(possibility);
        boolean flag = tranService.changeStage(tran1);
        map.put("success", flag);
        map.put("t", tran1);

        return map;
    }

    @RequestMapping(value = "/workbench/transaction/add.do")
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> uList = userService.getUserList();
        request.setAttribute("uList", uList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }

    @RequestMapping(value = "/workbench/transaction/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName() {
        List<String> nameList = tranService.getCustomerName();
        return nameList;
    }

    @RequestMapping(value = "/workbench/transaction/save.do")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String contactsId = request.getParameter("contactsId");
        String activityId = request.getParameter("activityId");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setSource(source);
        tran.setType(type);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        tran.setContactsId(contactsId);
        tran.setActivityId(activityId);

        boolean flag = false;
        try {
            flag = tranService.save(tran, customerName);
        } catch (CreateException e) {
            e.printStackTrace();
        }
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }
    }
}
