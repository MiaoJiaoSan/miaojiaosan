package com.miaojiaosan.material.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDTO<T> {

  private List<T> list;

  private Long total;
}
