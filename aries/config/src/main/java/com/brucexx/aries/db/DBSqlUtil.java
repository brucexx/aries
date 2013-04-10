package com.brucexx.aries.db;

public class DBSqlUtil {
    // HSQL数据库直接支持分页语句

    public static String getHSQLPagedSql(String sql, int start, int end) {
        return new StringBuffer(sql.length() + 10).append(sql).insert(
                sql.toLowerCase().indexOf("select") + 6, " limit ? ?").toString();
    }

    // Mysql数据库直接支持分页语句
    public static String getMysqlPagedSql(String sql, int start, int end) {
        return new StringBuffer(sql.length() + 20).append(sql).append(
                " limit ?, ?").toString();
    }

    // PostgreSQL数据库直接支持分页语句
    public static String getPostgreSQLPagedSql(String sql, int start, int end) {
        return new StringBuffer(sql.length() + 20).append(sql).append(
                " limit ? offset ?").toString();
    }

    // SqlServer数据库直接支持分页语句
    public static String getSqlServerPageSql(String sqlstr, int spage,
            int perpagenum) {
        // String sql = this.sSQL.toLowerCase(Sys.getLocale());
        String sql = sqlstr;
        String PK = "id";
        String sqltotal = "";
        String con1 = "select ";
        String con2 = " from ";
        String con3 = " where ";
        String con4 = " order ";
        int i1 = sql.indexOf(con1);
        int i2 = sql.indexOf(con2);
        int i3 = sql.indexOf(con3);
        int i4 = sql.indexOf(con4);
        String selectitem = "";
        String tablename = "";
        String cond = "";
        String orders = "";

        String tbsql = "";

        selectitem = sql.substring(i1 + 6, i2);

        if (i3 != -1) {
            tbsql = sqlstr.substring(i2, i3);
            tablename = sql.substring(i2 + 5, i3);
            if (i4 != -1) {
                cond = sql.substring(i3 + 6, i4);
                orders = sql.substring(i4 + 6, sql.length());
            } else {
                cond = sql.substring(i3 + 6, sql.length());
            }
        } else {
            tbsql = sqlstr.substring(i2);
            if (i4 != -1) {
                tablename = sql.substring(i2 + 5, i4);
                orders = sql.substring(i4 + 6, sql.length());
            } else {
                tablename = sql.substring(i2 + 5, sql.length());
            }
        }

        String maintb = tablename;// 主表名称和别名
        if (tablename.contains(",")) {
            maintb = tablename.split(",")[0];
        }

        String[] sqls = maintb.split(" ");
        String tabel = "";
        for (int i = 0; i < sqls.length; i++) {
            if (!sqls[i].trim().equals("")) {
                tabel = sqls[i];
            }
        }

        PK = tabel + ".id";
        String _orders = "";
        if (i4 != -1) {
            _orders = "order " + orders;
        }

        String distin = "";
        if (selectitem.toLowerCase().contains("distinct")) {
            distin = "distinct";
            selectitem = selectitem.substring(selectitem.indexOf("distinct") + 8);
        }

        StringBuffer pagesql = new StringBuffer();
        pagesql.append("select ").append(distin).append(" Top ").append(
                perpagenum).append(" ").append(selectitem).append(" from ").append(tablename).append(" where ");

        if (i3 != -1) {
            pagesql.append("(").append(cond).append(") and ");
        }

        pagesql.append(PK).append(" NOT IN (select Top ").append(spage).append(
                " ").append(PK).append(" from ").append(tablename).append(" ");
        if (i3 != -1) {
            pagesql.append(" where ").append(cond);
        }
        pagesql.append(_orders).append(" ) ").append(_orders);

        return pagesql.toString();
    }

    /**
     * sqlserver2005 分页
     * @param sqlstr
     * @param spage
     * @param perpagenum
     * @return
     */
    public static String getSqlServer2005PageSql(String sqlstr, int spage,
            int perpagenum) {
        int _from = sqlstr.indexOf(" from ");
        int _order = sqlstr.indexOf(" order ");
        int _where = sqlstr.indexOf(" where ");

        String sql1 = sqlstr.substring(0, _from);
        String sql2 = "";
        String tablename = "";
        if (_where != -1) {
            tablename = sqlstr.substring(_from + 5, _where);
        } else {
            if (_order != -1) {
                tablename = sqlstr.substring(_from + 5, _order);
            } else {
                tablename = sqlstr.substring(_from);
            }
        }
        String maintb = tablename;// 主表名称和别名
        if (tablename.contains(",")) {
            maintb = tablename.split(",")[0];
        }
        String[] sqls = maintb.split(" ");
        String tabel = "";
        for (int i = 0; i < sqls.length; i++) {
            if (!sqls[i].trim().equals("")) {
                tabel = sqls[i];
            }
        }
        String orderstr = "order by " + tabel + ".id";
        if (_order != -1) {
            sql2 = sqlstr.substring(_from, _order);
            orderstr = sqlstr.substring(_order);
        } else {
            sql2 = sqlstr.substring(_from);
        }
        StringBuffer pagesql = new StringBuffer();
        pagesql.append("select * from (").append(sql1).append(", ROW_NUMBER() OVER (").append(orderstr).append(") as pos").append(sql2).append(") as T where T.pos>").append(spage).append(" and T.pos<=").append(spage + perpagenum);
        return pagesql.toString();
    }

    // Oracle数据库直接支持分页语句
    public static String getOraclePagedSql(String sql, int start, int perpagenum) {
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        pagingSelect.append(sql);
//		pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
        pagingSelect.append(" ) row_ where rownum <=").append(perpagenum + start).append(") where rownum_ >").append(start);

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

    // DB2数据库直接支持分页语句
    public static String getDB2PagedSql(String sql, int start, int end) {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100).append(sql.substring(0, startOfSelect)) // add the comment
                .append("select * from ( select ") // nest the main query in an
                // outer select
                .append(getRowNumber(sql)); // add the rownnumber bit into the
        // outer query select list

        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ") // add another (inner)
                    // nested select
                    .append(sql.substring(startOfSelect)) // add the main
                    // query
                    .append(" ) as row_"); // close off the inner nested select
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6)); // add the
            // main
            // query
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");

        // add the restriction to the outer select
        pagingSelect.append("between ?+1 and ?");
        return pagingSelect.toString();
    }

    /**
     * Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> bit, that
     * goes in the select list
     */
    private static String getRowNumber(String sql) {
        StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");

        int orderByIndex = sql.toLowerCase().indexOf("order by");

        if (orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");

        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().indexOf("select distinct") >= 0;
    }

    // Interbase数据库直接支持分页语句
    public static String getInterbasePagedSql(String sql, int start, int end) {
        return new StringBuffer(sql.length() + 15).append(sql).append(
                " rows ? to ?").toString();
    }
}
