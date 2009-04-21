package liquibase.database.sql.generator;

import liquibase.database.Database;
import liquibase.database.sql.AddAutoIncrementStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.exception.LiquibaseException;

public class AddAutoIncrementHsqlGenerator implements SqlGenerator {

    public int getApplicability(SqlStatement statement, Database database) {
        if (!(statement instanceof AddAutoIncrementStatement)) {
            return APPLICABILITY_NOT;
        }

        if (database.supportsAutoIncrement()) {
            return APPLICABILITY_DATABASE_SPECIFIC;
        } else {
            return APPLICABILITY_NOT;
        }
    }

    public String[] generateSql(SqlStatement sqlStatement, Database database) throws LiquibaseException {
        AddAutoIncrementStatement statement = (AddAutoIncrementStatement) sqlStatement;
        return new String[]{
                "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " ALTER COLUMN " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) + " " + statement.getColumnDataType() + " GENERATED BY DEFAULT AS IDENTITY IDENTITY"
        };
    }
}