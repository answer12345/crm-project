package com.sangjie.workbench.dao;

import com.sangjie.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getCustomerByCompany(String company);

    int save(Customer customer);

    List<String> getName();

    String getCustomerIdByName(String name);
}
