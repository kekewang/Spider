package com.snm;

import com.slob.dao.mapping.MappingManager;
import com.slob.dao.sourcetool.DefaultDataObjectGenerator;
import com.slob.dao.sourcetool.STable;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

public class DataGenerator extends BaseJunit4Test {

    public static void main(String[] args) throws ParseException {
        String str = "2017041900010682|C|21900002|ESP|20170420|910091523952||15019480015|caoyangyang|2017041900010682|caoyangyang|ll0418-美团信用付订单14|500.00|500.00|0.00|0.00|20170420 08:38:03|20170420 08:38:03||3MTAPP|0.00";
        String[] strs = str.split("|");
        java.util.Random r = new java.util.Random();
        System.out.println(r.nextLong());
        System.out.println(String.valueOf(Math.random()).substring(2) + "L");
    }

    DefaultDataObjectGenerator dataGenerator = new DefaultDataObjectGenerator();

    @Resource
    MappingManager mappingManager;

    @Resource
    private SqlSessionFactory sqlSessionFactory;


    @Before(value = "")
    public void setUp() throws Exception {
        dataGenerator.setSqlSessionFactory(sqlSessionFactory);
        dataGenerator.setMappingManager(mappingManager);

    }

    @Test
    public void testTableToPOJO() throws Exception {
        dataGenerator.setSqlSessionFactory(sqlSessionFactory);
        dataGenerator.setMappingManager(mappingManager);
        //
        // dataGenerator.tableToPOJO(true, true);
        // xm datamapping
        dataGenerator.tableToPOJO(true, true);
        // 生成xml 描述文件 。
        // dataGenerator.buildMappingDescFile(false);
    }

    @Test
    public void getTableNames() {
        try {
            List<STable> tables = dataGenerator.getTables();
            for (STable table : tables) {
                // System.out.println("<table tableName=\"" + table.getName() + "\" />");
                System.out.println("ALTER TABLE msm." + table.getName() + " ENGINE = InnoDB ;");
                // System.out.println("delete from msm." + table.getName() + ";");
                // // for (SColumn column : table.getColumns()) {
                // if (column.getColumnName().toUpperCase().equals("SETT_UNIT_ID")) {
                // System.out.println("ALTER TABLE msm." + table.getName() +
                // " CHANGE COLUMN SETT_UNIT_ID SETT_UNIT_ID  VARCHAR(50) NOT NULL COMMENT '结算单元ID';");

                // System.out.println("update  msm." + table.getName() +
                // " set SETT_UNIT_ID = concat('ESP', SETT_UNIT_ID);");
                // break;
                // // }
                // }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
