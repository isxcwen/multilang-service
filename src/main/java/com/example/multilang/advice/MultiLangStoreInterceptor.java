package com.example.multilang.advice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.example.multilang.config.MultiLangContext;
import com.example.multilang.model.BaseMultiLang;
import com.example.multilang.model.MultiLangContent;
import com.example.multilang.model.MultiLangModel;
import com.example.multilang.service.MultiLangService;
import com.example.service.MultiLangEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MultiLangStoreInterceptor implements Interceptor {
    @Autowired(required = false)
    @SuppressWarnings("all")
    private MultiLangService multiLangService;
    private MultiLangContext multiLangContext = MultiLangContext.getContext();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        Object arg = invocation.getArgs()[1];
        if(arg instanceof BaseMultiLang){
            MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            List<MultiLangModel> cache = multiLangContext.removeCache();
            if((cache != null && !cache.isEmpty()) && (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE)){
                Long id = ((BaseMultiLang) arg).getId();
                if(sqlCommandType == SqlCommandType.UPDATE){
                    multiLangService.batchDelete(cache.get(0).getTableName(), id);
                }
                if(cache != null && !cache.isEmpty()){
                    multiLangService.batchSave(id, cache);
                }
            }
        }
        return proceed;
    }

    /**
     * <insert id="batchSave" >
     *         insert into dept (
     *         id,
     *         table_name,
     *         table_id,
     *         column_name,
     *         language_code,
     *         content_text,
     *         content_varchar
     *         )
     *         values
     *         <foreach collection="list" item="dept" index="index">
     *             <foreach collection="dept.contents" item="item" index="index" separator="," >
         *             (
         *                  ${@com.example.multilang.config.MultiLangContext@gengrateId()}, #{dept.tableName}, #{dept.id}, #{dept.columnName}, #{item.languageCode},
     *                 <when test="dept.isText">
     *                      #{item.content}, null
             *         </when>
             *         <otherwise>
             *            null, #{item.content}
             *         </otherwise>
         *             )
     *             </foreach>
     *         </foreach>
     *     </insert>
     */
}
