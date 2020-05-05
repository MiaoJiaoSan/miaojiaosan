package com.miaojiaosan.user.dal.repository.database;

import com.miaojiaosan.user.dal.dao.UserAccountDAO;
import com.miaojiaosan.user.dal.dao.UserPersonDAO;
import com.miaojiaosan.user.dal.mapperex.UserAccountMapperEx;
import com.miaojiaosan.user.dal.mapperex.UserPersonMapperEx;
import com.miaojiaosan.user.domain.UserDO;
import com.miaojiaosan.user.domain.data.Account;
import org.dozer.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author miaojiaosan
 * @date 2020/4/25
 */
@Repository
public class UserDataBaseRepository {

  @Resource
  private UserAccountMapperEx userAccountMapperEx;
  @Resource
  private UserPersonMapperEx userPersonMapperEx;
  @Resource
  private Mapper mapper;

  @Transactional(rollbackFor = Exception.class)
  public Boolean addDataBase(UserDO userDO){
    UserPersonDAO userPersonDAO = new UserPersonDAO();
    BeanUtils.copyProperties(userDO, userPersonDAO);
    int row = userPersonMapperEx.insert(userPersonDAO);
    if(row >= 0) {
      Account account = userDO.getAccount();
      UserAccountDAO userAccountDAO = new UserAccountDAO();
      BeanUtils.copyProperties(account, userAccountDAO);
      userAccountDAO.setUserId(userPersonDAO.getId());
      userAccountDAO.setModify(userPersonDAO.getId());
      row &= userAccountMapperEx.insert(userAccountDAO);
    }
    return row > 0;
  }



  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public UserDO byAccountDataBase(Account account){
    UserAccountDAO userAccountDAO = userAccountMapperEx.byAccount(account.getAccount());
    UserPersonDAO userPersonDAO = userPersonMapperEx.selectByPrimaryKey(userAccountDAO.getUserId());
    UserDO userDO = new UserDO();
    BeanUtils.copyProperties(userPersonDAO, userDO);
    account = mapper.map(userAccountDAO, Account.class);
    userDO.setAccount(account);
    return userDO;
  }


}