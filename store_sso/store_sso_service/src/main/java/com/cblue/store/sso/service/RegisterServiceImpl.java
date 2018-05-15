package com.cblue.store.sso.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.mapper.TbUserMapper;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.pojo.TbUserExample;
import com.cblue.store.pojo.TbUserExample.Criteria;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;

	@Override
	public ResponseResult checkData(String param, int type) {
		// TODO Auto-generated method stub
		// 根据不同跳转进行查询
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		// 1 检查用户名 2检查手机号
		// 校验用户名：/user/check/${param}/1
		// 校验手机号：/user/check/${param}/2
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else {
			return ResponseResult.build(500, "提交校验类型不对");
		}

		List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
		// 如果tbUser存在，就告诉用户不能注册
		if (tbUsers.size() > 0 && tbUsers != null) {
			// 不能注册
			return ResponseResult.ok(false);
		} else {
			// 可以注册
			return ResponseResult.ok(true);
		}
	}

	@Override
	public ResponseResult register(TbUser tbUser) {
		// TODO Auto-generated method stub
		// 服务端数据校验
		if (StringUtils.isBlank(tbUser.getUsername())
				|| StringUtils.isBlank(tbUser.getPassword())
				|| StringUtils.isBlank(tbUser.getPhone())) {
			return ResponseResult.build(400, "数据提交不完整");
		}
		// 校验用户名
		ResponseResult result = checkData(tbUser.getUsername(),
				1);
		// 不能注册
		if (!(Boolean) result.getData()) {
			return ResponseResult.build(200, "此用户名已经重复");
		}
		// 校验手机号
		ResponseResult phoneResult = checkData(
				tbUser.getPhone(), 2);
		// 不能注册
		if (!(Boolean) phoneResult.getData()) {
			return ResponseResult.build(200, "此手机号已经重复");
		}

		// 可以注册
		// 补数据
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		String md5Password = DigestUtils.md5Hex(tbUser.getPassword());
		System.out.println("原始密码是" + tbUser.getPassword());
		System.out.println("加密后的密码是" + md5Password);
		tbUser.setPassword(md5Password);

		// 保存数据
		tbUserMapper.insert(tbUser);
		return ResponseResult.ok();
	}

}
