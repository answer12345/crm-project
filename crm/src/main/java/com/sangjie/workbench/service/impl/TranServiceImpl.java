package com.sangjie.workbench.service.impl;

import com.sangjie.exception.CreateException;
import com.sangjie.util.DateTimeUtil;
import com.sangjie.util.UUIDUtil;
import com.sangjie.workbench.dao.CustomerDao;
import com.sangjie.workbench.dao.TranDao;
import com.sangjie.workbench.dao.TranHistoryDao;
import com.sangjie.workbench.domain.Customer;
import com.sangjie.workbench.domain.Tran;
import com.sangjie.workbench.domain.TranHistory;
import com.sangjie.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Override
    public Tran getTran(String id) {
        Tran tran = tranDao.getTranById(id);
        return tran;
    }

    @Override
    public boolean changeStage(Tran tran) {
        if (tranDao.changeStage(tran) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCustomerName() {
        List<String> l = customerDao.getName();
        return l;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public boolean save(Tran tran, String customerName) throws CreateException {
        String id = customerDao.getCustomerIdByName(customerName);
        //如果不存在就新建一个customer
        if (id == null) {
            id = UUIDUtil.getUUID();
            String owner = tran.getOwner();
            String createBy = tran.getCreateBy();
            String createTime = DateTimeUtil.getSysTime();
            String name = tran.getName();
            String contactsSummary = tran.getContactSummary();
            String nextContactTime = tran.getNextContactTime();
            String description = tran.getDescription();

            Customer customer = new Customer();
            customer.setId(id);
            customer.setOwner(owner);
            customer.setName(name);
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(contactsSummary);
            customer.setNextContactTime(nextContactTime);
            customer.setDescription(description);

            if (customerDao.save(customer) != 1) {
                throw new CreateException();
            }
        }
        tran.setCustomerId(id);
        if (tranDao.save(tran) != 1) {
            throw new CreateException();
        }

        //创建tran history
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        if (tranHistoryDao.save(tranHistory) != 1) {
            throw new CreateException();
        }

        return true;
    }
}
