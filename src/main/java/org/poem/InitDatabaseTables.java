package org.poem;

import org.poem.entity.table.DataBases;
import org.poem.entity.table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author poem
 */
public class InitDatabaseTables {

    /**
     * 初始化
     *
     * @return 数据库列表
     */
    public static List<DataBases> getInit() {

        return new ArrayList<DataBases>() {{
            add(new DataBases("cdzg028_shop", new ArrayList<Table>() {{
                add(new Table("skills_match"));
            }}));
            add(new DataBases("cms_service", new ArrayList<Table>() {{
                add(new Table("cms_moudle"));
            }}));
            add(new DataBases("customer_service", new ArrayList<Table>() {{
                add(new Table("customer"));
                add(new Table("customer_info"));
                add(new Table("model_worker_info"));
            }}));
            add(new DataBases("employee_service", new ArrayList<Table>() {{
                add(new Table("admin_organization"));
            }}));
            add(new DataBases("labor_money", new ArrayList<Table>() {{
                add(new Table("pay_cashier_orders"));
            }}));
            add(new DataBases("labor_union", new ArrayList<Table>() {{
                add(new Table("company_base_info"));
            }}));
            add(new DataBases("shop_pay_service", new ArrayList<Table>() {{
                add(new Table("order_item"));
            }}));
        }};
    }
}
