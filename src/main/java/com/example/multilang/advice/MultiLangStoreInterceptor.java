package com.example.multilang.advice;

import com.example.multilang.config.MultiLangContext;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangModel;
import com.example.multilang.service.MultiLangService;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.List;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MultiLangStoreInterceptor implements Interceptor, ApplicationContextAware {
    private MultiLangContext multiLangContext = MultiLangContext.getContext();
    private ApplicationContext applicationContext;
    private MultiLangService multiLangService;


    public MultiLangService getMultiLangService() {
        if (multiLangService == null) {
            multiLangService = applicationContext.getBean(MultiLangService.class);
        }
        return multiLangService;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        Object arg = invocation.getArgs()[1];
        //MapperMethod.ParamMap
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if(arg instanceof BaseMultiLang) {
            handle(sqlCommandType, (BaseMultiLang)arg);
        } else if(arg instanceof MapperMethod.ParamMap){
            BaseMultiLang realValue = getRealValue((MapperMethod.ParamMap)arg);
            if(realValue != null){
                handle(sqlCommandType, realValue);
            }
        }
        return proceed;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private BaseMultiLang getRealValue(MapperMethod.ParamMap paramMap){
        Collection values = paramMap.values();
        for (Object value : values) {
            if(value instanceof BaseMultiLang){
                return (BaseMultiLang)value;
            }
        }
        return null;
    }

    private void handle(SqlCommandType sqlCommandType, BaseMultiLang baseMultiLang){
        MultiLangService multiLangService = getMultiLangService();
        List<MultiLangModel> cache = multiLangContext.removeCache();
        if ((cache != null && !cache.isEmpty())) {
            Long id = baseMultiLang.getId();
            if (sqlCommandType == SqlCommandType.UPDATE) {
                multiLangService.batchDelete(cache.get(0).getTableName(), id);
            }
            if (cache != null && !cache.isEmpty()) {
                multiLangService.batchSave(id, cache);
            }
        }
    }
}
