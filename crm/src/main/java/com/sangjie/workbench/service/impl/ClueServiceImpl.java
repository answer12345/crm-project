package com.sangjie.workbench.service.impl;

import com.sangjie.dto.ClueRemarkAndClueDto;
import com.sangjie.exception.ConvertException;
import com.sangjie.setting.dao.UserDao;
import com.sangjie.setting.domain.User;
import com.sangjie.util.DateTimeUtil;
import com.sangjie.util.UUIDUtil;
import com.sangjie.workbench.dao.*;
import com.sangjie.workbench.domain.*;
import com.sangjie.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private UserDao userDao;

    @Resource
    private ClueDao clueDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private ContactsDao contactsDao;

    @Resource
    private ClueRemarkDao clueRemarkDao;

    @Resource
    private CustomerRemarkDao customerRemarkDao;

    @Resource
    private ContactsRemarkDao contactsRemarkDao;

    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public List<User> selectUserList() {
        List<User> userList = userDao.getUserList();

        return userList;
    }

    @Override
    public boolean addClue(Clue clue) {
        boolean flag = true;

        int res = clueDao.addClue(clue);

        if (res != 1) {
            flag = false;
        }

        return  flag;
    }

    @Override
    public List<Clue> pageList(Map<String, Object> map) {

        List<Clue> clues = clueDao.pageList(map);
        return clues;
    }

    @Override
    public Clue getClue(String id) {
        Clue clue = clueDao.getClue(id);
        return clue;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public boolean convert(String clueId, Tran tran, String createBy) throws ConvertException {
        boolean flag = true;
        //1.????????????id??????????????????
        Clue clue = clueDao.getClueById(clueId);

        //2.???????????????????????????????????????????????????????????????????????????
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByCompany(company);

        if (customer == null) {
            customer = new Customer();
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            String owner = clue.getOwner();
            String phone = clue.getPhone();
            String website = clue.getWebsite();
            String contactSummary = clue.getContactSummary();
            String nextContactTime = clue.getNextContactTime();
            String address = clue.getAddress();
            String description = clue.getDescription();

            customer.setAddress(address);
            customer.setContactSummary(contactSummary);
            customer.setCreateTime(createTime);
            customer.setId(id);
            customer.setOwner(owner);
            customer.setDescription(description);
            customer.setPhone(phone);
            customer.setWebsite(website);
            customer.setNextContactTime(nextContactTime);


            int result1 = customerDao.save(customer);
            if (result1 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //3.?????????????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        String id = UUIDUtil.getUUID();
        String fullname = clue.getFullname();
        String appellation = clue.getAppellation();
        String owner = clue.getOwner();
        String source = clue.getSource();
        String customerId = customer.getId();
        String email = clue.getEmail();
        String mphone = clue.getMphone();
        String job = clue.getJob();
        String createTime = DateTimeUtil.getSysTime();
        String contactSummary = clue.getContactSummary();
        String description = clue.getDescription();

        contacts.setDescription(description);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(contactSummary);
        contacts.setCreateTime(createTime);
        contacts.setJob(job);
        contacts.setMphone(mphone);
        contacts.setEmail(email);
        contacts.setCustomerId(customerId);
        contacts.setSource(source);
        contacts.setId(id);
        contacts.setFullname(fullname);
        contacts.setAppellation(appellation);
        contacts.setOwner(owner);

        int result2 = contactsDao.save(contacts);
        if (result2 != 1) {
            flag = false;
            throw new ConvertException();
        }

        //4.??????????????????????????????????????????
        List<ClueRemark> l = clueRemarkDao.query(clueId);
        for (ClueRemark clueRemark : l) {

            //?????????????????????????????????????????????
            String noteContent = clueRemark.getNoteContent();
            String customerRemarkId = UUIDUtil.getUUID();
            String customerRemarkCreateTime = DateTimeUtil.getSysTime();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(customerRemarkId);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(customerRemarkCreateTime);
            customerRemark.setNoteContent(noteContent);
            customerRemark.setEditFlag("0");
            int res = customerRemarkDao.save(customerRemark);
            if (res != 1) {
                flag = false;
                throw new ConvertException();
            }

            //??????????????????
            String contactRemarkId = UUIDUtil.getUUID();
            String contactRemarkCreateTime = DateTimeUtil.getSysTime();
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(contactRemarkId);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(contactRemarkCreateTime);
            contactsRemark.setEditFlag("0");
            int res2 = contactsRemarkDao.save(contactsRemark);
            if (res2 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //5."clueActivityRelation"?????????????????????contactActivityRelation????????????
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getById(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            String contactsActivityId = UUIDUtil.getUUID();
            contactsActivityRelation.setId(contactsActivityId);
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int res3 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (res3 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //6.??????????????????????????????????????????
        if (tran != null) {
            tran.setOwner(clue.getOwner());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            tran.setDescription(clue.getDescription());
            tran.setSource(clue.getSource());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            int res4 = tranDao.save(tran);
            if (res4 != 1) {
                flag = false;
                throw new ConvertException();
            }

            //????????????????????????????????????????????????(tranHistory)
            TranHistory tranHistory = new TranHistory();
            String tranHistoryId = UUIDUtil.getUUID();
            String tranHistoryCreateTime = DateTimeUtil.getSysTime();
            tranHistory.setCreateBy(createBy);
            tranHistory.setId(tranHistoryId);
            tranHistory.setCreateTime(tranHistoryCreateTime);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            int res5 = tranHistoryDao.save(tranHistory);
            if (res5 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //7.??????????????????(clueRemark)
        for (ClueRemark clueRemark : l) {
            String clueRemarkId = clueRemark.getId();
            int res6 = clueRemarkDao.delete(clueRemarkId);

            if (res6 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //8.????????????????????????????????????(clueActivityRelation)
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            String ClueActivityRelationId = clueActivityRelation.getId();
            int res7 = clueActivityRelationDao.delete(ClueActivityRelationId);
            if (res7 != 1) {
                flag = false;
                throw new ConvertException();
            }
        }

        //9.????????????
        int res8 = clueDao.delete(clueId);
        if (res8 != 1) {
            flag = false;
            throw new ConvertException();
        }

        return flag;
    }

    @Override
    public List<ClueRemarkAndClueDto> showRemark(String clueId) {
        List<ClueRemarkAndClueDto> clueRemarkAndClueDtos = clueRemarkDao.showRemark(clueId);

        return clueRemarkAndClueDtos;
    }

    @Override
    public List<Activity> showActivityList(String clueId) {
        List<Activity> l = clueActivityRelationDao.showActivityList(clueId);
        return l;
    }

    @Override
    public int deleteRelation(String id) {
        int res = clueActivityRelationDao.delete(id);
        return res;
    }

    @Override
    public List<Activity> getActivityListAndNoRelation(Map map) {
        List<Activity> l = clueActivityRelationDao.getActivityListAndNoRelation(map);
        return l;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        int sz = aids.length;
        boolean flag = true;
        for (int i = 0; i < sz; i++) {
            String aid = aids[i];
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(aid);
            clueActivityRelation.setClueId(cid);
            String id = UUIDUtil.getUUID();
            clueActivityRelation.setId(id);
            int res = clueActivityRelationDao.bund(clueActivityRelation);
            if (res != 1) {
                flag = false;
            }
        }

        return flag;
    }
}
