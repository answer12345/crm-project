crm项目

1.添加依赖
    spring, springmvc, mybatis, jsp, servlet, mysql, jackson, ,mybatis-spring, jstl
2.添加配置文件
    applicationContext.xml, springmvc.xml, mybatis.xml

3.数据字典：
    是在应用程序中，做表单元素选择内容用的相关数据
    下拉框，单选框，复选框
    数据字典普遍被应用在下拉框
    对数据字典分类保存
    缓存机制：
        在服务器启动阶段，将数据保存到服务器缓存中，服务器启动阶段，数据始终存在
        服务器处于开启状态，我们就可以一直从缓存中取得数据


4.clue:
    (1).线索模糊查询并且展示（pageList）

    (2).创建，修改，删除功能

    (3).clue detail:
        1.展示clue
            删除，编辑
        2.展示clueRemark
            删除，添加，修改
        3.展示clue关联的市场活动
            删除
            添加：模糊查询出相关联的市场活动并且与该线索不相关

    (4).线索转换(同一个事务)：
        1.通过线索id获取线索对象
        2.通过线索对象提取客户信息，如果客户不存在，新建客户
        3.通过线索对象提取联系人信息，保存联系人
        4.查询出与该线索关联的备注信息(clueRemark)：
            创建客户备注对象，添加客户备注
            创建联系备注
        5."clueActivityRelation"的关系转换到“contactActivityRelation”的关系
        6.如果有创建交易需求，创建交易
            如果创建交易历史，则创建交易历史(tranHistory)
        7.删除线索备注(clueRemark)
        8.删除线索和市场活动的关系(clueActivityRelation)
        9.删除线索

5.交易：
    (1).创建功能(同一个事务)：
      1.通过表单域标签提交(因为参数太多).
      2.通过customer name去查找customerID, 如果找不到就创建
      3.给tran设置customerId，保存tran
      4.创建交易历史