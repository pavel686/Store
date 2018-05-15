package com.cblue.store.sso.service;

import com.cblue.store.common.pojo.ResponseResult;

public interface LoginService {
	
	ResponseResult login(String username,String password);

}
