package com.ruoyi.common.casbin.adapter;

import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Casbin数据库适配器
 * 
 * @author ruoyi
 */
@Component
public class DatabaseAdapter implements Adapter
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "casbin_rule";

    /**
     * 加载策略
     */
    @Override
    public void loadPolicy(Model model)
    {
        String sql = "SELECT ptype, v0, v1, v2, v3, v4, v5 FROM " + TABLE_NAME;
        List<Map<String, Object>> rules = jdbcTemplate.queryForList(sql);
        
        for (Map<String, Object> rule : rules)
        {
            String ptype = (String) rule.get("ptype");
            String[] params = new String[6];
            for (int i = 0; i < 6; i++)
            {
                params[i] = (String) rule.get("v" + i);
            }
            
            // 过滤空值
            int actualParamCount = 0;
            for (String param : params)
            {
                if (param != null && !param.trim().isEmpty())
                {
                    actualParamCount++;
                }
                else
                {
                    break;
                }
            }
            
            String[] actualParams = new String[actualParamCount];
            System.arraycopy(params, 0, actualParams, 0, actualParamCount);
            
            model.addPolicy("p", ptype, actualParams);
        }
    }

    /**
     * 保存策略
     */
    @Override
    public void savePolicy(Model model)
    {
        // 清空现有策略
        String deleteSql = "DELETE FROM " + TABLE_NAME;
        jdbcTemplate.update(deleteSql);
        
        // 保存新策略
        for (Map.Entry<String, Map<String, List<List<String>>>> entry : model.getModel().entrySet())
        {
            String sec = entry.getKey();
            Map<String, List<List<String>>> ast = entry.getValue();
            
            for (Map.Entry<String, List<List<String>>> policyEntry : ast.entrySet())
            {
                String ptype = policyEntry.getKey();
                List<List<String>> rules = policyEntry.getValue();
                
                for (List<String> rule : rules)
                {
                    savePolicyLine(ptype, rule);
                }
            }
        }
    }

    /**
     * 添加策略
     */
    @Override
    public void addPolicy(String sec, String ptype, List<String> rule)
    {
        savePolicyLine(ptype, rule);
    }

    /**
     * 删除策略
     */
    @Override
    public void removePolicy(String sec, String ptype, List<String> rule)
    {
        StringBuilder sql = new StringBuilder("DELETE FROM " + TABLE_NAME + " WHERE ptype = ?");
        Object[] params = new Object[rule.size() + 1];
        params[0] = ptype;
        
        for (int i = 0; i < rule.size(); i++)
        {
            sql.append(" AND v").append(i).append(" = ?");
            params[i + 1] = rule.get(i);
        }
        
        jdbcTemplate.update(sql.toString(), params);
    }

    /**
     * 删除过滤策略
     */
    @Override
    public void removeFilteredPolicy(String sec, String ptype, int fieldIndex, String... fieldValues)
    {
        StringBuilder sql = new StringBuilder("DELETE FROM " + TABLE_NAME + " WHERE ptype = ?");
        Object[] params = new Object[fieldValues.length + 1];
        params[0] = ptype;
        
        for (int i = 0; i < fieldValues.length; i++)
        {
            sql.append(" AND v").append(fieldIndex + i).append(" = ?");
            params[i + 1] = fieldValues[i];
        }
        
        jdbcTemplate.update(sql.toString(), params);
    }

    /**
     * 保存策略行
     */
    private void savePolicyLine(String ptype, List<String> rule)
    {
        String sql = "INSERT INTO " + TABLE_NAME + " (ptype, v0, v1, v2, v3, v4, v5) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[7];
        params[0] = ptype;
        
        for (int i = 0; i < 6; i++)
        {
            if (i < rule.size())
            {
                params[i + 1] = rule.get(i);
            }
            else
            {
                params[i + 1] = null;
            }
        }
        
        jdbcTemplate.update(sql, params);
    }
}