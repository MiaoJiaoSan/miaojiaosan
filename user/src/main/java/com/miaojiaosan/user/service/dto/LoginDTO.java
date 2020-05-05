package com.miaojiaosan.user.service.dto;

import com.miaojiaosan.common.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author miaojiaosan
 * @date 2020/5/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginDTO extends BaseDTO {
  /**
   * 账号
   */
  private String account;
  /**
   * 密码
   */
  private String password;

  private String code;
}