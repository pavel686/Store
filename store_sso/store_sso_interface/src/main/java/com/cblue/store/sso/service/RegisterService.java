package com.cblue.store.sso.service;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbUser;

public interface RegisterService {

	ResponseResult checkData(String param,int type);
	
	ResponseResult register(TbUser tbUser);
	

}
