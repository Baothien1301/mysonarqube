/*
 * SonarQube
 * Copyright (C) 2009-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v102;

import java.sql.SQLException;
import org.junit.Test;
import org.sonar.server.platform.db.migration.step.RenameVarcharColumnChange;
import org.sonar.server.platform.db.migration.version.RenameVarcharColumnAbstractTest;

public class RenameComponentUuidInWebhookDeliveriesTest extends RenameVarcharColumnAbstractTest {

  public RenameComponentUuidInWebhookDeliveriesTest() {
    super("webhook_deliveries", "project_uuid", false);
  }

  @Test
  public void migration_is_reentrant() throws SQLException {
    super.verifyMigrationIsReentrant();
  }

  @Test
  public void column_is_renamed() throws SQLException {
    super.verifyColumnIsRenamed();
  }

  @Override
  protected RenameVarcharColumnChange getClassUnderTest() {
    return new RenameComponentUuidInWebhookDeliveries(db.database());
  }

}
