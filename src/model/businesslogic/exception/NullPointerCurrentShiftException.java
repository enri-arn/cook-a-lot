package model.businesslogic.exception;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 * Eccezione che si verifica nel caso in cui un turno e' null.
 */
public class NullPointerCurrentShiftException extends UseCaseLogicException {

    /**
     * Costruttore dell'eccezione.
     */
    public NullPointerCurrentShiftException() {
        super("Il turno corrente e' nullo");
    }
}
