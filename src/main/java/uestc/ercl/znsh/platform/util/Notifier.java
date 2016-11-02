package uestc.ercl.znsh.platform.util;

import cn.sel.jutil.lang.JText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uestc.ercl.znsh.common.constant.Regular;

/**
 * 消息推送工具（短信功能需要集成第三方平台）
 */
public class Notifier
{
    private static final Logger logger = LogManager.getLogger(Notifier.class);

    /**
     * 发送短信
     *
     * @param number  目标手机号
     * @param content 短信内容
     *
     * @return 成功与否
     */
    public static boolean sendSMS(String number, String content)
    {
        if(number == null || Regular.match(number, Regular.PHONE))
        {
            logger.error("手机号码<{}>不正确！", number);
            return false;
        }
        if(JText.isNullOrEmpty(content))
        {
            logger.error("短信内容不能为空！");
            return false;
        }
        logger.info(String.format("[发送短信]<号码=%s><内容=%s>", number, content));
        return true;
    }

    /**
     * 发送邮件
     *
     * @param address 邮箱地址
     * @param title   邮件标题
     * @param content 邮件内容
     *
     * @return 成功与否
     */
    public static boolean sendEmail(String address, String title, String content)
    {
        if(address == null || Regular.match(address, Regular.EMAIL))
        {
            logger.error("邮箱地址<{}>不正确！", address);
            return false;
        }
        if(JText.isNullOrEmpty(title))
        {
            logger.error("邮件标题不能为空！");
            return false;
        }
        if(JText.isNullOrEmpty(content))
        {
            logger.error("邮件内容不能为空！");
            return false;
        }
        logger.info(String.format("[发送邮件]<地址=%s><标题=%s><内容=%s>", address, title, content));
        return true;
    }
}