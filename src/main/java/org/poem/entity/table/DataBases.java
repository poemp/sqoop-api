package org.poem.entity.table;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author poem
 */
@Data
@AllArgsConstructor
public class DataBases {

    /**
     * 数据库
     */
    private String databases;

    /**
     * tables
     */
    private List<Table> tables;

}
