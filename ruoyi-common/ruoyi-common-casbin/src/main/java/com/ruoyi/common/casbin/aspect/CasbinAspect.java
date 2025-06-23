package com.ruoyi.common.casbin.aspect;

import com.ruoyi.common.casbin.annotation.CasbinEnforce;
import com.ruoyi.common.casbin.service.CasbinService;
import com.ruoyi.common.core.exception.auth.NotPermissionException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Casbin权限验证切面
 * 
 * @author ruoyi
 */
@Aspect
@Component
public class CasbinAspect
{
    @Autowired
    private CasbinService casbinService;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.ruoyi.common.casbin.annotation.CasbinEnforce)")
    public void casbinPointcut()
    {
    }

    @Around("casbinPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CasbinEnforce casbinEnforce = method.getAnnotation(CasbinEnforce.class);

        if (casbinEnforce != null)
        {
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            String[] paramNames = discoverer.getParameterNames(method);

            // 创建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            if (paramNames != null)
            {
                for (int i = 0; i < paramNames.length; i++)
                {
                    context.setVariable(paramNames[i], args[i]);
                }
            }

            // 解析主体
            String subject = parseExpression(casbinEnforce.subject(), context);
            if (StringUtils.isEmpty(subject))
            {
                // 默认使用当前登录用户
                subject = SecurityUtils.getUsername();
            }

            // 解析对象
            String object = parseExpression(casbinEnforce.object(), context);
            
            // 解析动作
            String action = parseExpression(casbinEnforce.action(), context);

            // 权限验证
            if (!casbinService.enforce(subject, object, action))
            {
                throw new NotPermissionException(casbinEnforce.message());
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 解析SpEL表达式
     */
    private String parseExpression(String expressionString, EvaluationContext context)
    {
        if (StringUtils.isEmpty(expressionString))
        {
            return "";
        }

        try
        {
            Expression expression = parser.parseExpression(expressionString);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        }
        catch (Exception e)
        {
            // 如果解析失败，返回原始字符串
            return expressionString;
        }
    }
}