package liquibase.precondition.core;

import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.Precondition;
import liquibase.precondition.PreconditionLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for controling "or" logic in preconditions.
 */
public class OrPrecondition extends PreconditionLogic {

    public Warnings warn(Database database) {
        return new Warnings();
    }

    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }

    public void check(Database database, DatabaseChangeLog changeLog, ChangeSet changeSet) throws PreconditionFailedException, PreconditionErrorException {
        boolean onePassed = false;
        List<FailedPrecondition> failures = new ArrayList<FailedPrecondition>();
        for (Precondition precondition : getNestedPreconditions()) {
            try {
                precondition.check(database, changeLog, changeSet);
                onePassed = true;
                break;
            } catch (PreconditionFailedException e) {
                failures.addAll(e.getFailedPreconditions());
            }
        }
        if (!onePassed) {
            throw new PreconditionFailedException(failures);
        }
    }

    public String getName() {
        return "or";
    }
}
