/**
 * ***************************************************************************
 * Copyright (c) 2018 RiceFish Limited
 * Project: SmartMES
 * Version: 1.6
 *
 * This file is part of SmartMES.
 *
 * SmartMES is Authorized software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.orders.states;

import com.qcadoo.mes.states.StateChangeContext;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.qcadoo.mes.orders.constants.OrderFields.*;

@Service
public class OrderStateValidationService {

    @Autowired
    private DataDefinitionService dataDefinitionService;

    private static final String ENTITY_IS_NULL = "entity is null";

    public void validationOnAccepted(final StateChangeContext stateChangeContext) {
        final List<String> references = Arrays.asList(DATE_TO, DATE_FROM, PRODUCTION_LINE);
        checkRequired(references, stateChangeContext);
    }

    public void validationOnInProgress(final StateChangeContext stateChangeContext) {
        final List<String> references = Arrays.asList(DATE_TO, DATE_FROM);
        checkRequired(references, stateChangeContext);
    }

    public void validationOnCompleted(final StateChangeContext stateChangeContext) {
        final List<String> fieldNames = Arrays.asList(DATE_TO, DATE_FROM, DONE_QUANTITY);
        checkRequired(fieldNames, stateChangeContext);
    }

    private void checkRequired(final List<String> fieldNames, final StateChangeContext stateChangeContext) {
        checkArgument(stateChangeContext != null, ENTITY_IS_NULL);
        final Entity stateChangeEntity = stateChangeContext.getOwner();
        for (String fieldName : fieldNames) {
            if (stateChangeEntity.getField(fieldName) == null) {
                stateChangeContext.addFieldValidationError(fieldName, "orders.order.orderStates.fieldRequired");
            }
        }
    }
}