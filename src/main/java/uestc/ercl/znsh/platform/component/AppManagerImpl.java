/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 版权所有：电子科技大学・信息与软件工程学院・嵌入式实时计算研究所（简称ERCL）
 * http://www.is.uestc.edu.cn
 *
 * 未经许可，任何其他组织或个人不得将此程序——
 * 1、用于商业用途。
 * 2、修改或再发布。
 */
package uestc.ercl.znsh.platform.component;

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import cn.sel.jutil.lang.JText;
import cn.sel.jutil.security.SecureUtil;
import cn.sel.shc.core.HttpClient;
import cn.sel.shc.core.HttpResponse;
import cn.sel.shc.core.RequestHolder;
import cn.sel.shc.core.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.constant.cmd.AppCtlCmd;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.common.logging.LogType;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.component.def.SysConfigManager;
import uestc.ercl.znsh.platform.dao.AppConfigDAO;
import uestc.ercl.znsh.platform.dao.AppDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class AppManagerImpl extends _SysLoggerHolder implements AppManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppManager.class);
    private static final int REQ_ID_CREATE = 0;
    private static final int REQ_ID_REMOVE = 1;
    private static final int REQ_ID_ACTIVATE = 2;
    private static final int REQ_ID_SUSPEND = 3;
    private final HttpClient httpClient = HttpClient.getInstance();
    private final AppConfigDAO configDAO;
    private final AppDAO appDAO;
    private final SysConfigManager sysConfigManager;
    private ResponseHandler handler = new ResponseHandler()
    {
        @Override
        protected boolean onFinished(int requestId, HttpResponse httpResponse)
        {
            return false;
        }

        @Override
        protected void onSuccess(int requestId, HttpResponse httpResponse)
        {
            switch(requestId)
            {
                case REQ_ID_CREATE:
                    break;
                case REQ_ID_REMOVE:
                    break;
                case REQ_ID_ACTIVATE:
                    break;
                case REQ_ID_SUSPEND:
                    break;
            }
        }

        @Override
        protected void onFailure(int requestId, HttpResponse httpResponse)
        {
        }

        @Override
        protected void onError(int requestId, String error)
        {
        }
    };

    @Autowired
    public AppManagerImpl(AppConfigDAO configDAO, AppDAO appDAO, SysConfigManager sysConfigManager)
    {
        Assert.notNull(configDAO, "AppConfigDAO 注入失败！不能为空！");
        Assert.notNull(appDAO, "AppDAO 注入失败！不能为空！");
        Assert.notNull(sysConfigManager, "SysConfigManager 注入失败！不能为空！");
        this.configDAO = configDAO;
        this.appDAO = appDAO;
        this.sysConfigManager = sysConfigManager;
        sysLogger = new SysLogManagerImpl(LogSource.SYS, appDAO.getJdbcTemplate().getDataSource());
    }

    @Override
    public boolean setConfig(@NonNull String appId, Config config)
    {
        return configDAO.insertOrUpdate(appId, config);
    }

    @Override
    public Config getConfig(@NonNull String appId, String configName)
    {
        return configDAO.select(appId, configName);
    }

    @Override
    public List<Config> getConfigList(@NonNull String appId)
    {
        return configDAO.select(appId);
    }

    @Override
    public void create(@NonNull String name, @Nullable String desc, @NonNull AppType type, @NonNull AppStatus status, @NonNull String master,
            @NonNull String pid, @NonNull String phone, @Nullable String email, @NonNull String account, @NonNull String password)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        App app = new App();
        app.setName(name);
        app.setDesc(desc);
        app.setType(type);
        app.setStatus(status);
        app.setMaster(master);
        app.setPid(pid);
        app.setPhone(phone);
        app.setEmail(email);
        app.setAccount(account);
        app.setPassword(password);
        app.setPk(newAppId(name));
        app.setCreateTime(System.currentTimeMillis());
        try
        {
            if(appDAO.insert(app))
            {
                switch(status)
                {
                    case NORMAL:
                        if(activate(app.appId()) == null)
                        {
                            throw new ZNSH_ServiceException("应用创建成功。但启动失败！");
                        }
                    case NEW:
                    case SUSPEND:
                    case DISABLED:
                        LOGGER.warn("预期之外的数据流！");
                        break;
                }
            }
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("创建应用失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public App get(@Nullable String appId)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            return appDAO.get(appId);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查询应用失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public App find(@Nullable String key)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            return appDAO.find(key);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查找应用失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public List<App> find(@Nullable String pk, @Nullable String name, @Nullable AppType type, @Nullable AppStatus status, @Nullable String master,
            @Nullable String pid, @Nullable String phone, @Nullable String email, long from, int count)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(from < 0)
        {
            from = 0;
        }
        if(count < 1)
        {
            count = 20;
        }
        try
        {
            return appDAO.select(pk, name, master, pid, phone, email, type, status, from, count);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查找应用列表失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public void update(@NonNull String pk, @Nullable String name, @Nullable String desc, @Nullable String master, @Nullable String pid,
            @Nullable String phone, @Nullable String email, @Nullable String account)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        App app = new App();
        app.update(pk, name, desc, master, pid, phone, email, account);
        try
        {
            if(!appDAO.update(app))
            {
                throw new ZNSH_ServiceException("更新应用信息失败！");
            }
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新应用信息失败！", e);
            throw new ZNSH_ServiceException("更新应用信息失败！", e);
        }
        throw new ZNSH_IllegalArgumentException("未选择应用！");
    }

    @Override
    public void updatePassword(@NonNull String pk, @NonNull String password)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        App app = new App();
        app.updatePassword(pk, password);
        try
        {
            if(!appDAO.update(app))
            {
                throw new ZNSH_ServiceException("更新应用密码失败！");
            }
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新应用密码失败！", e);
            throw new ZNSH_ServiceException("更新应用密码失败！", e);
        }
    }

    @Override
    public Map<String, String> delete(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            //设置应用状态为永久禁用
            Map<String, String> fails = new HashMap<>();
            try
            {
                Set<String> set = appDAO.delete(pks);
                if(set != null)
                {
                    set.forEach(pk->fails.put(pk, "删除信息失败！"));
                }
            } catch(ZNSH_DataAccessException e)
            {
                e.printStackTrace();
            }
            //通知所在集群移除该应用
            Map<String, String> map = disable(pks);
            if(map != null)
            {
                map.forEach((pk, error)->
                {
                    String origErr = fails.get(pk);
                    fails.put(pk, origErr == null ? error : origErr + error);
                });
            }
            return fails;
        }
    }

    @Override
    public Map<String, String> setType(@NonNull AppType type, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        throw new UnsupportedOperationException("暂不支持变更应用类型！");
        //        if(type == null)
        //        {
        //            throw new ZNSH_IllegalArgumentException("未指定应用类型！");
        //        } else if(pks == null || pks.length <= 0)
        //        {
        //            throw new ZNSH_IllegalArgumentException("未选择应用！");
        //        } else
        //        {
        //            Map<String, String> fails = new HashMap<>();
        //            List<App> apps = new ArrayList<>(pks.length);
        //            for(String pk : pks)
        //            {
        //                App app = new App();
        //                app.setPk(pk);
        //                app.setAppType(type);
        //                apps.add(app);
        //            }
        //            try
        //            {
        //                for(App app : apps)
        //                {
        //                    boolean success = appDAO.update(app);
        //                }
        //            } catch(ZNSH_DataAccessException e)
        //            {
        //                LOGGER.error("设置应用类型失败！", e);
        //            }
        //            return fails.isEmpty() ? null : fails;
        //        }
    }

    @Override
    public Map<String, String> review(boolean accept, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.REVIEW.withTag(accept ? 1 : 0), pks);
    }

    @Override
    public Map<String, String> disable(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.DISABLE, pks);
    }

    @Override
    public Map<String, String> activate(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.ACTIVATE, pks);
    }

    @Override
    public Map<String, String> suspend(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.SUSPEND, pks);
    }

    @Override
    public Map<String, String> start(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.START, pks);
    }

    @Override
    public Map<String, String> stop(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        return setStatus(Operation.STOP, pks);
    }

    @Override
    public Map<String, String> setCluster(int clusterPk, String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        }
        if(clusterPk < 1)
        {
            throw new ZNSH_IllegalArgumentException("集群编号不正确！");
        }
        Map<String, String> fails = null;
        return fails.isEmpty() ? null : fails;
    }

    private Map<String, String> setStatus(@NonNull Operation op, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        }
        String opName = op.name;
        AppStatus newStatus;
        AppCtlCmd ctlCmd;
        switch(op)
        {
            case REVIEW:
                newStatus = AppStatus.DISABLED;
                ctlCmd = null;
                break;
            case DISABLE:
                newStatus = AppStatus.DISABLED;
                ctlCmd = AppCtlCmd.REMOVE;
                break;
            case ACTIVATE:
                newStatus = AppStatus.NORMAL;
                ctlCmd = AppCtlCmd.ACTIVATE;
                break;
            case SUSPEND:
                newStatus = AppStatus.SUSPEND;
                ctlCmd = AppCtlCmd.SUSPEND;
                break;
            case START:
                newStatus = AppStatus.NORMAL;
                ctlCmd = AppCtlCmd.CREATE;
                break;
            case STOP:
                newStatus = null;
                ctlCmd = AppCtlCmd.REMOVE;
                break;
            default:
                throw new ZNSH_ServiceException("UNE");
        }
        Map<String, String> fails = new HashMap<>();
        for(String pk : pks)
        {
            String errMsg = null;
            try
            {
                App app = appDAO.get(pk);
                if(app != null)
                {
                    if(newStatus != null)
                    {
                        app.updateStatus(app.appId(), newStatus);
                    }
                    try
                    {
                        if(newStatus == null || appDAO.update(app))
                        {
                            if(ctlCmd == null || !notifyAppCluster(pk, ctlCmd))
                            {
                                errMsg = "调整应用服务状态失败！";
                            }
                        } else
                        {
                            errMsg = String.format("更新应用状态'%s'失败！", newStatus);
                        }
                    } catch(ZNSH_DataAccessException e)
                    {
                        errMsg = "查询指定应用失败！";
                    }
                } else
                {
                    errMsg = "指定应用不存在！";
                }
            } catch(ZNSH_DataAccessException e)
            {
                errMsg = "查询指定应用失败！";
            }
            if(errMsg == null)
            {
                LOGGER.info("{}'{}'成功！", pk);
                sysLogger.info(opName, String.format("成功（应用编号=%s）。", pk), LogType.SYS_MANAGE);
            } else
            {
                LOGGER.error("{}'{}'失败！{}！", opName, pk, errMsg);
                sysLogger.error(opName, String.format("失败（应用编号=%s）。", pk), LogType.SYS_MANAGE);
                fails.put(pk, errMsg);
            }
        }
        return fails.isEmpty() ? null : fails;
    }

    private boolean registerApp(Object appResource)
    {
        if(appResource != null)
        {
            String appId = appResource.toString();
            if(JText.isNormal(appId))
            {
                Cluster cluster = null;
                RequestHolder requestHolder = httpClient.prepare();
                requestHolder.setHeader("TOKEN", sysConfigManager.getToken());
                requestHolder.setHeader("APP_ID", appId);
                requestHolder.setHeader("CMD", AppCtlCmd.CREATE.toString());
                requestHolder.post(REQ_ID_CREATE, cluster.appCtlUrl(), handler);
            }
        }
        return false;
    }

    private boolean notifyAppCluster(String appId, AppCtlCmd ctlCmd)
    {
        if(JText.isNormal(appId))
        {
            Cluster cluster = null;
            RequestHolder requestHolder = httpClient.prepare();
            requestHolder.setHeader("TOKEN", sysConfigManager.getToken());
            requestHolder.setHeader("APP_ID", appId);
            requestHolder.setHeader("CMD", ctlCmd.toString());
            requestHolder.post(REQ_ID_REMOVE, cluster.appCtlUrl(), handler);
        }
        return false;
    }

    /**
     * 用于创建/注册应用时，生成编号
     *
     * @param name 应用名称
     *
     * @return 应用编号
     */
    private String newAppId(String name)
    {
        return SecureUtil.getMD5_16(name);
    }

    private Object getAppResource(String appId)
    {
        return null;
    }

    private enum Operation
    {
        REVIEW("审批应用"),
        DISABLE("注销应用"),
        ACTIVATE("激活应用"),
        SUSPEND("挂起应用"),
        START("启动应用"),
        STOP("停止应用");
        String name;
        /**
         * 附加数据
         */
        int tag;

        Operation(String name)
        {
            this.name = name;
        }

        private Operation withTag(int tag)
        {
            this.tag = tag;
            return this;
        }
    }
}