package com.globalholidaymini.common;

import org.springframework.data.domain.Sort.Direction;

public interface Sortable {

    String getColumnName();

    Direction getDirection();
}
