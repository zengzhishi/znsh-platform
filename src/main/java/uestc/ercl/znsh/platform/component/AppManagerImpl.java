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
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.component.def.SysConfigManager;
import uestc.ercl.znsh.platform.dao.AppConfigDAO;
import uestc.ercl.znsh.platform.dao.AppDAO;

import java.util.*;

@Component
public class AppManagerImpl implements AppManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppManager.class);
    private static final int REQ_ID_CREATE = 0;
    private static final int REQ_ID_REMOVE = 1;
    private static final int REQ_ID_SUSPEND = 2;
    private static final int REQ_ID_ACTIVATE = 3;
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
        Assert.notNull(configDAO, "AppConfigDAO注入失败！不能为空！");
        Assert.notNull(appDAO, "AppDAO注入失败！不能为空！");
        Assert.notNull(sysConfigManager, "SysConfigManager注入失败！不能为空！");
        this.configDAO = configDAO;
        this.appDAO = appDAO;
        this.sysConfigManager = sysConfigManager;
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
        Date now = new Date();
        App app = new App(newAppId(name), name, desc, type.value(), status.value(), now, now, master, pid, phone, email, account, password);
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
            @Nullable String pid, @Nullable String phone, @Nullable String email, int from, int count)
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
        app.setPk(pk);
        app.setName(name);
        app.setDesc(desc);
        app.setMaster(master);
        app.setPid(pid);
        app.setPhone(phone);
        app.setEmail(email);
        app.setAccount(account);
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
        app.setPk(pk);
        app.setPassword(password);
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
            Map<String, String> fails = disable(pks);
            try
            {
                Set<String> set = appDAO.delete(pks);
            } catch(ZNSH_DataAccessException e)
            {
                e.printStackTrace();
            }
            return fails;
        }
    }

    @Override
    public Map<String, String> setType(@NonNull AppType type, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(type == null)
        {
            throw new ZNSH_IllegalArgumentException("未指定应用类型！");
        } else if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> fails = new HashMap<>();
            List<App> apps = new ArrayList<>(pks.length);
            for(String pk : pks)
            {
                App app = new App();
                app.setPk(pk);
                app.setAppType(type);
                apps.add(app);
            }
            try
            {
                for(App app : apps)
                {
                    boolean success = appDAO.update(app);
                }
            } catch(ZNSH_DataAccessException e)
            {
                LOGGER.error("设置应用类型失败！", e);
            }
            return fails.isEmpty() ? null : fails;
        }
    }

    @Override
    public Map<String, String> review(boolean accept, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> fails = new HashMap<>();
            List<App> apps = new ArrayList<>(pks.length);
            for(String pk : pks)
            {
                App app = new App();
                app.setPk(pk);
                app.setAppStatus(accept ? AppStatus.NORMAL : AppStatus.DISABLED);
                apps.add(app);
            }
            try
            {
                for(App app : apps)
                {
                    appDAO.update(app);
                }
            } catch(ZNSH_DataAccessException e)
            {
                LOGGER.error("审批应用失败！", e);
            }
            return fails.isEmpty() ? null : fails;
        }
    }

    @Override
    public Map<String, String> setCluster(Integer clusterPk, String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> fails = new HashMap<>();
            for(String pk : pks)
            {
            }
            return fails.isEmpty() ? null : fails;
        }
    }

    @Override
    public Map<String, String> activate(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> fails = new HashMap<>();
            for(String pk : pks)
            {
                App app = new App();
                app.setPk(pk);
                app.setAppStatus(AppStatus.NORMAL);
                try
                {
                    if(appDAO.update(app))
                    {
                        if(activateApp(pk))
                        {
                        } else
                        {
                            fails.put(pk, "激活服务失败");
                        }
                    } else
                    {
                        fails.put(pk, "激活服务失败");
                    }
                } catch(ZNSH_DataAccessException e)
                {
                }
            }
            return fails.isEmpty() ? null : fails;
        }
    }

    @Override
    public Map<String, String> suspend(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> result = new HashMap<>();
            for(String pk : pks)
            {
                App app = new App();
                app.setPk(pk);
                app.setAppStatus(AppStatus.SUSPEND);
                try
                {
                    if(appDAO.update(app))
                    {
                        if(activateApp(pk))
                        {
                        } else
                        {
                            result.put(pk, "挂起服务失败");
                        }
                    } else
                    {
                        result.put(pk, "挂起服务失败");
                    }
                } catch(ZNSH_DataAccessException e)
                {
                }
            }
            return result.isEmpty() ? null : result;
        }
    }

    @Override
    public Map<String, String> disable(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择应用！");
        } else
        {
            Map<String, String> result = new HashMap<>();
            for(String pk : pks)
            {
                App app = new App();
                app.setPk(pk);
                app.setAppStatus(AppStatus.DISABLED);
                try
                {
                    if(appDAO.update(app))
                    {
                        if(activateApp(pk))
                        {
                        } else
                        {
                            result.put(pk, "禁用服务失败");
                        }
                    } else
                    {
                        result.put(pk, "禁用服务失败");
                    }
                } catch(ZNSH_DataAccessException e)
                {
                }
            }
            return result.isEmpty() ? null : result;
        }
    }

    public boolean registerApp(Object appResource)
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

    public boolean removeApp(String appId)
    {
        if(JText.isNormal(appId))
        {
            Cluster cluster = null;
            RequestHolder requestHolder = httpClient.prepare();
            requestHolder.setHeader("TOKEN", sysConfigManager.getToken());
            requestHolder.setHeader("APP_ID", appId);
            requestHolder.setHeader("CMD", AppCtlCmd.REMOVE.toString());
            requestHolder.post(REQ_ID_REMOVE, cluster.appCtlUrl(), handler);
        }
        return false;
    }

    public boolean activateApp(String appId)
    {
        if(JText.isNormal(appId))
        {
            Cluster cluster = null;
            RequestHolder requestHolder = httpClient.prepare();
            requestHolder.setHeader("TOKEN", sysConfigManager.getToken());
            requestHolder.setHeader("APP_ID", appId);
            requestHolder.setHeader("CMD", AppCtlCmd.ACTIVATE.toString());
            requestHolder.post(REQ_ID_ACTIVATE, cluster.appCtlUrl(), handler);
        }
        return false;
    }

    public boolean suspendApp(String appId)
    {
        if(JText.isNormal(appId))
        {
            Cluster cluster = null;
            RequestHolder requestHolder = httpClient.prepare();
            requestHolder.setHeader("TOKEN", sysConfigManager.getToken());
            requestHolder.setHeader("APP_ID", appId);
            requestHolder.setHeader("CMD", AppCtlCmd.SUSPEND.toString());
            requestHolder.post(REQ_ID_SUSPEND, cluster.appCtlUrl(), handler);
        }
        return false;
    }

    private String newAppId(String name)
    {
        return SecureUtil.getMD5_16(name);
    }
}