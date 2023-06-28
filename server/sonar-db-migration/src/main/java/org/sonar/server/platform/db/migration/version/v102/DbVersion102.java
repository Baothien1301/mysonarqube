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

import org.sonar.server.platform.db.migration.step.MigrationStepRegistry;
import org.sonar.server.platform.db.migration.version.DbVersion;

// ignoring bad number formatting, as it's indented that we align the migration numbers to SQ versions
@SuppressWarnings("java:S3937")
public class DbVersion102 implements DbVersion {

  /**
   * We use the start of the 10.X cycle as an opportunity to align migration numbers with the SQ version number.
   * Please follow this pattern:
   * 10_0_000
   * 10_0_001
   * 10_0_002
   * 10_1_000
   * 10_1_001
   * 10_1_002
   * 10_2_000
   */

  @Override
  public void addSteps(MigrationStepRegistry registry) {
    registry
      .add(10_2_000, "Rename 'component_uuid' in 'user_roles' table to 'entity_uuid'", RenameComponentUuidInUserRoles.class)
      .add(10_2_001, "Rename 'component_uuid' in 'group_roles' table to 'entity_uuid'", RenameComponentUuidInGroupRoles.class)

      .add(10_2_002, "Drop index 'group_roles_component_uuid' in 'group_roles'", DropIndexComponentUuidInGroupRoles.class)
      .add(10_2_003, "Create index 'entity_uuid_user_roles' in 'group_roles' table", CreateIndexEntityUuidInGroupRoles.class)

      .add(10_2_004, "Drop index 'user_roles_component_uuid' in 'user_roles' table", DropIndexComponentUuidInUserRoles.class)
      .add(10_2_005, "Create index 'user_roles_entity_uuid' in 'user_roles'", CreateIndexEntityUuidInUserRoles.class)

      .add(10_2_006, "Drop index 'ce_activity_component' in 'ce_activity'", DropIndexMainComponentUuidInCeActivity.class)
      .add(10_2_007, "Rename 'main_component_uuid' in 'ce_activity' table to 'entity_uuid'", RenameMainComponentUuidInCeActivity.class)
      .add(10_2_008, "Create index 'ce_activity_entity_uuid' in 'ce_activity' table'", CreateIndexEntityUuidInCeActivity.class)

      .add(10_2_009, "Drop index 'ce_queue_main_component' in 'ce_queue' table", DropIndexMainComponentUuidInCeQueue.class)
      .add(10_2_010, "Rename 'main_component_uuid' in 'ce_queue' table to 'entity_uuid'", RenameMainComponentUuidInCeQueue.class)
      .add(10_2_011, "Create index 'ce_queue_entity_uuid' in 'ce_queue' table", CreateIndexEntityUuidInCeQueue.class)

      .add(10_2_012, "Drop 'project_mappings' table", DropTableProjectMappings.class)

      .add(10_2_013, "Drop index on 'components.main_branch_project_uuid", DropIndexOnMainBranchProjectUuid.class)
      .add(10_2_014, "Drop column 'main_branch_project_uuid' in the components table", DropMainBranchProjectUuidInComponents.class)

      .add(10_2_015, "Drop index 'component_uuid' in 'webhook_deliveries' table", DropIndexComponentUuidInWebhookDeliveries.class)
      .add(10_2_016, "Rename 'component_uuid' in 'webhook_deliveries' table to 'project_uuid'", RenameComponentUuidInWebhookDeliveries.class)
      .add(10_2_017, "Create index 'webhook_deliveries_project_uuid' in 'webhook_deliveries' table", CreateIndexProjectUuidInWebhookDeliveries.class)

    ;
  }

}
