CREATE TABLE "ISSUES"(
    "KEE" CHARACTER VARYING(50) NOT NULL,
    "RULE_UUID" CHARACTER VARYING(40),
    "SEVERITY" CHARACTER VARYING(10),
    "MANUAL_SEVERITY" BOOLEAN NOT NULL,
    "MESSAGE" CHARACTER VARYING(4000),
    "LINE" INTEGER,
    "GAP" DOUBLE PRECISION,
    "STATUS" CHARACTER VARYING(20),
    "RESOLUTION" CHARACTER VARYING(20),
    "CHECKSUM" CHARACTER VARYING(1000),
    "ASSIGNEE" CHARACTER VARYING(255),
    "AUTHOR_LOGIN" CHARACTER VARYING(255),
    "EFFORT" INTEGER,
    "CREATED_AT" BIGINT,
    "UPDATED_AT" BIGINT,
    "ISSUE_CREATION_DATE" BIGINT,
    "ISSUE_UPDATE_DATE" BIGINT,
    "ISSUE_CLOSE_DATE" BIGINT,
    "TAGS" CHARACTER VARYING(4000),
    "COMPONENT_UUID" CHARACTER VARYING(50),
    "PROJECT_UUID" CHARACTER VARYING(50),
    "LOCATIONS" BINARY LARGE OBJECT,
    "ISSUE_TYPE" TINYINT,
    "FROM_HOTSPOT" BOOLEAN,
    "QUICK_FIX_AVAILABLE" BOOLEAN,
    "RULE_DESCRIPTION_CONTEXT_KEY" CHARACTER VARYING(50),
    "MESSAGE_FORMATTINGS" BINARY LARGE OBJECT
);
ALTER TABLE "ISSUES" ADD CONSTRAINT "PK_ISSUES" PRIMARY KEY("KEE");
CREATE INDEX "ISSUES_ASSIGNEE" ON "ISSUES"("ASSIGNEE" NULLS FIRST);
CREATE INDEX "ISSUES_COMPONENT_UUID" ON "ISSUES"("COMPONENT_UUID" NULLS FIRST);
CREATE INDEX "ISSUES_CREATION_DATE" ON "ISSUES"("ISSUE_CREATION_DATE" NULLS FIRST);
CREATE INDEX "ISSUES_PROJECT_UUID" ON "ISSUES"("PROJECT_UUID" NULLS FIRST);
CREATE INDEX "ISSUES_RESOLUTION" ON "ISSUES"("RESOLUTION" NULLS FIRST);
CREATE INDEX "ISSUES_UPDATED_AT" ON "ISSUES"("UPDATED_AT" NULLS FIRST);
CREATE INDEX "ISSUES_RULE_UUID" ON "ISSUES"("RULE_UUID" NULLS FIRST);
