package com.ruoyi.common.casbin.config;

import com.ruoyi.common.casbin.adapter.DatabaseAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Casbin配置类
 * 
 * @author ruoyi
 */
@Configuration
public class CasbinConfig
{
    @Value("${casbin.model:casbin_rbac_model.conf}")
    private String modelPath;

    @Value("${casbin.policy:casbin_policy.csv}")
    private String policyPath;

    @Value("${casbin.enable-auto-save:true}")
    private boolean enableAutoSave;

    @Value("${casbin.enable-log:false}")
    private boolean enableLog;

    @Value("${casbin.adapter.type:database}")
    private String adapterType;

    @Autowired(required = false)
    private DatabaseAdapter databaseAdapter;

    /**
     * 创建Casbin模型
     */
    @Bean
    public Model casbinModel() throws IOException
    {
        ClassPathResource resource = new ClassPathResource(modelPath);
        return Model.newModelFromFile(resource.getFile().getAbsolutePath());
    }

    /**
     * 创建Casbin数据库适配器
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "casbin.adapter.type", havingValue = "database", matchIfMissing = true)
    public Adapter casbinDatabaseAdapter()
    {
        if (databaseAdapter != null)
        {
            return databaseAdapter;
        }
        else
        {
            // 如果数据库适配器不可用，使用文件适配器作为备选
            return casbinFileAdapter();
        }
    }

    /**
     * 创建Casbin文件适配器
     */
    @Bean
    @ConditionalOnProperty(name = "casbin.adapter.type", havingValue = "file")
    public Adapter casbinFileAdapter()
    {
        try
        {
            ClassPathResource resource = new ClassPathResource(policyPath);
            return new FileAdapter(resource.getFile().getAbsolutePath());
        }
        catch (IOException e)
        {
            // 如果策略文件不存在，创建一个空的文件适配器
            return new FileAdapter();
        }
    }

    /**
     * 创建Casbin执行器
     */
    @Bean
    public Enforcer casbinEnforcer(Model model, Adapter adapter)
    {
        Enforcer enforcer = new Enforcer(model, adapter);
        enforcer.enableAutoSave(enableAutoSave);
        enforcer.enableLog(enableLog);
        return enforcer;
    }
}