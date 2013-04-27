/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brucexx.aries.db;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

/**
 * ���ù�����������Դ
 * @author zhao.xiong
 */
@Component("configDBManager")
public class JdbcManager extends JdbcDaoSupport implements BeanFactoryAware {

    private static final Logger logger = Logger.getLogger("config-dal");
    //DB����
    private DBType              dbType;

    private BeanFactory         beanFactory;

    /**
     * 
     * @see org.springframework.jdbc.core.support.JdbcDaoSupport#createJdbcTemplate(javax.sql.DataSource)
     */
    @Override
    protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        DataSourceManager dsm = (DataSourceManager) beanFactory.getBean("dataSourceManager");
        return new JdbcTemplate(dsm.getMainDS());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> executeSqlForList(String sql) {

        return this.getJdbcTemplate().queryForList(sql);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> preExecuteSqlForList(String sql, Object... args) {

        return this.getJdbcTemplate().queryForList(sql, args);
    }

    public void executeSql(String sql) {
        this.getJdbcTemplate().execute(sql);
    }

    public void preExecuteSql(String sql, Object... args) {
        this.getJdbcTemplate().update(sql, args);
    }

    public DBType getDBType() {
        return dbType;
    }

    public List<Map<String, String>> executeSqlForListSMapStr(String sql) {
        //  logger.info("exe_list_sql==>"+sql);
        return this.getJdbcTemplate().queryForList(sql);
    }

    public Map executeForMap(String sql) {
        //    logger.info("exe_map_sql==>"+sql);
        return getJdbcTemplate().queryForMap(sql);
    }

    public Map preExecuteForMap(String sql, Object... args) {
        //    logger.info("exe_map_sql==>"+sql);
        return getJdbcTemplate().queryForMap(sql, args);
    }

    public Page pagedQuery(String sql, int pageNo, int pageSize) {
        if (sql == null) {
            throw new IllegalArgumentException("NULL is not a valid string");
        }
        int totalCount = calculateTotalElementsByList(getJdbcTemplate(), sql);
        if (totalCount < 1) {
            return Page.EMPTY_PAGE;
        }
        if (pageNo < 1) {
            pageNo = 1;
        }
        int startIndex = getStartOfAnyPage(pageNo, pageSize);
        String sqlstr = getQueryStr(sql, pageSize * (pageNo - 1), pageSize);
        if (StringUtils.isEmpty(sqlstr)) {
            return Page.EMPTY_PAGE;
        }
        logger.info("Wrapped QueryStr:" + sqlstr);
        List list = getJdbcTemplate().queryForList(sqlstr.toString());
        int avaCount = list == null ? 0 : list.size();
        return new Page(startIndex, avaCount, totalCount, pageSize, list);
    }

    /**
     * ��ö�Ӧ���ݿ�ķ�ҳ��ѯ��䣨mjb��
     * @param sql
     * @return
     */
    private String getQueryStr(String sql, int start, int end) {
        switch (dbType) {
            case DB2:
                return DBSqlUtil.getDB2PagedSql(sql, start, end);
            case MYSQL:
                return DBSqlUtil.getMysqlPagedSql(sql, start, end);
            case ORACLE:
                return DBSqlUtil.getOraclePagedSql(sql, start, end);
            case SQLSERVER2005:
                return DBSqlUtil.getSqlServer2005PageSql(sql, start, end);
            case SQLSERVER:
                return DBSqlUtil.getSqlServerPageSql(sql, start, end);
            default:
                return "";
        }

    }

    /**
     * ��ȡ��һҳ��һ�����������ݿ��е�λ��
     */
    private static int getStartOfAnyPage(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) {
            startIndex = 1;
        }
        return startIndex;
    }

    /**
     * �����ܼ�¼��(mjb)
     * @param jdbcTemplate
     * @param sql
     * @return
     */
    private int calculateTotalElementsByList(JdbcTemplate jdbcTemplate, String sql) {
        String lower = sql.substring(sql.indexOf("from"));
        String selected = StringUtils.substringBetween(sql, "select", "from");
        if (selected.contains(",")) {
            selected = selected.substring(0, selected.indexOf(","));
        }
        if (sql.indexOf(" group by ") != -1) {

            String groupby = "";
            if (sql.indexOf(" order ") != -1) {
                groupby = sql.substring(sql.indexOf(" group by ") + 10, sql.indexOf(" order "));
            } else {
                groupby = sql.substring(sql.indexOf(" group by ") + 10);
            }
            if (groupby.contains(",")) {
                groupby = groupby.substring(0, groupby.indexOf(","));
            }

            sql = "select count( distinct " + groupby + ") as totalnum " + lower;
            sql = sql.substring(0, sql.indexOf(" group by "));
        } else if (sql.indexOf(" order ") != -1) {
            sql = "select count(" + selected + ") as totalnum " + lower;
            sql = sql.substring(0, sql.indexOf(" order "));
        } else {
            sql = "select count(" + selected + ") as totalnum " + lower;
        }
        logger.info("jdbcPage.sql:" + sql);
        int allcount = jdbcTemplate.queryForInt(sql);
        return allcount;

    }

    public int update(String sql) {
        return getJdbcTemplate().update(sql);
    }

    /**
     * @param dbType the dbType to set
     */
    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    /** 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
