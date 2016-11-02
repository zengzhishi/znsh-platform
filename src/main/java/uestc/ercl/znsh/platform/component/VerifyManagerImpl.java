/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.platform.component.def.VerifyManager;
import uestc.ercl.znsh.platform.constants.VerifyCodeStatus;
import uestc.ercl.znsh.platform.dao.VerifyCodeDAO;
import uestc.ercl.znsh.platform.entity.VCode;
import uestc.ercl.znsh.platform.util.Notifier;

@Component
public class VerifyManagerImpl implements VerifyManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyManager.class);
    private final VerifyCodeDAO verifyCodeDAO;
    private final SysConfigManagerImpl sysConfigService;

    @Autowired
    public VerifyManagerImpl(VerifyCodeDAO verifyCodeDAO, SysConfigManagerImpl sysConfigService)
    {
        Assert.notNull(verifyCodeDAO);
        Assert.notNull(sysConfigService);
        this.verifyCodeDAO = verifyCodeDAO;
        this.sysConfigService = sysConfigService;
    }

    @Override
    public String generateVerifyCode()
    {
        return "123456";
        //TODO return RandomString.generateWithNumbersAndLetters(6, RandomString.CharacterCase.UPPER, JText.StringParity.ANY);
    }

    @Override
    public boolean verifyBySMS(String number)
    {
        return Notifier.sendSMS(number, String.format("【智能审核系统】您正在进行身份验证。验证码：%s，有效时间：3分钟。", generateVerifyCode()));
    }

    @Override
    public boolean verifyByEmail(String address)
    {
        return Notifier.sendEmail(address, "智能审核系统身份验证", String.format("验证码：%s。有效时间：3分钟。", generateVerifyCode()));
    }

    @Override
    public VerifyCodeStatus checkAdminVerifyCode(int adminPk, String code)
    {
        VCode vcode = verifyCodeDAO.select(String.valueOf(adminPk));
        return getVerifyCodeStatus(code, vcode);
    }

    @Override
    public VerifyCodeStatus checkAppVerifyCode(String appId, String code)
    {
        VCode vcode = verifyCodeDAO.select(appId);
        return getVerifyCodeStatus(code, vcode);
    }

    private VerifyCodeStatus getVerifyCodeStatus(String code, VCode vcode)
    {
        if(vcode == null)
        {
            return VerifyCodeStatus.NONE;
        }
        String expectedCode = vcode.getCode();
        if(expectedCode == null || expectedCode.length() != 6)
        {
            return VerifyCodeStatus.ERROR;
        }
        if(System.currentTimeMillis() - vcode.getCreateTime() > sysConfigService.getVCodeExpires())
        {
            return VerifyCodeStatus.EXPIRED;
        }
        return expectedCode.equals(code) ? VerifyCodeStatus.VALID : VerifyCodeStatus.INVALID;
    }
}