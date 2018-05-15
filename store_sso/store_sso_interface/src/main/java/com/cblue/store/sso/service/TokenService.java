package com.cblue.store.sso.service;

import com.cblue.store.common.pojo.ResponseResult;

public interface TokenService {
	
   ResponseResult getUserByToken(String token);	

}
