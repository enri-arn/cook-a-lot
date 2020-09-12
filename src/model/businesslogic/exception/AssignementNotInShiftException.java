package model.businesslogic.exception;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 * Eccezione che si verifica nel caso in cui un assegnamento non e' presente in un turno.
 */
public class AssignementNotInShiftException extends UseCaseLogicException {

    /**
     * Costruttore dell'eccezione.
     */
    public AssignementNotInShiftException() {
        super("Assegnamento non presente");
    }
}
